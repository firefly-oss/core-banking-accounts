-- V11__Create_statement_tables.sql

-- ===========================
-- ACCOUNT_STATEMENT
-- ===========================
CREATE TABLE IF NOT EXISTS account_statement (
    account_statement_id    BIGSERIAL               PRIMARY KEY,
    account_id              BIGINT                  NOT NULL,
    statement_number        VARCHAR(50)             NOT NULL,
    period_start_date       DATE                    NOT NULL,
    period_end_date         DATE                    NOT NULL,
    opening_balance         DECIMAL(18, 4)          NOT NULL,
    closing_balance         DECIMAL(18, 4)          NOT NULL,
    total_deposits          DECIMAL(18, 4)          NOT NULL,
    total_withdrawals       DECIMAL(18, 4)          NOT NULL,
    total_fees              DECIMAL(18, 4)          NOT NULL,
    total_interest          DECIMAL(18, 4)          NOT NULL,
    generation_date_time    TIMESTAMP               NOT NULL,
    delivery_method         statement_delivery_method NOT NULL,
    delivery_date_time      TIMESTAMP,
    document_url            VARCHAR(255),
    is_viewed               BOOLEAN                 NOT NULL DEFAULT FALSE,
    first_viewed_date_time  TIMESTAMP,
    date_created            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_statement_account
        FOREIGN KEY (account_id)
        REFERENCES account (account_id)
);

-- Create index on account_id for faster lookups
CREATE INDEX idx_account_statement_account_id ON account_statement(account_id);

-- Create index on period_end_date for faster date-based queries
CREATE INDEX idx_account_statement_period_end_date ON account_statement(period_end_date);

-- Create index on statement_number for faster lookups by statement number
CREATE INDEX idx_account_statement_number ON account_statement(statement_number);

-- Create index on is_viewed for faster filtering of unviewed statements
CREATE INDEX idx_account_statement_viewed ON account_statement(is_viewed);
