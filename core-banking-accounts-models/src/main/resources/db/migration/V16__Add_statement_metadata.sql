-- V16__Add_statement_metadata.sql

-- Add metadata column to account_statement table
ALTER TABLE account_statement
    ADD COLUMN metadata TEXT;

-- Create index on metadata for faster lookups
CREATE INDEX idx_account_statement_metadata ON account_statement USING GIN (to_tsvector('english', metadata));

COMMENT ON COLUMN account_statement.metadata IS 'Additional metadata in JSON format, used to store space-specific information or other custom data';
