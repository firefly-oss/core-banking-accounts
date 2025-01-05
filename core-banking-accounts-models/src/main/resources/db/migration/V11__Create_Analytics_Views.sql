-- V11__Create_Analytics_Views.sql
--
-- Purpose:
--   Provide curated or aggregated “views” for data analysts or BI tools.

-----------------------------------------------------
-- 1) Non-materialized View: 'vw_account_balance_summary'
-----------------------------------------------------
DROP VIEW IF EXISTS vw_account_balance_summary CASCADE;

CREATE OR REPLACE VIEW vw_account_balance_summary AS
SELECT
    a.account_id,
    a.account_status,
    SUM(ab.balance_amount) AS total_balance,
    COUNT(ab.account_balance_id) AS balance_records
FROM account a
         JOIN account_balance ab ON a.account_id = ab.account_id
GROUP BY a.account_id, a.account_status;

------------------------------------------------------------
-- 2) Materialized View: 'mv_daily_balance_stats'
--    Summarizes balance amounts by day and type.
--    Great for quickly querying historical trends.
------------------------------------------------------------
DROP MATERIALIZED VIEW IF EXISTS mv_daily_balance_stats;

CREATE MATERIALIZED VIEW mv_daily_balance_stats AS
SELECT
    DATE_TRUNC('day', ab.as_of_datetime) AS day,
    ab.balance_type,
    SUM(ab.balance_amount) AS total_amount,
    COUNT(DISTINCT ab.account_id) AS unique_accounts
FROM account_balance ab
GROUP BY 1, ab.balance_type
WITH NO DATA;  -- 'WITH NO DATA' means the view is created but not populated yet.

----------------------------------------------------
-- 3) Refresh the Materialized View On-Demand or Scheduled
----------------------------------------------------
-- Optionally, populate (or refresh) the materialized view right away:
-- REFRESH MATERIALIZED VIEW mv_daily_balance_stats;

-- In daily_maintenance (from V12), you could do:
-- REFRESH MATERIALIZED VIEW CONCURRENTLY mv_daily_balance_stats;
