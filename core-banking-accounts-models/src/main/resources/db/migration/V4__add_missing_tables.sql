-- V4__add_missing_tables.sql
-- Add missing tables and enums for core-banking-accounts

-- =============================================
-- CREATE ENUM TYPES
-- =============================================

-- Restriction Type Enum
CREATE TYPE restriction_type_enum AS ENUM (
    'WITHDRAWAL_HOLD',
    'DEPOSIT_HOLD',
    'ACCOUNT_FREEZE',
    'LEGAL_ORDER',
    'COURT_ORDER',
    'FRAUD_INVESTIGATION',
    'DECEASED_HOLDER',
    'DORMANCY',
    'NEGATIVE_BALANCE',
    'EXCESSIVE_OVERDRAFTS',
    'SUSPICIOUS_ACTIVITY',
    'AML_INVESTIGATION',
    'INTERNATIONAL_TRANSFER_RESTRICTION',
    'HIGH_VALUE_TRANSACTION_RESTRICTION',
    'PENDING_DOCUMENTATION',
    'EXPIRED_IDENTIFICATION',
    'BANKRUPTCY',
    'ESTATE_SETTLEMENT',
    'REGULATORY_COMPLIANCE',
    'SANCTIONS_SCREENING',
    'TRANSACTION_TYPE_RESTRICTION',
    'CHANNEL_RESTRICTION',
    'GEOGRAPHIC_RESTRICTION',
    'CLOSURE_IN_PROGRESS'
);

-- =============================================
-- CREATE TABLES
-- =============================================

-- Account Restriction Table
CREATE TABLE account_restriction (
    account_restriction_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    restriction_type restriction_type_enum NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP,
    restricted_amount NUMERIC(19, 4),
    reference_number VARCHAR(100),
    reason TEXT,
    applied_by VARCHAR(100) NOT NULL,
    removed_by VARCHAR(100),
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- CREATE INDEXES
-- =============================================

-- Account Restriction indexes
CREATE INDEX idx_account_restriction_account_id ON account_restriction(account_id);
CREATE INDEX idx_account_restriction_restriction_type ON account_restriction(restriction_type);
CREATE INDEX idx_account_restriction_start_date_time ON account_restriction(start_date_time);
CREATE INDEX idx_account_restriction_is_active ON account_restriction(is_active);
CREATE INDEX idx_account_restriction_reference_number ON account_restriction(reference_number);

-- =============================================
-- CREATE CASTS
-- =============================================

-- Create cast from varchar to restriction_type_enum
CREATE CAST (varchar AS restriction_type_enum) WITH INOUT AS IMPLICIT;