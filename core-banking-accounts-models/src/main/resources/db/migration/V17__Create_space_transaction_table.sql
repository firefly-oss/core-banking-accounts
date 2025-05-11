-- V17__Create_space_transaction_table.sql

-- ===========================
-- SPACE_TRANSACTION
-- ===========================
CREATE TABLE IF NOT EXISTS space_transaction (
    space_transaction_id   BIGSERIAL               PRIMARY KEY,
    account_space_id       BIGINT                  NOT NULL,
    amount                 DECIMAL(18, 4)          NOT NULL,
    balance_after_transaction DECIMAL(18, 4)       NOT NULL,
    transaction_date_time  TIMESTAMP               NOT NULL,
    description            VARCHAR(255),
    reference_id           VARCHAR(100),
    transaction_type       VARCHAR(50),
    date_created           TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated           TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_space_transaction_account_space
        FOREIGN KEY (account_space_id)
        REFERENCES account_space (account_space_id)
        ON DELETE CASCADE
);

-- Create indexes for faster lookups
CREATE INDEX idx_space_transaction_account_space_id ON space_transaction(account_space_id);
CREATE INDEX idx_space_transaction_transaction_date_time ON space_transaction(transaction_date_time);
CREATE INDEX idx_space_transaction_reference_id ON space_transaction(reference_id);
CREATE INDEX idx_space_transaction_transaction_type ON space_transaction(transaction_type);

COMMENT ON TABLE space_transaction IS 'Stores transaction history for account spaces';
COMMENT ON COLUMN space_transaction.space_transaction_id IS 'Unique identifier for the transaction';
COMMENT ON COLUMN space_transaction.account_space_id IS 'Reference to the associated account space';
COMMENT ON COLUMN space_transaction.amount IS 'Transaction amount (positive for deposits, negative for withdrawals)';
COMMENT ON COLUMN space_transaction.balance_after_transaction IS 'Balance after this transaction was applied';
COMMENT ON COLUMN space_transaction.transaction_date_time IS 'When the transaction occurred';
COMMENT ON COLUMN space_transaction.description IS 'Description of the transaction';
COMMENT ON COLUMN space_transaction.reference_id IS 'Optional reference ID for cross-system tracking';
COMMENT ON COLUMN space_transaction.transaction_type IS 'Type of transaction (DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST, FEE)';
