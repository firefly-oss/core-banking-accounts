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

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountStatusHistoryDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountStatusHistoryId;

    @FilterableId
    private Long accountId;

    private StatusCodeEnum statusCode;
    private LocalDateTime statusStartDatetime;
    private LocalDateTime statusEndDatetime;
    private String reason;
}