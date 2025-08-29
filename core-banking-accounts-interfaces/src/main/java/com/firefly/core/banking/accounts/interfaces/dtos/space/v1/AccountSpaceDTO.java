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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountSpaceDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountSpaceId;

    @FilterableId
    private Long accountId;

    private String spaceName;
    private AccountSpaceTypeEnum spaceType;
    private BigDecimal balance;
    private BigDecimal targetAmount;
    private LocalDateTime targetDate;
    private String iconId;
    private String colorCode;
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
    private Long sourceSpaceId; // For automatic transfers from another space

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
