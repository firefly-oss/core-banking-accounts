-- V2__Create_tables.sql

-- ===========================
-- ACCOUNT
-- ===========================
CREATE TABLE IF NOT EXISTS account (
                                       account_id       BIGINT        NOT NULL PRIMARY KEY,
                                       contract_id      BIGINT        NOT NULL,
                                       account_number   VARCHAR(50)   NOT NULL,
    account_type     VARCHAR(50)   NOT NULL,
    currency         VARCHAR(3)    NOT NULL, -- e.g., 'USD', 'EUR'
    open_date        DATE          NOT NULL,
    close_date       DATE,
    account_status   account_status_type NOT NULL DEFAULT 'OPEN',
    branch_id        BIGINT        NOT NULL,
    description      VARCHAR(255),
    date_created     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- ===========================
-- ACCOUNT_BALANCE
-- ===========================
CREATE TABLE IF NOT EXISTS account_balance (
                                               account_balance_id BIGINT          NOT NULL PRIMARY KEY,
                                               account_id         BIGINT          NOT NULL,
                                               balance_type       balance_type    NOT NULL,
                                               balance_amount     DECIMAL(18, 2)  NOT NULL,
    as_of_datetime     TIMESTAMP       NOT NULL,
    date_created       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_balance_account
    FOREIGN KEY (account_id) REFERENCES account (account_id)
    );

-- ===========================
-- ACCOUNT_STATUS_HISTORY
-- ===========================
CREATE TABLE IF NOT EXISTS account_status_history (
                                                      account_status_history_id BIGINT           NOT NULL PRIMARY KEY,
                                                      account_id                BIGINT           NOT NULL,
                                                      status_code               status_code_type NOT NULL,
                                                      status_start_datetime     TIMESTAMP        NOT NULL,
                                                      status_end_datetime       TIMESTAMP,
                                                      reason                    VARCHAR(255),
    date_created              TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated              TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_account
    FOREIGN KEY (account_id) REFERENCES account (account_id)
    );

-- ===========================
-- ACCOUNT_PARAMETER
-- ===========================
CREATE TABLE IF NOT EXISTS account_parameter (
                                                 account_parameter_id BIGINT         NOT NULL PRIMARY KEY,
                                                 account_id           BIGINT         NOT NULL,
                                                 param_type           param_type     NOT NULL,
                                                 param_value          DECIMAL(18, 4) NOT NULL,
    param_unit           VARCHAR(10),
    effective_date       TIMESTAMP      NOT NULL,
    expiry_date          TIMESTAMP,
    description          VARCHAR(255),
    date_created         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_param_account
    FOREIGN KEY (account_id) REFERENCES account (account_id)
    );

-- ===========================
-- ACCOUNT_PROVIDER
-- ===========================
CREATE TABLE IF NOT EXISTS account_provider (
                                                account_provider_id BIGINT                NOT NULL PRIMARY KEY,
                                                account_id          BIGINT                NOT NULL,
                                                provider_name       VARCHAR(100)          NOT NULL,
    external_reference  VARCHAR(100),
    status             provider_status_type   NOT NULL DEFAULT 'ACTIVE',
    date_created        TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated        TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_provider_account
    FOREIGN KEY (account_id) REFERENCES account (account_id)
    );
