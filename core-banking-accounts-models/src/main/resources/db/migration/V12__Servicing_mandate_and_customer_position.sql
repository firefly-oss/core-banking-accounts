-- Stored servicing mandate and customer position.
-- The mandate is a child of the account and cascades with it.
-- The party on either row is a UUID, not a foreign key into another core.

CREATE TABLE servicing_mandate (
    servicing_mandate_id UUID PRIMARY KEY,
    account_id           UUID NOT NULL REFERENCES account (account_id) ON DELETE CASCADE,
    party_id             UUID NOT NULL,
    role                 VARCHAR(16) NOT NULL,
    active               BOOLEAN NOT NULL,
    date_created         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_servicing_mandate_role CHECK (role IN ('OWNER', 'OPERATOR', 'VIEWER'))
);

CREATE TABLE customer_position (
    customer_position_id UUID PRIMARY KEY,
    party_id             UUID NOT NULL,
    currency             VARCHAR(3) NOT NULL,
    balances             NUMERIC(19, 4) NOT NULL,
    credit_limit         NUMERIC(19, 4) NOT NULL,
    credit_used          NUMERIC(19, 4) NOT NULL,
    collateral_value     NUMERIC(19, 4) NOT NULL,
    tax_withheld         NUMERIC(19, 4) NOT NULL,
    available_funds      NUMERIC(19, 4) NOT NULL,
    credit_remaining     NUMERIC(19, 4) NOT NULL,
    date_created         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_customer_position_currency CHECK (currency ~ '^[A-Z]{3}$')
);
