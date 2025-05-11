-- V10__Create_restriction_tables.sql

-- ===========================
-- ACCOUNT_RESTRICTION
-- ===========================
CREATE TABLE IF NOT EXISTS account_restriction (
    account_restriction_id   BIGSERIAL               PRIMARY KEY,
    account_id              BIGINT                  NOT NULL,
    restriction_type        restriction_type        NOT NULL,
    start_date_time         TIMESTAMP               NOT NULL,
    end_date_time           TIMESTAMP,
    restricted_amount       DECIMAL(18, 4),
    reference_number        VARCHAR(50)             NOT NULL,
    reason                  VARCHAR(255)            NOT NULL,
    applied_by              VARCHAR(100)            NOT NULL,
    removed_by              VARCHAR(100),
    notes                   TEXT,
    is_active               BOOLEAN                 NOT NULL DEFAULT TRUE,
    date_created            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_restriction_account
        FOREIGN KEY (account_id)
        REFERENCES account (account_id)
);

-- Create index on account_id for faster lookups
CREATE INDEX idx_account_restriction_account_id ON account_restriction(account_id);

-- Create index on restriction_type for faster filtering
CREATE INDEX idx_account_restriction_type ON account_restriction(restriction_type);

-- Create index on is_active for faster filtering of active restrictions
CREATE INDEX idx_account_restriction_active ON account_restriction(is_active);
