-- V14__create_account_type_enum_cast.sql
-- Create the varchar cast for account_type_enum.
-- V5 introduced the type and V6 replaced it. Neither created the cast.

-- =============================================
-- CREATE CASTS (VARCHAR -> ENUM)
-- =============================================

CREATE CAST (varchar AS account_type_enum) WITH INOUT AS IMPLICIT;
