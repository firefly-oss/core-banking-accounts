package com.firefly.core.banking.accounts.interfaces.dtos.status.v1;


import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.status.v1.StatusCodeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountStatusHistoryDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountStatusHistoryId;

    @FilterableId
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotNull(message = "Status code is required")
    private StatusCodeEnum statusCode;

    @NotNull(message = "Status start datetime is required")
    private LocalDateTime statusStartDatetime;

    private LocalDateTime statusEndDatetime;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}