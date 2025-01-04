-- V10__Create_Reporting_Functions_and_Views.sql
--
-- Purpose:
--   1. Provide final reporting functions for regulatory compliance (EBA, SEPBLAC, AML/CTF, liquidity, etc.).
--   2. Create a materialized view (mv_daily_liquidity) for daily liquidity snapshots.
--   3. Define an end_of_day_maintenance() function that refreshes the MV (or does more tasks),
--      intended to be called by an external cron job at day’s end.
--
-- Usage:
--   - Deploy this script via Flyway, Liquibase, or direct psql.
--   - Schedule an external cron:
--         59 23 * * * psql -U myuser -d mydb -c "SELECT end_of_day_maintenance();"
--     to run at 23:59 daily, refreshing data in the materialized view (and performing any
--     other end-of-day logic).

------------------------------
-- 1) Reporting Functions
------------------------------

-------------------------------------------
-- 1.1) EBA/Central Bank: Monthly Exposure
-------------------------------------------
CREATE OR REPLACE FUNCTION get_eba_monthly_exposure_report(
    p_year  INT,
    p_month INT
)
RETURNS TABLE (
    account_id   INT,
    balance_type balance_type_enum,
    total_amount NUMERIC(19,4),
    currency     CHAR(3),
    branch_id    INT,
    risk_class   VARCHAR(50)
)
AS $$
BEGIN
RETURN QUERY
SELECT
    ab.account_id,
    ab.balance_type,
    SUM(ab.balance_amount) AS total_amount,
    a.currency,
    a.branch_id,
    CASE
        WHEN SUM(ab.balance_amount) > 100000 THEN 'HIGH_RISK'
        WHEN SUM(ab.balance_amount) BETWEEN 50000 AND 100000 THEN 'MEDIUM_RISK'
        ELSE 'LOW_RISK'
        END AS risk_class
FROM account_balance ab
         JOIN account a
              ON ab.account_id = a.account_id
WHERE EXTRACT(YEAR FROM ab.as_of_datetime) = p_year
  AND EXTRACT(MONTH FROM ab.as_of_datetime) = p_month
GROUP BY ab.account_id, ab.balance_type, a.currency, a.branch_id;
END;
$$ LANGUAGE plpgsql;


-----------------------------------------------------------
-- 1.2) SEPBLAC Suspicious Accounts (Frequent Suspensions)
-----------------------------------------------------------
CREATE OR REPLACE FUNCTION get_sepblac_suspicious_accounts(
    p_days_lookback INT DEFAULT 30
)
RETURNS TABLE (
    account_id      INT,
    current_status  account_status_enum,
    times_suspended INT,
    max_balance     NUMERIC(19,4),
    flagged_reason  VARCHAR(100)
)
AS $$
BEGIN
RETURN QUERY
    WITH recent_suspensions AS (
        SELECT
            ash.account_id,
            COUNT(*) FILTER (WHERE ash.status_code = 'SUSPENDED') AS susp_count
        FROM account_status_history ash
        WHERE ash.date_created >= NOW() - (p_days_lookback || ' days')::INTERVAL
        GROUP BY ash.account_id
    ),
    large_balance AS (
        SELECT
            ab.account_id,
            MAX(ab.balance_amount) AS max_recent_balance
        FROM account_balance ab
        WHERE ab.as_of_datetime >= NOW() - (p_days_lookback || ' days')::INTERVAL
        GROUP BY ab.account_id
    )
SELECT
    a.account_id,
    a.account_status AS current_status,
    COALESCE(rs.susp_count, 0) AS times_suspended,
    COALESCE(lb.max_recent_balance, 0.00) AS max_balance,
    CASE
        WHEN COALESCE(rs.susp_count, 0) >= 3 AND COALESCE(lb.max_recent_balance, 0) > 50000
            THEN 'MULTIPLE_SUSPENSIONS_AND_HIGH_BALANCE'
        WHEN COALESCE(rs.susp_count, 0) >= 3
            THEN 'MULTIPLE_SUSPENSIONS'
        WHEN COALESCE(lb.max_recent_balance, 0) > 50000
            THEN 'HIGH_BALANCE_SPIKE'
        ELSE 'NO_FLAG'
        END AS flagged_reason
FROM account a
         LEFT JOIN recent_suspensions rs ON a.account_id = rs.account_id
         LEFT JOIN large_balance lb      ON a.account_id = lb.account_id
WHERE COALESCE(rs.susp_count, 0) >= 3
   OR COALESCE(lb.max_recent_balance, 0) > 50000
ORDER BY flagged_reason DESC, a.account_id;
END;
$$ LANGUAGE plpgsql;


-----------------------------------------------------
-- 1.3) Daily Liquidity Report (Basel III/IV LCR)
-----------------------------------------------------
CREATE OR REPLACE FUNCTION get_daily_liquidity_report(
    p_report_date DATE DEFAULT CURRENT_DATE
)
RETURNS TABLE (
    report_date       DATE,
    account_id        INT,
    available_balance NUMERIC(19,4),
    currency          CHAR(3)
)
AS $$
BEGIN
RETURN QUERY
SELECT
    DATE_TRUNC('day', ab.as_of_datetime)::DATE AS report_date,
        ab.account_id,
    SUM(ab.balance_amount) AS available_balance,
    a.currency
