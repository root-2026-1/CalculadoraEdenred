import { useState, useEffect, useCallback } from 'react';
import { fetchHistory, fetchScore } from '../services/api';
import ScoreCard from './ScoreCard';
import TransactionHistory from './TransactionHistory';
import './Dashboard.css';

function today() {
  return new Date().toISOString().slice(0, 10);
}

function firstOfMonth() {
  const d = new Date();
  return new Date(d.getFullYear(), d.getMonth(), 1).toISOString().slice(0, 10);
}

export default function Dashboard() {
  const [companyId, setCompanyId] = useState('1');
  const [startDate, setStartDate] = useState(firstOfMonth());
  const [endDate, setEndDate] = useState(today());
  const [score, setScore] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = useCallback(async () => {
    if (!companyId || !startDate || !endDate) return;
    setLoading(true);
    setError(null);
    try {
      const [scoreData, historyData] = await Promise.all([
        fetchScore(companyId, startDate, endDate),
        fetchHistory(companyId, startDate, endDate),
      ]);
      setScore(scoreData);
      setTransactions(historyData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [companyId, startDate, endDate]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Painel de Sustentabilidade</h1>
        <p>Impacto ambiental das transações digitais vs. físicas</p>
      </header>

      <section className="filter-bar">
        <label className="filter-field">
          <span>Empresa (ID)</span>
          <input
            type="number"
            value={companyId}
            min="1"
            onChange={(e) => setCompanyId(e.target.value)}
          />
        </label>
        <label className="filter-field">
          <span>Data inicial</span>
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
        </label>
        <label className="filter-field">
          <span>Data final</span>
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
        </label>
      </section>

      {loading && <div className="dashboard-loading">Carregando...</div>}
      {error && <div className="dashboard-error">{error}</div>}

      {!loading && !error && (
        <>
          <section className="dashboard-section">
            <h2>Score de sustentabilidade</h2>
            <ScoreCard score={score} />
          </section>

          <section className="dashboard-section">
            <h2>Histórico de transações</h2>
            <TransactionHistory transactions={transactions} />
          </section>
        </>
      )}
    </div>
  );
}
