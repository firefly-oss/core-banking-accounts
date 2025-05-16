-- V6__add_consent_table.sql
-- Add consent management for PSD2/PSD3 and FIDA compliance

-- Create consent type enum
CREATE TYPE consent_type_enum AS ENUM (
    'ACCOUNT_INFORMATION',
    'PAYMENT_INITIATION',
    'FUNDS_CONFIRMATION',
    'INVESTMENT_INFORMATION',
    'INSURANCE_INFORMATION',
    'PENSION_INFORMATION',
    'CREDIT_INFORMATION'
);

-- Create consent status enum
CREATE TYPE consent_status_enum AS ENUM (
    'RECEIVED',
    'AUTHORIZED',
    'REJECTED',
    'REVOKED',
    'EXPIRED',
    'TERMINATED'
);

-- Create account consent table
CREATE TABLE account_consent (
    consent_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    contract_id BIGINT NOT NULL,
    tpp_id VARCHAR(100) NOT NULL,
    tpp_name VARCHAR(255),
    consent_type consent_type_enum NOT NULL,
    consent_status consent_status_enum NOT NULL,
    creation_date_time TIMESTAMP NOT NULL,
    authorization_date_time TIMESTAMP,
    expiration_date_time TIMESTAMP NOT NULL,
    last_used_date_time TIMESTAMP,
    usage_count INTEGER NOT NULL DEFAULT 0,
    frequency_per_day VARCHAR(50),
    consent_scope TEXT,
    description TEXT,
    authentication_reference VARCHAR(255),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_account_consent_account_id ON account_consent(account_id);
CREATE INDEX idx_account_consent_contract_id ON account_consent(contract_id);
CREATE INDEX idx_account_consent_tpp_id ON account_consent(tpp_id);
CREATE INDEX idx_account_consent_consent_status ON account_consent(consent_status);
CREATE INDEX idx_account_consent_expiration_date_time ON account_consent(expiration_date_time);
