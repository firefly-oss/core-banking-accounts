-- Stored statement, limit, savings interest, term-deposit payout, and space sweep.
-- Each row is a child of the account and cascades with it.
-- Space ids stay inside this core and reference account_space.

CREATE TABLE account_statement (
    account_statement_id UUID PRIMARY KEY,
    account_id           UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    currency             VARCHAR(3) NOT NULL,
    opening_balance      NUMERIC(19, 4) NOT NULL,
    credits              NUMERIC(19, 4) NOT NULL,
    debits               NUMERIC(19, 4) NOT NULL,
    closing_balance      NUMERIC(19, 4) NOT NULL,
    date_created         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_account_statement_currency CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE TABLE account_limit (
    account_limit_id  UUID PRIMARY KEY,
    account_id        UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    currency          VARCHAR(3) NOT NULL,
    withdrawal_limit  NUMERIC(19, 4) NOT NULL,
    overdraft_limit   NUMERIC(19, 4) NOT NULL,
    balance           NUMERIC(19, 4) NOT NULL,
    withdrawal_amount NUMERIC(19, 4) NOT NULL,
    allowed           BOOLEAN NOT NULL,
    date_created      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_account_limit_currency CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE TABLE savings_interest (
    savings_interest_id  UUID PRIMARY KEY,
    account_id           UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    currency             VARCHAR(3) NOT NULL,
    balance              NUMERIC(19, 4) NOT NULL,
    annual_rate_percent  NUMERIC(19, 4) NOT NULL,
    day_count            INTEGER NOT NULL,
    interest_amount      NUMERIC(19, 4) NOT NULL,
    date_created         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_savings_interest_currency CHECK (currency ~ '^[A-Z]{3}$'),
    CONSTRAINT ck_savings_interest_days CHECK (day_count >= 0)
);

CREATE TABLE term_deposit (
    term_deposit_id      UUID PRIMARY KEY,
    account_id           UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    currency             VARCHAR(3) NOT NULL,
    principal            NUMERIC(19, 4) NOT NULL,
    annual_rate_percent  NUMERIC(19, 4) NOT NULL,
    open_date            DATE NOT NULL,
    maturity_date        DATE NOT NULL,
    withdrawal_date      DATE NOT NULL,
    penalty_rate_percent NUMERIC(19, 4) NOT NULL,
    payout_amount        NUMERIC(19, 4) NOT NULL,
    date_created         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_deposit_currency CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE TABLE account_sweep (
    account_sweep_id UUID PRIMARY KEY,
    account_id       UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    source_space_id  UUID NOT NULL REFERENCES account_space (account_space_id),
    target_space_id  UUID NOT NULL REFERENCES account_space (account_space_id),
    amount           NUMERIC(19, 4) NOT NULL,
    currency         VARCHAR(3) NOT NULL,
    date_created     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_account_sweep_currency CHECK (currency ~ '^[A-Z]{3}$'),
    CONSTRAINT ck_account_sweep_amount CHECK (amount > 0),
    CONSTRAINT ck_account_sweep_spaces CHECK (source_space_id <> target_space_id)
);
