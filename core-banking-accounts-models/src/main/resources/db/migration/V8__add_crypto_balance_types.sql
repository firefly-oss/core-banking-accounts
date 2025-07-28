-- V8__add_crypto_balance_types.sql
-- Add crypto-specific balance types to balance_type_enum

-- Create a new enum type with all values (existing + new)
CREATE TYPE balance_type_enum_new AS ENUM (
    'CURRENT',
    'AVAILABLE',
    'BLOCKED',
    -- New crypto balance types
    'STAKED',
    'LOCKED',
    'PENDING_CONFIRMATION'
);

-- Add a temporary column with the new enum type
ALTER TABLE account_balance ADD COLUMN balance_type_new balance_type_enum_new;

-- Copy data from the old enum column to the new one
UPDATE account_balance SET balance_type_new = balance_type::text::balance_type_enum_new;

-- Drop the old column and rename the new one
ALTER TABLE account_balance DROP COLUMN balance_type;
ALTER TABLE account_balance RENAME COLUMN balance_type_new TO balance_type;

-- Drop the old enum type with CASCADE to remove dependent objects (like casts)
DROP TYPE balance_type_enum CASCADE;

-- Rename the new enum type to the original name
ALTER TYPE balance_type_enum_new RENAME TO balance_type_enum;

-- Recreate the cast from varchar to balance_type_enum that was dropped by CASCADE
CREATE CAST (varchar AS balance_type_enum) WITH INOUT AS IMPLICIT;