import './TransactionHistory.css';

const TYPE_LABELS = {
  PIX: 'PIX',
  NFC: 'NFC',
  TED: 'TED',
  PHYSICAL: 'Físico',
  UNKNOWN: 'Desconhecido',
};

function formatDate(dt) {
  return new Date(dt).toLocaleString('pt-BR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  });
}

export default function TransactionHistory({ transactions }) {
  if (!transactions || transactions.length === 0) {
    return <p className="tx-empty">Nenhuma transação encontrada para o período.</p>;
  }

  return (
    <div className="table-wrapper">
      <table className="tx-table">
        <thead>
          <tr>
            <th>Data</th>
            <th>Tipo</th>
            <th>Valor (R$)</th>
            <th>CO₂ (g)</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((tx) => (
            <tr key={tx.id} className={tx.paymentType === 'PHYSICAL' ? 'row-physical' : 'row-digital'}>
              <td>{formatDate(tx.transactionDate)}</td>
              <td>
                <span className={`badge badge-${tx.paymentType.toLowerCase()}`}>
                  {TYPE_LABELS[tx.paymentType] ?? tx.paymentType}
                </span>
              </td>
              <td>{tx.amount.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</td>
              <td>{tx.co2Grams.toFixed(0)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
