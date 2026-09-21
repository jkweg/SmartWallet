import { useEffect, useState } from "react"
import './App.css'

type TransactionSummary = {
  totalIncome: number
  totalExpenses: number
  balance: number
}

type Transaction = {
  id: number
  amount: number
  type: string
  category: string
  date: string
  description: string
}

type InvestmentTransaction = {
  id: number
  symbol: string
  investmentType: string
  operationType: string
  quantity: number
  pricePerUnit: number
  date: string
}

type InvestmentPositionSummary = {
  currentQuantity: number
  netInvestedAmount: number
  currentPrice: number
  currentValue: number
  profitLoss: number
}

type InvestmentPortfolioSummary = {
  totalInvested: number
  currentValue: number
  profitLoss: number
  openPositions: number
}

type TransactionForm = {
  amount: string
  type: string
  category: string
  date: string
  description: string
}

type InvestmentForm = {
  symbol: string
  investmentType: string
  operationType: string
  quantity: string
  pricePerUnit: string
  date: string
}

const formatMoney = (value: number) =>
  value.toLocaleString('en-GB', { minimumFractionDigits: 2, maximumFractionDigits: 2 })

const formatNumber = (value: number) =>
  value.toLocaleString('en-GB', { maximumFractionDigits: 8 })

async function readResponse<T>(response: Response): Promise<T> {
  if (!response.ok) throw new Error('Request failed')
  return response.json()
}

