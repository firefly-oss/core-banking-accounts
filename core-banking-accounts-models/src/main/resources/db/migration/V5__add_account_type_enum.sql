-- V5__add_account_type_enum.sql
-- Add account_type_enum and update account table to use it

-- Create account_type_enum
CREATE TYPE account_type_enum AS ENUM (
    'CHECKING',
    'SAVINGS',
    'TERM_DEPOSIT',
    'LOAN',
    'CREDIT_CARD',
    'INVESTMENT',
    'MORTGAGE',
    'BUSINESS_CHECKING',
    'BUSINESS_SAVINGS',
    'FOREIGN_CURRENCY',
    'PREMIUM',
    'YOUTH',
    'SENIOR',
    'PENSION',
    'GOVERNMENT_BENEFITS'
);

-- Add a temporary column for the transition
ALTER TABLE account ADD COLUMN account_type_enum account_type_enum;

-- Update the temporary column with values from the existing account_type string column
UPDATE account SET account_type_enum = account_type::account_type_enum;

-- Drop the old column and rename the new one
ALTER TABLE account DROP COLUMN account_type;
ALTER TABLE account RENAME COLUMN account_type_enum TO account_type;
