-- V2__create_tables.sql
-- Create all tables for core-banking-accounts

-- =============================================
-- CREATE TABLES
-- =============================================

-- Account Table
CREATE TABLE account (
    account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_id UUID NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_type VARCHAR(50) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    open_date DATE NOT NULL,
    close_date DATE,
    account_status account_status_enum NOT NULL,
    branch_id UUID NOT NULL,
    description TEXT,
    account_sub_type account_sub_type_enum,
    tax_reporting_status tax_reporting_status_enum,
    regulatory_status regulatory_status_enum,
    maturity_date DATE,
    interest_rate NUMERIC(10, 4),
    interest_accrual_method interest_accrual_method_enum,
    interest_payment_frequency interest_payment_frequency_enum,
    minimum_balance NUMERIC(19, 4),
    overdraft_limit NUMERIC(19, 4),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Account Balance Table
CREATE TABLE account_balance (
    account_balance_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    account_space_id UUID,
    balance_type balance_type_enum NOT NULL,
    balance_amount NUMERIC(19, 4) NOT NULL,
    as_of_datetime TIMESTAMP NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Account Space Table
CREATE TABLE account_space (
    account_space_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    space_name VARCHAR(100) NOT NULL,
    space_type account_space_type_enum NOT NULL,
    balance NUMERIC(19, 4) NOT NULL DEFAULT 0.0000,
    target_amount NUMERIC(19, 4),
    target_date TIMESTAMP,
    icon_id VARCHAR(50),
    color_code VARCHAR(20),
    description TEXT,
    is_visible BOOLEAN NOT NULL DEFAULT TRUE,
    enable_automatic_transfers BOOLEAN NOT NULL DEFAULT FALSE,
    transfer_frequency transfer_frequency_enum,
    transfer_amount NUMERIC(19, 4),
    source_space_id UUID,
    is_frozen BOOLEAN NOT NULL DEFAULT FALSE,
    frozen_datetime TIMESTAMP,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_source_space FOREIGN KEY (source_space_id) REFERENCES account_space(account_space_id)
);

-- Space Transaction Table
CREATE TABLE space_transaction (
    space_transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_space_id UUID NOT NULL REFERENCES account_space(account_space_id),
    amount NUMERIC(19, 4) NOT NULL,
    balance_after_transaction NUMERIC(19, 4) NOT NULL,
    transaction_datetime TIMESTAMP NOT NULL,
    description TEXT,
    reference_id VARCHAR(100),
    transaction_type VARCHAR(50) NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Account Status History Table
CREATE TABLE account_status_history (
    account_status_history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    status_code status_code_enum NOT NULL,
    status_start_datetime TIMESTAMP NOT NULL,
    status_end_datetime TIMESTAMP,
    reason TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Account Parameter Table
CREATE TABLE account_parameter (
    account_parameter_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    param_type param_type_enum NOT NULL,
    param_value NUMERIC(19, 4) NOT NULL,
    param_unit VARCHAR(20) NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP,
    description TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Account Provider Table
CREATE TABLE account_provider (
    account_provider_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    provider_name VARCHAR(100) NOT NULL,
    external_reference VARCHAR(100) NOT NULL,
    status provider_status_enum NOT NULL,
    account_space_id UUID,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_space FOREIGN KEY (account_space_id) REFERENCES account_space(account_space_id)
);

-- Account Notification Table
CREATE TABLE account_notification (
    account_notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES account(account_id),
    notification_type notification_type_enum NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    creation_datetime TIMESTAMP NOT NULL,
    expiry_datetime TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_datetime TIMESTAMP,
    priority INTEGER NOT NULL DEFAULT 0,
    delivery_channels VARCHAR(200),
    event_reference VARCHAR(100),
    related_amount NUMERIC(19, 4),
    action_url VARCHAR(500),
    action_text VARCHAR(100),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- CREATE INDEXES
-- =============================================

-- Account indexes
CREATE INDEX idx_account_contract_id ON account(contract_id);
CREATE INDEX idx_account_account_number ON account(account_number);
CREATE INDEX idx_account_status ON account(account_status);
CREATE INDEX idx_account_open_date ON account(open_date);

-- Account Balance indexes
CREATE INDEX idx_account_balance_account_id ON account_balance(account_id);
CREATE INDEX idx_account_balance_account_space_id ON account_balance(account_space_id);
CREATE INDEX idx_account_balance_as_of_datetime ON account_balance(as_of_datetime);

-- Account Space indexes
CREATE INDEX idx_account_space_account_id ON account_space(account_id);
CREATE INDEX idx_account_space_space_type ON account_space(space_type);

-- Space Transaction indexes
CREATE INDEX idx_space_transaction_account_space_id ON space_transaction(account_space_id);
CREATE INDEX idx_space_transaction_transaction_datetime ON space_transaction(transaction_datetime);
CREATE INDEX idx_space_transaction_reference_id ON space_transaction(reference_id);

-- Account Status History indexes
CREATE INDEX idx_account_status_history_account_id ON account_status_history(account_id);
CREATE INDEX idx_account_status_history_status_code ON account_status_history(status_code);
CREATE INDEX idx_account_status_history_status_start_datetime ON account_status_history(status_start_datetime);

-- Account Parameter indexes
CREATE INDEX idx_account_parameter_account_id ON account_parameter(account_id);
CREATE INDEX idx_account_parameter_param_type ON account_parameter(param_type);
CREATE INDEX idx_account_parameter_effective_date ON account_parameter(effective_date);

-- Account Provider indexes
CREATE INDEX idx_account_provider_account_id ON account_provider(account_id);
CREATE INDEX idx_account_provider_external_reference ON account_provider(external_reference);
CREATE INDEX idx_account_provider_status ON account_provider(status);

-- Account Notification indexes
CREATE INDEX idx_account_notification_account_id ON account_notification(account_id);
CREATE INDEX idx_account_notification_notification_type ON account_notification(notification_type);
CREATE INDEX idx_account_notification_creation_datetime ON account_notification(creation_datetime);
CREATE INDEX idx_account_notification_is_read ON account_notification(is_read);
