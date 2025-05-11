-- V15__Add_space_status_fields.sql

-- Add status management fields to account_space table
ALTER TABLE account_space
    ADD COLUMN is_frozen BOOLEAN DEFAULT FALSE,
    ADD COLUMN frozen_date_time TIMESTAMP,
    ADD COLUMN unfrozen_date_time TIMESTAMP,
    ADD COLUMN last_balance_update_reason VARCHAR(255),
    ADD COLUMN last_balance_update_date_time TIMESTAMP;

-- Create index on is_frozen for faster lookups
CREATE INDEX idx_account_space_is_frozen ON account_space(is_frozen);
