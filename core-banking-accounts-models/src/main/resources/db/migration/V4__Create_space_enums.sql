-- V4__Create_space_enums.sql
-- Creates custom ENUM types for the Account Spaces functionality

-- 1) Account space type
CREATE TYPE account_space_type AS ENUM (
    'MAIN',
    'SAVINGS',
    'VACATION',
    'EMERGENCY',
    'GOALS',
    'CUSTOM'
);