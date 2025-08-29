package com.firefly.core.banking.accounts.interfaces.dtos.space.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for account space transactions.
 * Represents a transaction that affects the balance of an account space.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceTransactionDTO extends BaseDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long spaceTransactionId;
    
    @FilterableId
    private Long accountSpaceId;
    
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private LocalDateTime transactionDateTime;
    private String description;
    private String referenceId;
    private String transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST, FEE
    
    // Calculated fields
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String spaceName; // Added for convenience in UI display
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountId; // Added for convenience in UI display
}
