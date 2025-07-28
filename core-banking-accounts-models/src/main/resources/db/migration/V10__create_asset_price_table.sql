-- V10__create_asset_price_table.sql
-- Create asset_price table for tracking crypto asset prices

-- Create asset_price table
CREATE TABLE asset_price (
    -- Primary key and identifiers
    asset_price_id BIGSERIAL PRIMARY KEY,
    
    -- Asset information
    asset_symbol VARCHAR(20) NOT NULL,
    quote_currency VARCHAR(10) NOT NULL,
    price NUMERIC(36, 18) NOT NULL,
    
    -- Timestamp and source
    as_of_datetime TIMESTAMP NOT NULL,
    price_source VARCHAR(50) NOT NULL,
    
    -- Audit fields (inherited from BaseEntity)
    created_by VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP
);

-- Add indexes for efficient querying
CREATE INDEX idx_asset_price_symbol ON asset_price(asset_symbol);
CREATE INDEX idx_asset_price_quote_currency ON asset_price(quote_currency);
CREATE INDEX idx_asset_price_datetime ON asset_price(as_of_datetime);

-- Create a composite index for looking up the latest price for a specific asset/currency pair
CREATE INDEX idx_asset_price_lookup ON asset_price(asset_symbol, quote_currency, as_of_datetime);

-- Add comments to explain the purpose of the table and columns
COMMENT ON TABLE asset_price IS 'Stores historical price data for crypto assets and tokenized assets';
COMMENT ON COLUMN asset_price.asset_price_id IS 'Unique identifier for the asset price record';
COMMENT ON COLUMN asset_price.asset_symbol IS 'Symbol or ticker of the crypto asset (BTC, ETH, etc.)';
COMMENT ON COLUMN asset_price.quote_currency IS 'Currency in which the asset is priced (USD, EUR, etc.)';
COMMENT ON COLUMN asset_price.price IS 'The price of the asset in the quote currency with high precision';
COMMENT ON COLUMN asset_price.as_of_datetime IS 'Timestamp when this price was recorded';
COMMENT ON COLUMN asset_price.price_source IS 'Source of the price data (exchange, API, etc.)';