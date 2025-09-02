package com.firefly.core.banking.accounts.interfaces.dtos.crypto.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for asset price information.
 * Used for transferring crypto asset price data between layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AssetPriceDTO extends BaseDTO {
    
    /**
     * Unique identifier for the asset price record
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID assetPriceId;
    
    /**
     * Symbol or ticker of the crypto asset
     * Examples: "BTC", "ETH", "USDC", "SOL"
     */
    private String assetSymbol;
    
    /**
     * Currency in which the asset is priced
     * Examples: "USD", "EUR", "BTC"
     */
    private String quoteCurrency;
    
    /**
     * The price of the asset in the quote currency
     */
    private BigDecimal price;
    
    /**
     * Timestamp when this price was recorded
     */
    private LocalDateTime asOfDatetime;
    
    /**
     * Source of the price data
     * Examples: "Coinbase", "Binance", "CoinMarketCap"
     */
    private String priceSource;
}