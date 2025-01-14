-- V1__Create_enums.sql
-- Creates custom ENUM types for the Account Management module

-- 1) Account status
CREATE TYPE account_status_type AS ENUM (
    'OPEN',
    'CLOSED',
    'SUSPENDED',
    'DORMANT'
);

-- 2) Account balance type
CREATE TYPE balance_type AS ENUM (
    'CURRENT',
    'AVAILABLE',
    'BLOCKED'
);

-- 3) Status code for account status history
CREATE TYPE status_code_type AS ENUM (
    'OPEN',
    'SUSPENDED',
    'CLOSED',
    'DORMANT'
);

-- 4) Parameter type
CREATE TYPE param_type AS ENUM (
    'MONTHLY_FEE',
    'OVERDRAFT_LIMIT',
    'INTEREST_RATE'
);

-- 5) Provider status
CREATE TYPE provider_status_type AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'PENDING',
    'SUSPENDED'
);