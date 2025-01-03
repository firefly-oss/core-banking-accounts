-- V5__Create_Account_Parameter_Table.sql

CREATE TABLE IF NOT EXISTS account_parameter (
                                                 account_parameter_id SERIAL PRIMARY KEY,
                                                 account_id INTEGER NOT NULL,
                                                 param_type param_type_enum NOT NULL,
                                                 param_value DECIMAL(19,4) NOT NULL,
    param_unit VARCHAR(10) NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP,
    description TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_parameter_account
    FOREIGN KEY (account_id)
    REFERENCES account(account_id)
    );

CREATE INDEX IF NOT EXISTS idx_account_parameter_account_id ON account_parameter(account_id);
CREATE INDEX IF NOT EXISTS idx_account_parameter_type ON account_parameter(param_type);
CREATE INDEX IF NOT EXISTS idx_account_parameter_dates ON account_parameter(effective_date, expiry_date);
CREATE INDEX IF NOT EXISTS idx_account_parameter_value ON account_parameter(param_value);