function App() {

  const [summary, setSummary] = useState<TransactionSummary | null>(null)

  const [transactions, setTransactions] = useState<Transaction[]>([])

  const [form, setForm] = useState<TransactionForm>({
    amount: "",
    type: "EXPENSE",
    category: "OTHER",
    date: "",
    description: ""
  })

  const [investmentForm, setInvestmentForm] = useState<InvestmentForm>(
    {
      symbol: "",
      investmentType: "STOCK",
      operationType: "BUY",
      quantity: "",
      pricePerUnit: "",
      date: ""
    })

  const [transactionError, setTransactionError] = useState('')
  const [investmentError, setInvestmentError] = useState('')
  const [summaryError, setSummaryError] = useState('')
  const [dataError, setDataError] = useState('')
  const [savingTransaction, setSavingTransaction] = useState(false)
  const [savingInvestment, setSavingInvestment] = useState(false)
  const [loadingSummary, setLoadingSummary] = useState(false)
  const [loadedSymbol, setLoadedSymbol] = useState('')

  const [summarySymbol, setSummarySymbol] = useState("")

  const [investmentSummary, setInvestmentSummary] = useState<InvestmentPositionSummary | null>(null)

  const [portfolioSummary, setPortfolioSummary] = useState<InvestmentPortfolioSummary | null>(null)

  const [transactionTypes, setTransactionTypes] = useState<string[]>([])

  const [transactionCategories, setTransactionCategories] = useState<string[]>([])

  const [investments, setInvestments] = useState<InvestmentTransaction[]>([])

  const [investmentTypes, setInvestmentTypes] = useState<string[]>([])

  const [investmentOperationTypes, setInvestmentOperationTypes] = useState<string[]>([])

  const fetchSummary = () => {
    fetch('http://localhost:8080/transactions/summary')
      .then(readResponse<TransactionSummary>)
      .then(data => setSummary(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }

  const fetchTransactions = () => {
    fetch('http://localhost:8080/transactions')
      .then(readResponse<Transaction[]>)
      .then(data => setTransactions(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }

  const fetchInvestments = () => {
    fetch('http://localhost:8080/investments')
      .then(readResponse<InvestmentTransaction[]>)
      .then(data => setInvestments(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }

  const fetchPortfolioSummary = () => {
    fetch('http://localhost:8080/investments/portfolio-summary')
      .then(readResponse<InvestmentPortfolioSummary>)
      .then(data => setPortfolioSummary(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }

  useEffect(() => {
    fetchSummary()
    fetchTransactions()
    fetchInvestments()
    fetchPortfolioSummary()
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/transactions/types')
      .then(readResponse<string[]>)
      .then(data => setTransactionTypes(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/transactions/categories')
      .then(readResponse<string[]>)
      .then(data => setTransactionCategories(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/investments/types')
      .then(readResponse<string[]>)
      .then(data => setInvestmentTypes(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/investments/operation-types')
      .then(readResponse<string[]>)
      .then(data => setInvestmentOperationTypes(data))
      .catch(() => setDataError('Some data could not be loaded. Check the backend connection and reload the page.'))
  }, [])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setTransactionError('')
    setSavingTransaction(true)
    try {
      const response = await fetch('http://localhost:8080/transactions', {
        method: 'POST',
        body: JSON.stringify({ ...form, amount: Number(form.amount) }),
        headers: { 'Content-Type': 'application/json' }
      })
      if (!response.ok) throw new Error('Transaction rejected')
      fetchTransactions()
      fetchSummary()
      setForm({ amount: "", type: 'EXPENSE', category: 'OTHER', date: '', description: '' })
    } catch {
      setTransactionError('Could not add the transaction. Check the fields and backend connection, then try again.')
    } finally {
      setSavingTransaction(false)
    }
  }

  const handleInvestmentSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setInvestmentError('')
    setSavingInvestment(true)
    try {
      const response = await fetch('http://localhost:8080/investments', {
        method: 'POST',
        body: JSON.stringify({
          ...investmentForm,
          quantity: Number(investmentForm.quantity),
          pricePerUnit: Number(investmentForm.pricePerUnit)
        }),
        headers: { 'Content-Type': 'application/json' }
      })
      if (!response.ok) throw new Error('Investment rejected')
      fetchInvestments()
      fetchPortfolioSummary()
      setInvestmentForm({ symbol: '', investmentType: 'STOCK', operationType: 'BUY', quantity: "", pricePerUnit: "", date: '' })
    } catch {
      setInvestmentError('Could not add the investment. Check the fields and backend connection, then try again.')
    } finally {
      setSavingInvestment(false)
    }
  }

  const fetchInvestmentSummary = async (e: React.FormEvent) => {
    e.preventDefault()
    const symbol = summarySymbol.trim().toUpperCase()
    setSummaryError('')
    setInvestmentSummary(null)
    if (!symbol) {
      setSummaryError('Enter a symbol, for example AAPL.')
      return
    }
    setLoadingSummary(true)
    try {
      const response = await fetch('http://localhost:8080/investments/' + encodeURIComponent(symbol) + '/summary')
      const data = await readResponse<InvestmentPositionSummary>(response)
      setInvestmentSummary(data)
      setLoadedSymbol(symbol)
    } catch {
      setSummaryError('Could not load this position. Check the symbol and try again; the backend or price provider may be unavailable.')
    } finally {
      setLoadingSummary(false)
    }
  }

  const expensesByCategory: Record<string, number> = {}
  for (const transaction of transactions) {
    if (transaction.type === 'EXPENSE') {
      expensesByCategory[transaction.category] = (expensesByCategory[transaction.category] ?? 0) + transaction.amount
    }
  }
  const expenseCategories = Object.entries(expensesByCategory).sort((a, b) => b[1] - a[1])
  const largestExpense = Math.max(0, ...expenseCategories.map(([, amount]) => amount))

  return (
    <>
      <div className="app">
        <header className="header">
          <h1>SmartWallet</h1>
        </header>

        <main>
          {dataError && <p className="error" role="alert">{dataError}</p>}
          <section className="summary-section" aria-label="Financial overview">
            <div className="summary-grid">
              <div className="summary-card">
                <h3>Income</h3>
                <p>{summary ? formatMoney(summary.totalIncome) + ' PLN' : '—'}</p>
              </div>

              <div className="summary-card">
                <h3>Expenses</h3>
                <p>{summary ? formatMoney(summary.totalExpenses) + ' PLN' : '—'}</p>
              </div>

              <div className="summary-card">
                <h3>Balance</h3>
                <p>{summary ? <><span className={summary.balance > 0 ? 'positive' : summary.balance < 0 ? 'negative' : undefined}>{formatMoney(summary.balance)}</span> PLN</> : '—'}</p>
              </div>
            </div>
          </section>

          <section className="summary-section" aria-labelledby="portfolio-summary-title">
            <h2 id="portfolio-summary-title">Investment portfolio</h2>
            <div className="summary-grid portfolio-summary-grid">
              <div className="summary-card">
                <h3>Total invested</h3>
                <p>{portfolioSummary ? formatMoney(portfolioSummary.totalInvested) : '—'}</p>
              </div>

              <div className="summary-card">
                <h3>Current value</h3>
                <p>{portfolioSummary ? formatMoney(portfolioSummary.currentValue) : '—'}</p>
              </div>

              <div className="summary-card">
                <h3>Profit / Loss</h3>
                <p className={portfolioSummary && portfolioSummary.profitLoss > 0 ? 'positive' : portfolioSummary && portfolioSummary.profitLoss < 0 ? 'negative' : undefined}>
                  {portfolioSummary ? (portfolioSummary.profitLoss > 0 ? '+' : '') + formatMoney(portfolioSummary.profitLoss) : '—'}
                </p>
              </div>

              <div className="summary-card">
                <h3>Open positions</h3>
                <p>{portfolioSummary ? portfolioSummary.openPositions : '—'}</p>
              </div>
            </div>
          </section>

          <section className="expense-chart" aria-labelledby="expense-chart-title">
            <h2 id="expense-chart-title">Expenses by category</h2>
            {expenseCategories.length ? (
              <ul className="chart-list">
                {expenseCategories.map(([category, amount]) => (
                  <li key={category} className="chart-row">
                    <span>{category}</span>
                    <div className="bar-track" aria-hidden="true">
                      <div className="bar" style={{ width: (largestExpense > 0 ? amount / largestExpense * 100 : 0) + '%' }} />
                    </div>
                    <span className="numeric">{formatMoney(amount)} PLN</span>
                  </li>
                ))}
              </ul>
            ) : <p className="empty-state">Add an expense to see spending by category.</p>}
          </section>

          <section className="transactions-section">
            <h2>Transactions</h2>

            <div className="section-grid">

              <div className="panel">
                <h3>Add transaction</h3>

                <form onSubmit={handleSubmit}>
                  <label htmlFor="transaction-amount">Amount (PLN)
                    <input required id="transaction-amount"
                      type="number" min="0.00000001" step="any"
                      value={form.amount}
                      onChange={(e) =>
                        setForm({ ...form, amount: e.target.value })
                      }
                    />
                  </label>
                  <label htmlFor="transaction-description">Description
                    <input id="transaction-description"
                      type="text" placeholder="e.g. Weekly groceries"
                      value={form.description}
                      onChange={(e) =>
                        setForm({ ...form, description: String(e.target.value) })
                      }
                    />
                  </label>
                  <label htmlFor="transaction-date">Date
                    <input required id="transaction-date"
                      type="date"
                      value={form.date}
                      onChange={(e) =>
                        setForm({ ...form, date: String(e.target.value) })
                      }
                    />
                  </label>
                  <label htmlFor="transaction-type">Type
                    <select required id="transaction-type"
                      value={form.type}
                      onChange={(e) =>
                        setForm({
                          ...form, type: e.target.value
                        })
                      }
                    >
                      {transactionTypes.map(type => (
                        <option key={type} value={type}> {type} </option>
                      ))}
                    </select>
                  </label>
                  <label htmlFor="transaction-category">Category
                    <select required id="transaction-category"
                      value={form.category}
                      onChange={(e) =>
                        setForm({
                          ...form, category: e.target.value
                        })
                      }
                    >
                      {transactionCategories.map(category => (
                        <option key={category} value={category}> {category} </option>
                      ))}
                    </select>
                  </label>

                  {transactionError && <p className="error" role="alert">{transactionError}</p>}
                  <button type="submit" disabled={savingTransaction || !transactionTypes.length || !transactionCategories.length}>
                    {savingTransaction ? 'Saving…' : 'Add transaction'}
                  </button>

                </form>
              </div>

              <div className="panel">
                <h3>Transaction history</h3>
                <div className="history-list" tabIndex={0} role="region" aria-label="Transaction history">
                  <table>
                    <thead>
                      <tr>
                        <th scope="col">Description</th>
                        <th scope="col" className="numeric">Amount</th>
                        <th scope="col">Type</th>
                        <th scope="col">Category</th>
                        <th scope="col">Date</th>
                      </tr>
                    </thead>
                    <tbody>
                      {transactions.map(transaction => (
                        <tr key={transaction.id}>
                          <td className="description">{transaction.description || '—'}</td>
                          <td className="numeric">{formatMoney(transaction.amount)} PLN</td>
                          <td>{transaction.type}</td>
                          <td>{transaction.category}</td>
                          <td>{transaction.date}</td>
                        </tr>
                      ))}
                      {!transactions.length && <tr><td colSpan={5} className="empty-state">No transactions to display.</td></tr>}
                    </tbody>
                  </table>
                </div>
              </div>

            </div>

          </section>

          <section className="investments-section">
            <h2>Investments</h2>

            <div className="section-grid">

              <div className="panel">
                <h3>Add investment</h3>

                <form onSubmit={handleInvestmentSubmit}>
                  <label htmlFor="investment-symbol">Symbol
                    <input required id="investment-symbol"
                      type="text" placeholder="e.g. AAPL"
                      value={investmentForm.symbol}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, symbol: e.target.value })}
                    />
                  </label>
                  <label htmlFor="investment-type">Investment type
                    <select required id="investment-type"
                      value={investmentForm.investmentType}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, investmentType: e.target.value })}>
                      {investmentTypes.map(investmentType => (
                        <option key={investmentType} value={investmentType}> {investmentType} </option>
                      ))}

                    </select>
                  </label>
                  <label htmlFor="investment-operation">Operation
                    <select required id="investment-operation"
                      value={investmentForm.operationType}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, operationType: e.target.value })}
                    >
                      {investmentOperationTypes.map(operationType => (
                        <option key={operationType} value={operationType}> {operationType} </option>
                      ))}

                    </select>
                  </label>
                  <label htmlFor="investment-quantity">Quantity
                    <input required id="investment-quantity"
                      type="number" min="0.00000001" step="any"
                      value={investmentForm.quantity}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, quantity: e.target.value })}
                    />
                  </label>
                  <label htmlFor="investment-price">Price per unit
                    <input required id="investment-price"
                      type="number" min="0.00000001" step="any"
                      value={investmentForm.pricePerUnit}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, pricePerUnit: e.target.value })}
                    />
                  </label>
                  <label htmlFor="investment-date">Date
                    <input required id="investment-date"
                      type="date"
                      value={investmentForm.date}
                      onChange={(e) => setInvestmentForm({ ...investmentForm, date: String(e.target.value) })}
                    />
                  </label>

                  {investmentError && <p className="error" role="alert">{investmentError}</p>}
                  <button type="submit" disabled={savingInvestment || !investmentTypes.length || !investmentOperationTypes.length}>
                    {savingInvestment ? 'Saving…' : 'Add investment'}
                  </button>
                </form>
              </div>

              <div className="panel">
                <h3>Investment history</h3>
                <div className="history-list" tabIndex={0} role="region" aria-label="Investment history">
                  <table>
                    <thead>
                      <tr>
                        <th scope="col">Symbol</th>
                        <th scope="col">Type</th>
                        <th scope="col">Operation</th>
                        <th scope="col" className="numeric">Quantity</th>
                        <th scope="col" className="numeric">Unit price</th>
                        <th scope="col">Date</th>
                      </tr>
                    </thead>
                    <tbody>
                      {investments.map(investment => (
                        <tr key={investment.id}>
                          <td>{investment.symbol}</td>
                          <td>{investment.investmentType}</td>
                          <td>{investment.operationType}</td>
                          <td className="numeric">{formatNumber(investment.quantity)}</td>
                          <td className="numeric">{formatNumber(investment.pricePerUnit)}</td>
                          <td>{investment.date}</td>
                        </tr>
                      ))}
                      {!investments.length && <tr><td colSpan={6} className="empty-state">No investments to display.</td></tr>}
                    </tbody>
                  </table>
                </div>
              </div>

            </div>
            <div className="position-summary-card">
              <h3>Position summary</h3>

              <form className="summary-search" onSubmit={fetchInvestmentSummary}>
                <label htmlFor="summary-symbol">Symbol
                  <input required id="summary-symbol"
                    type="text" disabled={loadingSummary}
                    placeholder="Symbol, e.g. AAPL"
                    value={summarySymbol}
                    onChange={(e) => setSummarySymbol(e.target.value)}
                  />
                </label>

                <button type="submit" disabled={loadingSummary}>
                  {loadingSummary ? 'Loading…' : 'Show summary'}
                </button>
              </form>
              {loadingSummary && <p className="muted" role="status">Fetching the position and current market price…</p>}
              {summaryError && <p className="error" role="alert">{summaryError}</p>}
              {investmentSummary && <p className="muted">Position: <strong>{loadedSymbol}</strong></p>}

              {investmentSummary && (
                <div className="position-summary-grid">
                  <div className="position-stat">
                    <span>Quantity</span>
                    <strong>{formatNumber(investmentSummary.currentQuantity)}</strong>
                  </div>

                  <div className="position-stat">
                    <span>Invested</span>
                    <strong>{formatNumber(investmentSummary.netInvestedAmount)}</strong>
                  </div>

                  <div className="position-stat">
                    <span>Current price</span>
                    <strong>{formatNumber(investmentSummary.currentPrice)}</strong>
                  </div>

                  <div className="position-stat">
                    <span>Current value</span>
                    <strong>{formatNumber(investmentSummary.currentValue)}</strong>
                  </div>

                  <div className="position-stat">
                    <span>Profit / Loss</span>
                    <strong className={investmentSummary.profitLoss > 0 ? 'positive' : investmentSummary.profitLoss < 0 ? 'negative' : undefined}>
                      {investmentSummary.profitLoss > 0 ? '+' : ''}{formatNumber(investmentSummary.profitLoss)}
                    </strong>
                  </div>
                </div>
              )}
            </div>
          </section>
        </main>
      </div>
    </>
  )
}

export default App
