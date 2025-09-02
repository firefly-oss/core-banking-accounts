package com.firefly.core.banking.accounts.interfaces.dtos.core.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountBalanceDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountBalanceId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @FilterableId
    private UUID accountSpaceId;

    @NotNull(message = "Balance type is required")
    private BalanceTypeEnum balanceType;

    @NotNull(message = "Balance amount is required")
    @Digits(integer = 15, fraction = 2, message = "Balance amount must have at most 15 integer digits and 2 decimal places")
    private BigDecimal balanceAmount;

    @NotNull(message = "As of datetime is required")
    @PastOrPresent(message = "As of datetime cannot be in the future")
    private LocalDateTime asOfDatetime;

    // Crypto-specific fields
    @Size(max = 10, message = "Asset symbol must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "Asset symbol must contain only uppercase letters and numbers")
    private String assetSymbol;

    @Size(max = 2, message = "Asset decimals must not exceed 2 characters")
    @Pattern(regexp = "^[0-9]*$", message = "Asset decimals must contain only numbers")
    private String assetDecimals;

    @Size(max = 100, message = "Transaction hash must not exceed 100 characters")
    private String transactionHash;

    @Min(value = 0, message = "Confirmations cannot be negative")
    @Max(value = 999999, message = "Confirmations cannot exceed 999999")
    private Integer confirmations;
}