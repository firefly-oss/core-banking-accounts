-- V7__add_crypto_account_columns.sql
-- Add crypto-specific columns to the account table

-- Add wallet address column
ALTER TABLE account ADD COLUMN wallet_address VARCHAR(255);

-- Add blockchain network column
ALTER TABLE account ADD COLUMN blockchain_network VARCHAR(50);

-- Add token contract address column
ALTER TABLE account ADD COLUMN token_contract_address VARCHAR(255);

-- Add token standard column
ALTER TABLE account ADD COLUMN token_standard VARCHAR(50);

-- Add is custodial column
ALTER TABLE account ADD COLUMN is_custodial BOOLEAN;

-- Add comment to explain the purpose of these columns
COMMENT ON COLUMN account.wallet_address IS 'Public blockchain address for crypto accounts';
COMMENT ON COLUMN account.blockchain_network IS 'Blockchain network for the crypto account (Bitcoin, Ethereum, etc.)';
COMMENT ON COLUMN account.token_contract_address IS 'Smart contract address for tokenized assets';
COMMENT ON COLUMN account.token_standard IS 'Token standard for the crypto asset (ERC-20, ERC-721, etc.)';
COMMENT ON COLUMN account.is_custodial IS 'Indicates whether the bank holds the private keys (true) or the customer manages their own keys (false)';