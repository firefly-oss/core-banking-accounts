-- V12__Create_notification_tables.sql

-- ===========================
-- ACCOUNT_NOTIFICATION
-- ===========================
CREATE TABLE IF NOT EXISTS account_notification (
    account_notification_id BIGSERIAL               PRIMARY KEY,
    account_id              BIGINT                  NOT NULL,
    notification_type       notification_type       NOT NULL,
    title                   VARCHAR(100)            NOT NULL,
    message                 TEXT                    NOT NULL,
    creation_date_time      TIMESTAMP               NOT NULL,
    expiry_date_time        TIMESTAMP,
    is_read                 BOOLEAN                 NOT NULL DEFAULT FALSE,
    read_date_time          TIMESTAMP,
    priority                INTEGER                 NOT NULL DEFAULT 3,
    delivery_channels       VARCHAR(100),
    event_reference         VARCHAR(100),
    related_amount          DECIMAL(18, 4),
    action_url              VARCHAR(255),
    action_text             VARCHAR(100),
    date_created            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_account
        FOREIGN KEY (account_id)
        REFERENCES account (account_id)
);

-- Create index on account_id for faster lookups
CREATE INDEX idx_account_notification_account_id ON account_notification(account_id);

-- Create index on notification_type for faster filtering
CREATE INDEX idx_account_notification_type ON account_notification(notification_type);

-- Create index on is_read for faster filtering of unread notifications
CREATE INDEX idx_account_notification_read ON account_notification(is_read);

-- Create index on priority for faster sorting by priority
CREATE INDEX idx_account_notification_priority ON account_notification(priority);

-- Create index on creation_date_time for faster sorting by date
CREATE INDEX idx_account_notification_creation_date ON account_notification(creation_date_time);
