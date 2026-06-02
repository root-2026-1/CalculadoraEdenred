-- Fatores de emissão tropicalizados para o Brasil (matriz elétrica MCTI/SIRENE 2023: 38,5 kg CO2/MWh)
-- Referência: Lindgreen 2018, JCB 2024, Riksbank WP 431, Worldline 2024 — ajustados para matriz BR.
-- Este arquivo só executa com spring.sql.init.mode=always. O seed oficial é o DataInitializer.java.
INSERT INTO emission_factors (payment_type, co2grams_per_transaction) VALUES
    ('PHYSICAL', 0.98),
    ('NFC',      0.85),
    ('PIX',      0.13),
    ('TED',      0.13),
    ('UNKNOWN',  0.98)
ON CONFLICT (payment_type) DO UPDATE
    SET co2grams_per_transaction = EXCLUDED.co2grams_per_transaction;
