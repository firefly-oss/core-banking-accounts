-- V3__Create_casts.sql

-----------------------------
-- 1) account_status_type
-----------------------------
CREATE CAST (varchar AS account_status_type)
    WITH INOUT
    AS IMPLICIT;

-----------------------------
-- 2) balance_type
-----------------------------
CREATE CAST (varchar AS balance_type)
    WITH INOUT
    AS IMPLICIT;

-----------------------------
-- 3) status_code_type
-----------------------------
CREATE CAST (varchar AS status_code_type)
    WITH INOUT
    AS IMPLICIT;

-----------------------------
-- 4) param_type
-----------------------------
CREATE CAST (varchar AS param_type)
    WITH INOUT
    AS IMPLICIT;

-----------------------------
-- 5) provider_status_type
-----------------------------
CREATE CAST (varchar AS provider_status_type)
    WITH INOUT
    AS IMPLICIT;
