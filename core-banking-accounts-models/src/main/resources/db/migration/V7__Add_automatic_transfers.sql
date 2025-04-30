-- V7__Add_automatic_transfers.sql
-- Adds automatic transfer fields to the account_space table

-- Create transfer frequency enum type
CREATE TYPE transfer_frequency_type AS ENUM (
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'QUARTERLY',
    'ANNUALLY'
);

-- Create cast for transfer_frequency_type
CREATE CAST (varchar AS transfer_frequency_type)
    WITH INOUT
    AS IMPLICIT;

-- Add automatic transfer fields to account_space table
ALTER TABLE account_space
    ADD COLUMN enable_automatic_transfers BOOLEAN DEFAULT FALSE,
    ADD COLUMN transfer_frequency transfer_frequency_type,
    ADD COLUMN transfer_amount DECIMAL(18, 4),
    ADD COLUMN source_space_id BIGINT;

-- Add index for faster lookups on source_space_id
CREATE INDEX idx_account_space_source_space_id ON account_space(source_space_id);

-- Add foreign key constraint to ensure source_space_id references a valid account_space
ALTER TABLE account_space
    ADD CONSTRAINT fk_account_space_source
        FOREIGN KEY (source_space_id)
        REFERENCES account_space (account_space_id);

-- Add comment to explain the purpose of the new fields
COMMENT ON COLUMN account_space.enable_automatic_transfers IS 'Whether automatic transfers are enabled for this space';
COMMENT ON COLUMN account_space.transfer_frequency IS 'Frequency of automatic transfers (DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY)';
COMMENT ON COLUMN account_space.transfer_amount IS 'Amount to transfer automatically';
COMMENT ON COLUMN account_space.source_space_id IS 'Source space ID for automatic transfers';