FROM account_balance ab
         JOIN account a ON a.account_id = ab.account_id
WHERE ab.balance_type = 'AVAILABLE'
  AND DATE_TRUNC('day', ab.as_of_datetime)::DATE = p_report_date
GROUP BY DATE_TRUNC('day', ab.as_of_datetime), ab.account_id, a.currency;
END;
$$ LANGUAGE plpgsql;


---------------------------------------------
-- 1.4) FINREP Monthly Report (Status + Bal)
---------------------------------------------
CREATE OR REPLACE FUNCTION get_finrep_monthly_report(
    p_year  INT,
    p_month INT
)
RETURNS TABLE (
    account_id     INT,
    account_status account_status_enum,
    status_changes INT,
    total_balance  NUMERIC(19,4),
    currency       CHAR(3)
)
AS $$
BEGIN
RETURN QUERY
    WITH status_change_cte AS (
        SELECT
            ash.account_id,
            COUNT(*) AS num_status_changes
        FROM account_status_history ash
        WHERE EXTRACT(YEAR FROM ash.status_start_datetime) = p_year
          AND EXTRACT(MONTH FROM ash.status_start_datetime) = p_month
        GROUP BY ash.account_id
    ),
    balance_cte AS (
        SELECT
            ab.account_id,
            SUM(ab.balance_amount) AS monthly_balance
        FROM account_balance ab
        WHERE EXTRACT(YEAR FROM ab.as_of_datetime) = p_year
          AND EXTRACT(MONTH FROM ab.as_of_datetime) = p_month
        GROUP BY ab.account_id
    )
SELECT
    a.account_id,
    a.account_status,
    COALESCE(sc.num_status_changes, 0) AS status_changes,
    COALESCE(bc.monthly_balance, 0)    AS total_balance,
    a.currency
FROM account a
         LEFT JOIN status_change_cte sc ON a.account_id = sc.account_id
         LEFT JOIN balance_cte      bc ON a.account_id = bc.account_id;
END;
$$ LANGUAGE plpgsql;


-------------------------------------------------
-- 1.5) AML/CTF Threshold Watchlist
-------------------------------------------------
CREATE OR REPLACE FUNCTION get_aml_ctf_watchlist_accounts(
    p_threshold NUMERIC(19,4) DEFAULT 10000
)
RETURNS TABLE (
    account_id        INT,
    highest_balance   NUMERIC(19,4),
    last_balance_date TIMESTAMP,
    risk_assessment   VARCHAR(50)
)
AS $$
BEGIN
RETURN QUERY
SELECT
    ab.account_id,
    MAX(ab.balance_amount) AS highest_balance,
    MAX(ab.as_of_datetime) AS last_balance_date,
    CASE
        WHEN MAX(ab.balance_amount) > p_threshold THEN 'THRESHOLD_EXCEEDED'
        ELSE 'NORMAL'
        END AS risk_assessment
FROM account_balance ab
GROUP BY ab.account_id
HAVING MAX(ab.balance_amount) > p_threshold
ORDER BY MAX(ab.balance_amount) DESC;
END;
$$ LANGUAGE plpgsql;


------------------------------
-- 2) Materialized View
--    For storing daily liquidity snapshot
------------------------------

DROP MATERIALIZED VIEW IF EXISTS mv_daily_liquidity CASCADE;

CREATE MATERIALIZED VIEW mv_daily_liquidity AS
SELECT *
FROM get_daily_liquidity_report(CURRENT_DATE)
    WITH NO DATA;

-- Optionally, do an initial populate:
-- REFRESH MATERIALIZED VIEW mv_daily_liquidity;

-- If you want to allow "REFRESH MATERIALIZED VIEW CONCURRENTLY",
-- make sure you create a UNIQUE INDEX on 'mv_daily_liquidity'
-- that covers all rows (e.g., on (report_date, account_id)).


------------------------------
-- 3) End-of-Day Maintenance
--    3.1) A function that you call from an external cron at 23:59,
--         which refreshes the MV (and can do more tasks).
------------------------------

CREATE OR REPLACE FUNCTION end_of_day_maintenance()
RETURNS VOID AS $$
BEGIN
    RAISE NOTICE 'End-of-day maintenance started at %', NOW();

    -- Refresh the materialized view (snapshot for the day).
    -- Use CONCURRENTLY if you need reads to continue during refresh
    -- and if your MV has a unique index. Otherwise, plain REFRESH is fine.
    REFRESH MATERIALIZED VIEW mv_daily_liquidity;

    -- Example: Additional housekeeping or reporting tasks:
    -- PERFORM get_sepblac_suspicious_accounts(1);
    -- PERFORM get_finrep_monthly_report(EXTRACT(YEAR FROM CURRENT_DATE)::INT, EXTRACT(MONTH FROM CURRENT_DATE)::INT);
    -- etc.

    RAISE NOTICE 'End-of-day maintenance completed at %', NOW();
END;
$$ LANGUAGE plpgsql;


-- End of V10__Create_Reporting_Functions_and_Views.sql
