-- V4__Create_Account_Status_History_Table.sql

CREATE TABLE IF NOT EXISTS account_status_history (
                                                      account_status_history_id SERIAL PRIMARY KEY,
                                                      account_id INTEGER NOT NULL,
                                                      status_code status_code_enum NOT NULL,
                                                      status_start_datetime TIMESTAMP NOT NULL,
                                                      status_end_datetime TIMESTAMP,
                                                      reason TEXT,
                                                      date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                      date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                      CONSTRAINT fk_account_status_history_account
                                                      FOREIGN KEY (account_id)
    REFERENCES account(account_id)
    );

CREATE INDEX IF NOT EXISTS idx_account_status_history_account_id ON account_status_history(account_id);
CREATE INDEX IF NOT EXISTS idx_account_status_history_code ON account_status_history(status_code);
CREATE INDEX IF NOT EXISTS idx_account_status_history_dates ON account_status_history(status_start_datetime, status_end_datetime);