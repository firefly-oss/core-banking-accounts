-- V8__Add_account_space_id_to_balance.sql

-- Add account_space_id column to account_balance table
ALTER TABLE account_balance
    ADD COLUMN account_space_id BIGINT NULL;

-- Add foreign key constraint to reference account_space table
ALTER TABLE account_balance
    ADD CONSTRAINT fk_balance_account_space
        FOREIGN KEY (account_space_id)
            REFERENCES account_space (account_space_id);

-- Create index on account_space_id for faster lookups
CREATE INDEX idx_account_balance_account_space_id ON account_balance(account_space_id);

-- Create index on the combination of account_id and account_space_id for faster lookups
CREATE INDEX idx_account_balance_account_space ON account_balance(account_id, account_space_id);