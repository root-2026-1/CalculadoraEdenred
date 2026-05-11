 -- Fatores de emissão tropicalizados para o Brasil (matriz elétrica MCTI/SIRENE 2023: 38,5 kg CO2/MWh)
-- Base: pesquisa peer-reviewed (Lindgreen 2018, JCB 2024, Riksbank WP 431, Worldline 2024)
-- ajustada para o contexto brasileiro (matriz ~10x mais limpa que NL/JP).
-- PENDENTE: validar com inventário primário Edenred Brasil + ABECS + BCB.
-- Incerteza estimada: +-50%.
-- ON CONFLICT DO UPDATE permite atualizar valores ao reiniciar a aplicação.

INSERT INTO emission_factors (payment_type, co2grams_per_transaction) VALUES
    ('PHYSICAL', 1.40),  -- cartão físico com comprovante impresso (cenário típico BR)
    ('NFC',      0.60),  -- praticamente igual ao físico (mesma infra), sem comprovante
    ('PIX',      0.05),  -- A2A direto via app, infra SPI/BCB
    ('TED',      0.10),  -- transferência bancária via STR, leve overhead vs PIX
    ('UNKNOWN',  0.00)   -- tipo não identificado, sem fator
ON CONFLICT (payment_type) DO UPDATE
    SET co2grams_per_transaction = EXCLUDED.co2grams_per_transaction;

-- Seed: transações de exemplo (só insere se a tabela estiver vazia)
INSERT INTO transactions (company_id, payment_type, amount, transaction_date)
SELECT * FROM (VALUES
    -- Empresa 1: 2 transações digitais → score ~77 (Floresta)
    (1, 'PIX',      150.00, TIMESTAMP '2026-04-01 09:15:00'),
    (1, 'NFC',       89.90, TIMESTAMP '2026-04-03 14:32:00'),
    -- Empresa 2: mix digital/físico → score ~46 (Arbusto)
    (2, 'PHYSICAL', 320.50, TIMESTAMP '2026-04-10 11:00:00'),
    (2, 'TED',     1000.00, TIMESTAMP '2026-04-15 16:45:00'),
    -- Empresa 3: só PIX → score ~96 (Floresta)
    (3, 'PIX',       47.30, TIMESTAMP '2026-04-22 08:05:00'),
    -- Empresa 4: mix com maioria digital → score ~66 (Árvore grande)
    (4, 'TED',      200.00, TIMESTAMP '2026-04-02 10:00:00'),
    (4, 'TED',      350.00, TIMESTAMP '2026-04-05 11:30:00'),
    (4, 'TED',      180.00, TIMESTAMP '2026-04-08 09:45:00'),
    (4, 'TED',      420.00, TIMESTAMP '2026-04-12 14:00:00'),
    (4, 'NFC',       95.00, TIMESTAMP '2026-04-14 16:20:00'),
    (4, 'PIX',       60.00, TIMESTAMP '2026-04-18 08:30:00'),
    (4, 'PHYSICAL', 530.00, TIMESTAMP '2026-04-20 13:10:00'),
    (4, 'PHYSICAL', 275.00, TIMESTAMP '2026-04-25 17:00:00')
) AS v(company_id, payment_type, amount, transaction_date)
WHERE NOT EXISTS (SELECT 1 FROM transactions LIMIT 1);
