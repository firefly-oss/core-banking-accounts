-- V2__Create_Account_Table.sql
CREATE TABLE IF NOT EXISTS account (
                                       account_id SERIAL PRIMARY KEY,
                                       contract_id INTEGER NOT NULL,
                                       account_number VARCHAR(34) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    currency CHAR(3) NOT NULL,
    open_date DATE NOT NULL,
    close_date DATE,
    account_status account_status_enum NOT NULL,
    branch_id INTEGER NOT NULL,
    description TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_number_unique UNIQUE (account_number)
    );

CREATE INDEX IF NOT EXISTS idx_account_contract_id ON account(contract_id);
CREATE INDEX IF NOT EXISTS idx_account_branch_id ON account(branch_id);
CREATE INDEX IF NOT EXISTS idx_account_status ON account(account_status);
CREATE INDEX IF NOT EXISTS idx_account_open_date ON account(open_date);
CREATE INDEX IF NOT EXISTS idx_account_currency ON account(currency);