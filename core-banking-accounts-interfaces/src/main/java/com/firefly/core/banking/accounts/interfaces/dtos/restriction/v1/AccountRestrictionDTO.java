package com.firefly.core.banking.accounts.interfaces.dtos.restriction.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.restriction.v1.RestrictionTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountRestrictionDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountRestrictionId;

    @FilterableId
    private UUID accountId;

    private RestrictionTypeEnum restrictionType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private BigDecimal restrictedAmount;
    private String referenceNumber;
    private String reason;
    private String appliedBy;
    private String removedBy;
    private String notes;
    private Boolean isActive;
}
