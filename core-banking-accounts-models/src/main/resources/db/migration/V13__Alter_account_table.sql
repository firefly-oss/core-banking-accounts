-- V13__Alter_account_table.sql

-- Add new columns to the account table
ALTER TABLE account
    ADD COLUMN account_sub_type account_subtype_type,
    ADD COLUMN tax_reporting_status tax_reporting_status,
    ADD COLUMN regulatory_status regulatory_status,
    ADD COLUMN maturity_date DATE,
    ADD COLUMN interest_accrual_method interest_accrual_method,
    ADD COLUMN interest_payment_frequency interest_payment_frequency,
    ADD COLUMN minimum_balance DECIMAL(18, 4) DEFAULT 0,
    ADD COLUMN overdraft_limit DECIMAL(18, 4) DEFAULT 0,
    ADD COLUMN statement_frequency statement_frequency,
    ADD COLUMN statement_delivery_method statement_delivery_method;

-- Create indexes for the new columns
CREATE INDEX idx_account_sub_type ON account(account_sub_type);
CREATE INDEX idx_account_tax_reporting_status ON account(tax_reporting_status);
CREATE INDEX idx_account_regulatory_status ON account(regulatory_status);
CREATE INDEX idx_account_maturity_date ON account(maturity_date);
CREATE INDEX idx_account_interest_accrual_method ON account(interest_accrual_method);
CREATE INDEX idx_account_interest_payment_frequency ON account(interest_payment_frequency);
CREATE INDEX idx_account_statement_frequency ON account(statement_frequency);
CREATE INDEX idx_account_statement_delivery_method ON account(statement_delivery_method);
