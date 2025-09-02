package com.firefly.core.banking.accounts.interfaces.dtos.space.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.space.v1.TransferFrequencyEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountSpaceDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountSpaceId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Space name is required")
    @Size(max = 100, message = "Space name must not exceed 100 characters")
    private String spaceName;

    @NotNull(message = "Space type is required")
    private AccountSpaceTypeEnum spaceType;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Balance must have at most 15 integer digits and 2 decimal places")
    private BigDecimal balance;

    @DecimalMin(value = "0.0", inclusive = false, message = "Target amount must be positive")
    @Digits(integer = 15, fraction = 2, message = "Target amount must have at most 15 integer digits and 2 decimal places")
    private BigDecimal targetAmount;

    @Future(message = "Target date must be in the future")
    private LocalDateTime targetDate;

    @Size(max = 50, message = "Icon ID must not exceed 50 characters")
    private String iconId;

    @Size(max = 7, message = "Color code must not exceed 7 characters")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code must be a valid hex color (e.g., #FF0000)")
    private String colorCode;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean isVisible;

    // Goal tracking fields - calculated, not stored in database
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal goalProgressPercentage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal remainingToTarget;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime estimatedCompletionDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isGoalCompleted;

    // Automatic transfer fields
    private Boolean enableAutomaticTransfers;
    private TransferFrequencyEnum transferFrequency;
    private BigDecimal transferAmount;
    private UUID sourceSpaceId; // For automatic transfers from another space

    // Analytics fields - calculated, not stored in database
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal averageGrowthRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal contributionToAccountTotal;

    // Status management fields
    private Boolean isFrozen;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime frozenDateTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime unfrozenDateTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastBalanceUpdateReason;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastBalanceUpdateDateTime;
}
