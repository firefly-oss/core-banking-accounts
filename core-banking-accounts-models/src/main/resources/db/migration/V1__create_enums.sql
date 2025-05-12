-- V1__create_enums.sql
-- Create all enum types for core-banking-accounts

-- =============================================
-- CREATE ENUM TYPES
-- =============================================

-- Account Status Enum
CREATE TYPE account_status_enum AS ENUM (
    'OPEN',
    'CLOSED',
    'SUSPENDED',
    'DORMANT'
);

-- Status Code Enum (for status history)
CREATE TYPE status_code_enum AS ENUM (
    'OPEN',
    'SUSPENDED',
    'CLOSED',
    'DORMANT'
);

-- Balance Type Enum
CREATE TYPE balance_type_enum AS ENUM (
    'CURRENT',
    'AVAILABLE',
    'BLOCKED'
);

-- Account Sub Type Enum
CREATE TYPE account_sub_type_enum AS ENUM (
    'STANDARD_CHECKING',
    'INTEREST_CHECKING',
    'REWARDS_CHECKING',
    'FREE_CHECKING',
    'PREMIUM_CHECKING',
    'STANDARD_SAVINGS',
    'HIGH_YIELD_SAVINGS',
    'MONEY_MARKET',
    'CERTIFICATE_OF_DEPOSIT',
    'IRA',
    'FIXED_TERM_DEPOSIT',
    'VARIABLE_TERM_DEPOSIT',
    'PERSONAL_LOAN',
    'AUTO_LOAN',
    'HELOC',
    'STANDARD_CREDIT_CARD',
    'REWARDS_CREDIT_CARD',
    'CASHBACK_CREDIT_CARD',
    'TRAVEL_CREDIT_CARD',
    'BUSINESS_CREDIT_CARD',
    'FIXED_RATE_MORTGAGE',
    'ADJUSTABLE_RATE_MORTGAGE',
    'INTEREST_ONLY_MORTGAGE',
    'STANDARD_BUSINESS_CHECKING',
    'PREMIUM_BUSINESS_CHECKING',
    'STANDARD_BUSINESS_SAVINGS',
    'PREMIUM_BUSINESS_SAVINGS',
    'MULTI_CURRENCY',
    'SINGLE_CURRENCY_FOREIGN',
    'RELATIONSHIP_PREMIUM',
    'WEALTH_MANAGEMENT',
    'YOUTH_SAVINGS',
    'STUDENT_CHECKING',
    'SENIOR_CHECKING',
    'SENIOR_SAVINGS',
    'PENSION_DISTRIBUTION',
    'SOCIAL_SECURITY_BENEFITS',
    'GOVERNMENT_ASSISTANCE'
);

-- Account Space Type Enum
CREATE TYPE account_space_type_enum AS ENUM (
    'MAIN',
    'SAVINGS',
    'VACATION',
    'EMERGENCY',
    'GOALS',
    'CUSTOM'
);

-- Transfer Frequency Enum
CREATE TYPE transfer_frequency_enum AS ENUM (
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'QUARTERLY',
    'ANNUALLY'
);

-- Provider Status Enum
CREATE TYPE provider_status_enum AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'PENDING',
    'SUSPENDED'
);

-- Notification Type Enum
CREATE TYPE notification_type_enum AS ENUM (
    'LOW_BALANCE',
    'LARGE_DEPOSIT',
    'LARGE_WITHDRAWAL',
    'OVERDRAFT',
    'INSUFFICIENT_FUNDS',
    'APPROACHING_MINIMUM_BALANCE',
    'STATEMENT_AVAILABLE',
    'INTEREST_PAYMENT',
    'FEE_CHARGED',
    'STATUS_CHANGE',
    'APPROACHING_MATURITY',
    'MATURITY_REACHED',
    'SUSPICIOUS_ACTIVITY',
    'RESTRICTION_APPLIED',
    'RESTRICTION_REMOVED',
    'APPROACHING_DORMANCY',
    'DORMANCY_APPLIED',
    'GOAL_PROGRESS',
    'GOAL_ACHIEVED',
    'AUTOMATIC_TRANSFER',
    'AUTOMATIC_TRANSFER_FAILED',
    'BALANCE_THRESHOLD',
    'PARAMETER_CHANGE',
    'ACCOUNT_CLOSURE'
);

-- Parameter Type Enum
CREATE TYPE param_type_enum AS ENUM (
    'MONTHLY_FEE',
    'OVERDRAFT_LIMIT',
    'INTEREST_RATE'
);

-- Interest Accrual Method Enum
CREATE TYPE interest_accrual_method_enum AS ENUM (
    'SIMPLE',
    'DAILY_COMPOUND',
    'MONTHLY_COMPOUND',
    'QUARTERLY_COMPOUND',
    'SEMI_ANNUAL_COMPOUND',
    'ANNUAL_COMPOUND',
    'CONTINUOUS_COMPOUND',
    'DAILY_AVERAGE_BALANCE',
    'MONTHLY_AVERAGE_BALANCE',
    'MINIMUM_BALANCE',
    'TIERED_RATE',
    'STEPPED_RATE',
    'DAY_COUNT_30_360',
    'DAY_COUNT_ACTUAL_360',
    'DAY_COUNT_ACTUAL_365',
    'DAY_COUNT_ACTUAL_ACTUAL'
);

-- Interest Payment Frequency Enum
CREATE TYPE interest_payment_frequency_enum AS ENUM (
    'DAILY',
    'WEEKLY',
    'BI_WEEKLY',
    'MONTHLY',
    'QUARTERLY',
    'SEMI_ANNUALLY',
    'ANNUALLY',
    'AT_MATURITY',
    'AT_CLOSURE',
    'SPECIFIC_DAY_OF_MONTH',
    'SPECIFIC_DATE',
    'NONE'
);

-- Regulatory Status Enum
CREATE TYPE regulatory_status_enum AS ENUM (
    'COMPLIANT',
    'PENDING_DOCUMENTATION',
    'ENHANCED_DUE_DILIGENCE',
    'UNDER_REVIEW',
    'SPECIAL_MONITORING',
    'SANCTIONS_SCREENING',
    'PEP_MONITORING'
);

-- Tax Reporting Status Enum
CREATE TYPE tax_reporting_status_enum AS ENUM (
    'REPORTABLE',
    'EXEMPT',
    'BACKUP_WITHHOLDING',
    'FATCA_REPORTABLE',
    'CRS_REPORTABLE',
    'FATCA_CRS_REPORTABLE',
    'PENDING_DOCUMENTATION',
    'INCOMPLETE_INFORMATION',
    'NON_REPORTABLE'
);
