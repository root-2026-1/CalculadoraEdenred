import './App.css'
import Dashboard from './components/Dashboard/Dashboard'
import Login from './components/Login/Login'
import { useAuth } from './context/AuthContext'

// Mockup com 2 telas: sem roteador. Mostra Login ou Dashboard conforme a sessão.
export default function App() {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? <Dashboard /> : <Login />
}
