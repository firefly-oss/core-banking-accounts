-- V5__Create_space_tables.sql

-- ===========================
-- ACCOUNT_SPACE
-- ===========================
CREATE TABLE IF NOT EXISTS account_space (
    account_space_id   BIGSERIAL               PRIMARY KEY,
    account_id         BIGINT                  NOT NULL,
    space_name         VARCHAR(100)            NOT NULL,
    space_type         account_space_type      NOT NULL,
    balance            DECIMAL(18, 4)          NOT NULL DEFAULT 0,
    target_amount      DECIMAL(18, 4),
    target_date        TIMESTAMP,
    icon_id            VARCHAR(50),
    color_code         VARCHAR(20),
    description        VARCHAR(255),
    is_visible         BOOLEAN                 NOT NULL DEFAULT TRUE,
    date_created       TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated       TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_space_account
        FOREIGN KEY (account_id)
        REFERENCES account (account_id)
);

-- Create index on account_id for faster lookups
CREATE INDEX idx_account_space_account_id ON account_space(account_id);