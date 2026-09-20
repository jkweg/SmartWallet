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

type TransactionForm = {
  amount: number
  type: string
  category: string
  date: string
  description: string
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

  const [transactionTypes, setTransactionTypes] = useState<string[]>([])

  const [transactionCategories, setTransactionCategories] = useState<string[]>([])

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
        
            <form name="form1" onSubmit={handleSubmit}>
              
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

    </>
  )
}

export default App
