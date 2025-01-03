-- V7__Create_Triggers.sql

CREATE OR REPLACE FUNCTION update_date_updated_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.date_updated = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS update_account_date_updated ON account;
CREATE TRIGGER update_account_date_updated
    BEFORE UPDATE ON account
    FOR EACH ROW
    EXECUTE FUNCTION update_date_updated_column();

DROP TRIGGER IF EXISTS update_account_balance_date_updated ON account_balance;
CREATE TRIGGER update_account_balance_date_updated
    BEFORE UPDATE ON account_balance
    FOR EACH ROW
    EXECUTE FUNCTION update_date_updated_column();

DROP TRIGGER IF EXISTS update_account_status_history_date_updated ON account_status_history;
CREATE TRIGGER update_account_status_history_date_updated
    BEFORE UPDATE ON account_status_history
    FOR EACH ROW
    EXECUTE FUNCTION update_date_updated_column();

DROP TRIGGER IF EXISTS update_account_parameter_date_updated ON account_parameter;
CREATE TRIGGER update_account_parameter_date_updated
    BEFORE UPDATE ON account_parameter
    FOR EACH ROW
    EXECUTE FUNCTION update_date_updated_column();

DROP TRIGGER IF EXISTS update_account_provider_date_updated ON account_provider;
CREATE TRIGGER update_account_provider_date_updated
    BEFORE UPDATE ON account_provider
    FOR EACH ROW
    EXECUTE FUNCTION update_date_updated_column();