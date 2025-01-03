-- V6__Create_Account_Provider_Table.sql

CREATE TABLE IF NOT EXISTS account_provider (
                                                account_provider_id SERIAL PRIMARY KEY,
                                                account_id INTEGER NOT NULL,
                                                provider_name VARCHAR(100) NOT NULL,
    external_reference VARCHAR(100) NOT NULL,
    status provider_status_enum NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_provider_account
    FOREIGN KEY (account_id)
    REFERENCES account(account_id)
    );

CREATE INDEX IF NOT EXISTS idx_account_provider_account_id ON account_provider(account_id);
CREATE INDEX IF NOT EXISTS idx_account_provider_status ON account_provider(status);
CREATE INDEX IF NOT EXISTS idx_account_provider_name ON account_provider(provider_name);
CREATE INDEX IF NOT EXISTS idx_account_provider_external_ref ON account_provider(external_reference);