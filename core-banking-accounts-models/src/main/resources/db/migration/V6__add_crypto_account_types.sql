-- V6__add_crypto_account_types.sql
-- Add crypto-specific account types to account_type_enum

-- Create a new enum type with all values (existing + new)
CREATE TYPE account_type_enum_new AS ENUM (
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
    'GOVERNMENT_BENEFITS',
    -- New crypto types
    'CRYPTO_WALLET',
    'TOKENIZED_ASSET',
    'DIGITAL_ASSET',
    'STABLECOIN',
    'NFT_COLLECTION'
);

-- Add a temporary column with the new enum type
ALTER TABLE account ADD COLUMN account_type_new account_type_enum_new;

-- Copy data from the old enum column to the new one
UPDATE account SET account_type_new = account_type::text::account_type_enum_new;

-- Drop the old column and rename the new one
ALTER TABLE account DROP COLUMN account_type;
ALTER TABLE account RENAME COLUMN account_type_new TO account_type;

-- Drop the old enum type
DROP TYPE account_type_enum;

-- Rename the new enum type to the original name
ALTER TYPE account_type_enum_new RENAME TO account_type_enum;