-- V3__create_casts.sql
-- Create casts from varchar to enum types for core-banking-accounts

-- =============================================
-- CREATE CASTS (VARCHAR -> ENUM)
-- =============================================

CREATE CAST (varchar AS account_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS status_code_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS balance_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS account_sub_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS account_space_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS transfer_frequency_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS provider_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS notification_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS param_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS interest_accrual_method_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS interest_payment_frequency_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS regulatory_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS tax_reporting_status_enum) WITH INOUT AS IMPLICIT;
