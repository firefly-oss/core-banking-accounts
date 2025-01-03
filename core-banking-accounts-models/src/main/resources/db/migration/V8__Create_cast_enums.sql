-- V8__Create_cast_enums.sql

CREATE CAST (varchar AS account_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS balance_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS status_code_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS param_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS provider_status_enum) WITH INOUT AS IMPLICIT;