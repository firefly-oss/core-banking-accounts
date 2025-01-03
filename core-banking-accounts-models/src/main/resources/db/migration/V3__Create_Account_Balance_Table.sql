-- V3__Create_Account_Balance_Table.sql

CREATE TABLE IF NOT EXISTS account_balance (
                                               account_balance_id SERIAL PRIMARY KEY,
                                               account_id INTEGER NOT NULL,
                                               balance_type balance_type_enum NOT NULL,
                                               balance_amount DECIMAL(19,4) NOT NULL,
    as_of_datetime TIMESTAMP NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_balance_account
    FOREIGN KEY (account_id)
    REFERENCES account(account_id)
    );

CREATE INDEX IF NOT EXISTS idx_account_balance_account_id ON account_balance(account_id);
CREATE INDEX IF NOT EXISTS idx_account_balance_type ON account_balance(balance_type);
CREATE INDEX IF NOT EXISTS idx_account_balance_datetime ON account_balance(as_of_datetime);
CREATE INDEX IF NOT EXISTS idx_account_balance_amount ON account_balance(balance_amount);