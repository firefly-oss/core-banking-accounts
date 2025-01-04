-- V9__Create_Audit_Trail_Table.sql
--
-- Purpose:
--   1. Create the audit_log table for capturing DML changes.
--   2. Define a single trigger function (audit_change) that does NOT have formal parameters.
--   3. Pass the PK column name as an argument via TG_ARGV in the CREATE TRIGGER statements.

-------------------
-- 1) Audit Log Table
-------------------
CREATE TABLE IF NOT EXISTS audit_log (
                                         audit_log_id   SERIAL        PRIMARY KEY,
                                         table_name     VARCHAR(100)  NOT NULL,
    record_id      BIGINT        NOT NULL,
    operation      VARCHAR(10)   NOT NULL,   -- e.g., 'INSERT', 'UPDATE', 'DELETE'
    old_data       JSONB,
    new_data       JSONB,
    changed_by     VARCHAR(100)  NOT NULL,
    changed_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

------------------------------------------
-- 2) Trigger Function Without Parameters
------------------------------------------
CREATE OR REPLACE FUNCTION audit_change()
RETURNS TRIGGER AS $$
DECLARE
    -- We read the primary key column name from TG_ARGV[0] (passed in the trigger).
v_pk_col    TEXT;
    v_record_id BIGINT;
BEGIN
    /*
        TG_OP is 'INSERT', 'UPDATE', or 'DELETE'.
        TG_TABLE_NAME is the name of the table where the trigger fired.
        TG_ARGV[n] are arguments passed from the CREATE TRIGGER statement.
    */
    v_pk_col := TG_ARGV[0];  -- e.g., 'account_id', 'account_balance_id', etc.

    IF (TG_OP = 'DELETE') THEN
        EXECUTE format('SELECT ($1).' || quote_ident(v_pk_col))
            INTO v_record_id
            USING OLD;

INSERT INTO audit_log (
    table_name,
    record_id,
    operation,
    old_data,
    changed_by
)
VALUES (
           TG_TABLE_NAME,
           v_record_id,
           'DELETE',
           TO_JSONB(OLD),
           CURRENT_USER
       );

RETURN OLD;

ELSIF (TG_OP = 'UPDATE') THEN
        EXECUTE format('SELECT ($1).' || quote_ident(v_pk_col))
            INTO v_record_id
            USING OLD;

INSERT INTO audit_log (
    table_name,
    record_id,
    operation,
    old_data,
    new_data,
    changed_by
)
VALUES (
           TG_TABLE_NAME,
           v_record_id,
           'UPDATE',
           TO_JSONB(OLD),
           TO_JSONB(NEW),
           CURRENT_USER
       );

RETURN NEW;

ELSIF (TG_OP = 'INSERT') THEN
        EXECUTE format('SELECT ($1).' || quote_ident(v_pk_col))
            INTO v_record_id
            USING NEW;

INSERT INTO audit_log (
    table_name,
    record_id,
    operation,
    new_data,
    changed_by
)
VALUES (
           TG_TABLE_NAME,
           v_record_id,
           'INSERT',
           TO_JSONB(NEW),
           CURRENT_USER
       );

RETURN NEW;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------
-- 3) Example: Apply the Trigger to Tables
--    We pass the pk_col name in the TG_ARGV
-------------------------------------------

-- 3.1. For 'account' table
DROP TRIGGER IF EXISTS account_audit_trigger ON account;
CREATE TRIGGER account_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON account
    FOR EACH ROW
    EXECUTE PROCEDURE audit_change('account_id');

-- 3.2. For 'account_balance' table
DROP TRIGGER IF EXISTS account_balance_audit_trigger ON account_balance;
CREATE TRIGGER account_balance_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON account_balance
    FOR EACH ROW
    EXECUTE PROCEDURE audit_change('account_balance_id');

-- 3.3. Repeat for other tables (account_status_history, account_parameter, etc.)
