-- V12__Database_Maintenance_Using_Triggers.sql
--
-- This migration defines regular DML triggers and housekeeping functions,
-- allowing automated maintenance without requiring PostgreSQL superuser privileges.

-------------------------------
-- 1) Configuration Table
-------------------------------
CREATE TABLE IF NOT EXISTS housekeeping_config (
                                                   config_id   INT PRIMARY KEY,
                                                   last_run    TIMESTAMP NOT NULL
);

INSERT INTO housekeeping_config (config_id, last_run)
SELECT 1, '1970-01-01 00:00:00'
    WHERE NOT EXISTS (SELECT 1 FROM housekeeping_config WHERE config_id = 1);

-------------------------------
-- 2) Housekeeping Function
-------------------------------
CREATE OR REPLACE FUNCTION run_housekeeping_tasks()
RETURNS VOID AS $$
BEGIN
DELETE FROM audit_log
WHERE changed_at < NOW() - INTERVAL '7 days';

-- Add additional housekeeping if needed

UPDATE housekeeping_config
SET last_run = NOW()
WHERE config_id = 1;
END;
$$ LANGUAGE plpgsql;

-------------------------------
-- 3) Trigger Function
-------------------------------
CREATE OR REPLACE FUNCTION housekeeping_trigger()
RETURNS TRIGGER AS $$
DECLARE
v_last_run TIMESTAMP;
BEGIN
SELECT last_run INTO v_last_run
FROM housekeeping_config
WHERE config_id = 1;

IF v_last_run < (NOW() - INTERVAL '1 day') THEN
        PERFORM run_housekeeping_tasks();
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-------------------------------
-- 4) Apply to account_balance
-------------------------------
DROP TRIGGER IF EXISTS trig_account_balance_housekeeping ON account_balance;

CREATE TRIGGER trig_account_balance_housekeeping
    AFTER INSERT OR UPDATE OR DELETE
                    ON account_balance
                        FOR EACH STATEMENT
                        EXECUTE PROCEDURE housekeeping_trigger();

---------------------------------------
-- 5) Apply to account_status_history
---------------------------------------
DROP TRIGGER IF EXISTS trig_status_history_housekeeping ON account_status_history;

CREATE TRIGGER trig_status_history_housekeeping
    AFTER INSERT OR UPDATE OR DELETE
                    ON account_status_history
                        FOR EACH STATEMENT
                        EXECUTE PROCEDURE housekeeping_trigger();
