import './ScoreCard.css';

function scoreColor(score) {
  if (score >= 67) return 'green';
  if (score >= 34) return 'yellow';
  return 'red';
}

export default function ScoreCard({ score }) {
  if (!score) return <div className="score-empty">Sem dados para o período selecionado.</div>;

  const color = scoreColor(score.score);
  const co2SavedKg = (score.co2Saved / 1000).toFixed(2);
  const digitalPct = score.totalTransactions > 0
    ? ((score.digitalTransactions / score.totalTransactions) * 100).toFixed(1)
    : '0.0';

  return (
    <div className="score-card">
      <div className={`score-ring ${color}`}>
        <span className="score-value">{score.score.toFixed(1)}</span>
        <span className="score-label">/ 100</span>
      </div>
      <div className="score-details">
        <div className="score-stat">
          <span className="stat-value">{co2SavedKg} kg</span>
          <span className="stat-label">CO₂ economizado</span>
        </div>
        <div className="score-stat">
          <span className="stat-value">{score.totalTransactions}</span>
          <span className="stat-label">transações</span>
        </div>
        <div className="score-stat">
          <span className="stat-value">{digitalPct}%</span>
          <span className="stat-label">digitais</span>
        </div>
      </div>
    </div>
  );
}
