-- V9__add_crypto_balance_columns.sql
-- Add crypto-specific columns to the account_balance table

-- Add asset symbol column
ALTER TABLE account_balance ADD COLUMN asset_symbol VARCHAR(20);

-- Add asset decimals column
ALTER TABLE account_balance ADD COLUMN asset_decimals VARCHAR(10);

-- Add transaction hash column
ALTER TABLE account_balance ADD COLUMN transaction_hash VARCHAR(100);

-- Add confirmations column
ALTER TABLE account_balance ADD COLUMN confirmations INTEGER;

-- Add comment to explain the purpose of these columns
COMMENT ON COLUMN account_balance.asset_symbol IS 'Symbol or ticker of the crypto asset (BTC, ETH, USDC, etc.)';
COMMENT ON COLUMN account_balance.asset_decimals IS 'Number of decimal places used by the token';
COMMENT ON COLUMN account_balance.transaction_hash IS 'Blockchain transaction hash/ID that affected this balance';
COMMENT ON COLUMN account_balance.confirmations IS 'Number of blockchain confirmations for the transaction';