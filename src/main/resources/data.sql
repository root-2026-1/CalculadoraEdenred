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
