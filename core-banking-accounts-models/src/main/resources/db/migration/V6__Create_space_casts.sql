-- V6__Create_space_casts.sql

-----------------------------
-- 1) account_space_type
-----------------------------
CREATE CAST (varchar AS account_space_type)
    WITH INOUT
    AS IMPLICIT;