import { useEffect , useState } from "react"

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

type TransactionForm = {
  amount: number
  type: string
  category: string
  date: string
  description: string
}

type InvestmentForm = {
  symbol: string
  investmentType: string
  operationType: string
  quantity: number
  pricePerUnit: number
  date: string
}

function App() {
  console.log("APP RENDER")

  const [summary, setSummary] = useState<TransactionSummary>({
    totalIncome: 0,
    totalExpenses: 0,
    balance: 0
  })

  const [transactions, setTransactions] = useState<Transaction[]>([])

  const [form, setForm] = useState<TransactionForm>({
  amount: 0,
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
      quantity: 0,
      pricePerUnit: 0,
      date: ""
      })

  const [summarySymbol, setSummarySymbol] = useState("")

  const [investmentSummary, setInvestmentSummary] = useState<InvestmentPositionSummary | null>(null)

  const [transactionTypes, setTransactionTypes] = useState<string[]>([])

  const [transactionCategories, setTransactionCategories] = useState<string[]>([])

  const [investments, setInvestments] = useState<InvestmentTransaction[]>([])

  const [investmentTypes, setInvestmentTypes] = useState<string[]>([])

  const [investmentOperationTypes, setInvestmentOperationTypes] = useState<string[]>([])

  const fetchSummary = () => {
  fetch('http://localhost:8080/transactions/summary')
    .then(response => response.json())
    .then(data => setSummary(data))
  }

  const fetchTransactions = () => {
  fetch('http://localhost:8080/transactions')
    .then(response => response.json())
    .then(data => setTransactions(data))
  }

  useEffect(() => {
  fetchSummary()
  fetchTransactions()
  fetchInvestments()
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/transactions/types')
    .then(response => response.json())
    .then(data => setTransactionTypes(data))
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/transactions/categories')
    .then(response => response.json())
    .then(data => setTransactionCategories(data))
  }, [])

  useEffect(() => {
    fetch('http://localhost:8080/investments/types')
    .then(response => response.json())
    .then(data => setInvestmentTypes(data))
  },[])

  useEffect(() => {
    fetch('http://localhost:8080/investments/operation-types')
    .then(response => response.json())
    .then(data => setInvestmentOperationTypes(data))
  },[])


  
  
  const handleSubmit = (e: React.FormEvent) => {
  e.preventDefault()

  fetch('http://localhost:8080/transactions', {
    method: "POST",
    body: JSON.stringify(form),
    headers: {
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (response.ok) {
        fetchTransactions()
        fetchSummary()
        setForm({
          amount: 0,
          type: "EXPENSE",
          category: "OTHER",
          date: "",
          description: ""
})
      }
    })
  }

  const fetchInvestments = () => {
    fetch('http://localhost:8080/investments')
    .then(response => response.json())
    .then(data => setInvestments(data))
  }

  const handleInvestmentSubmit = (e: React.FormEvent) => {
  e.preventDefault()

  fetch('http://localhost:8080/investments', {
    method: "POST",
    body: JSON.stringify(investmentForm),
    headers: {
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (response.ok) {
        fetchInvestments()
        setInvestmentForm({
          symbol: "",
          investmentType: "STOCK",
          operationType: "BUY",
          quantity: 0,
          pricePerUnit: 0,
          date: ""
        })
      }
    })
  }

  const fetchInvestmentSummary = () => {
  fetch(`http://localhost:8080/investments/${summarySymbol}/summary`)
    .then(response => response.json())
    .then(data => setInvestmentSummary(data))
  }

  return (
    <>

        <h1> SmartWallet działa </h1> <br />
        <h2> Summary: </h2>
        <p>Income: {summary.totalIncome}</p>
        <p>Expenses: {summary.totalExpenses}</p>
        <p>Balance: {summary.balance}</p>

        <br />
        <h2> Transactions: </h2>
        {transactions.map(transaction => (
            <div key={transaction.id}>
                <p>Description: {transaction.description}</p>
                <p>Amount: {transaction.amount}</p>
                <p>Type: {transaction.type}</p>
                <p>Category: {transaction.category}</p>
                <p>Date: {transaction.date} </p>
                <br />
            </div>))}
        
            <form onSubmit={handleSubmit}>
              
              <input
                  type="number"
                  value={form.amount}
                  onChange={(e) => 
                      setForm({...form,amount: Number(e.target.value)})
                  }
              />
              <br />
              <input 
                  type="text" 
                  value={form.description}
                  onChange={(e) => 
                    setForm({...form, description: String(e.target.value)})
                  }
              />
              <br />
              <input 
                type="date"
                value={form.date}
                onChange={(e) =>
                  setForm({...form, date: String(e.target.value)})
                } 
              />

              <br />
              <select
                value={form.type}
                onChange={(e) => 
                    setForm({...form, type: e.target.value
                    })
                }
              >
                {transactionTypes.map(type => (
                  <option key={type} value={type}> {type} </option>
                ))}
              </select>
              
              <br />
              <select
                value={form.category}
                onChange={(e) => 
                    setForm({...form, category: e.target.value
                    })
                }
              >
                {transactionCategories.map(category => (
                  <option key={category} value={category}> {category} </option>
                ))}
              </select>
            <br />
            <button type="submit">Add transaction</button>
            
            </form>
      <h2> Investments </h2>
      <br />
      {investments.map(investment => (

          <div key={investment.id}>
                <p>Symbol: {investment.symbol}</p>
                <p>InvestmentType: {investment.investmentType}</p>
                <p>OperationType: {investment.operationType}</p>
                <p>Quantity: {investment.quantity}</p>
                <p>PricePerUnit: {investment.pricePerUnit}</p>
                <p>Date: {investment.date}</p>
                <br />
          </div>
      ))}  

      <form onSubmit={handleInvestmentSubmit}>

          <input 
            type="text" 
            value={investmentForm.symbol}
            onChange={(e) => setInvestmentForm({...investmentForm, symbol: e.target.value})}
          />
          <br />
          <select 
              value={investmentForm.investmentType}
              onChange={(e) => setInvestmentForm({...investmentForm, investmentType:e.target.value})}>
            {investmentTypes.map(investmentType => (
              <option key={investmentType} value={investmentType}> {investmentType} </option>
            ))}

          </select>
          <br />
          <select
              value={investmentForm.operationType}
              onChange={(e) => setInvestmentForm({...investmentForm, operationType: e.target.value})}
          >
            {investmentOperationTypes.map(operationType => (
              <option key={operationType} value={operationType}> {operationType} </option>
            ))}

          </select>
          <br />
          <input 
            type= "number" 
            value={investmentForm.quantity}
            onChange={(e) => setInvestmentForm({...investmentForm, quantity: Number(e.target.value)})}
          />
          <br />
          <input 
            type="number"
            value={investmentForm.pricePerUnit}
            onChange={(e) => setInvestmentForm({...investmentForm, pricePerUnit: Number(e.target.value)})}
          />
          <br />
          <input 
            type="date"
            value={investmentForm.date}
            onChange={(e) => setInvestmentForm({...investmentForm, date: String(e.target.value)})}
          />
          
          <br />
          <br />
          <button type="submit">Add investment</button>
      </form>
      <br />
      <br />

      <input
        type="text"
        value={summarySymbol}
        onChange={(e) => setSummarySymbol(e.target.value)}
      />
      <br />
      <button type="button" onClick={fetchInvestmentSummary}>
                    Show summary
      </button>
      <br />
      {investmentSummary && (
          <div>
            
            <h2> Summary: </h2>
            
            <p>Current quantity: {investmentSummary.currentQuantity}</p>
            <p>Net invested amount: {investmentSummary.netInvestedAmount}</p>
            <p>Current price: {investmentSummary.currentPrice}</p>
            <p>Current value: {investmentSummary.currentValue}</p>
            <p>Profit / Loss: {investmentSummary.profitLoss}</p>
          </div>
)}

    </>
  )
}

export default App
