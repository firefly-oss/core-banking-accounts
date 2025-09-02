package com.firefly.core.banking.accounts.interfaces.dtos.crypto.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

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
    @NotBlank(message = "Asset symbol is required")
    @Size(max = 10, message = "Asset symbol must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Asset symbol must contain only uppercase letters and numbers")
    private String assetSymbol;

    /**
     * Currency in which the asset is priced
     * Examples: "USD", "EUR", "BTC"
     */
    @NotBlank(message = "Quote currency is required")
    @Size(min = 3, max = 3, message = "Quote currency must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Quote currency must be a valid 3-letter code")
    private String quoteCurrency;

    /**
     * The price of the asset in the quote currency
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Digits(integer = 15, fraction = 8, message = "Price must have at most 15 integer digits and 8 decimal places")
    private BigDecimal price;

    /**
     * Timestamp when this price was recorded
     */
    @NotNull(message = "As of datetime is required")
    @PastOrPresent(message = "As of datetime cannot be in the future")
    private LocalDateTime asOfDatetime;

    /**
     * Source of the price data
     * Examples: "Coinbase", "Binance", "CoinMarketCap"
     */
    @NotBlank(message = "Price source is required")
    @Size(max = 50, message = "Price source must not exceed 50 characters")
    private String priceSource;
}