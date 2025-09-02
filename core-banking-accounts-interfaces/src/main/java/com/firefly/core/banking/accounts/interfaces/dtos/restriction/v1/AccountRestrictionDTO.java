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
public class AccountRestrictionDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountRestrictionId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotNull(message = "Restriction type is required")
    private RestrictionTypeEnum restrictionType;

    @NotNull(message = "Start datetime is required")
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @DecimalMin(value = "0.0", inclusive = true, message = "Restricted amount cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Restricted amount must have at most 15 integer digits and 2 decimal places")
    private BigDecimal restrictedAmount;

    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    private String referenceNumber;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    @NotBlank(message = "Applied by is required")
    @Size(max = 100, message = "Applied by must not exceed 100 characters")
    private String appliedBy;

    @Size(max = 100, message = "Removed by must not exceed 100 characters")
    private String removedBy;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private Boolean isActive;
}
