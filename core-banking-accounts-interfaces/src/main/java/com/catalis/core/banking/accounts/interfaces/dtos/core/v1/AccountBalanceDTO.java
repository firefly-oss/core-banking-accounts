package com.catalis.core.banking.accounts.interfaces.dtos.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.catalis.core.utils.annotations.FilterableId;
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
public class AccountBalanceDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountBalanceId;

    @FilterableId
    private Long accountId;

    @FilterableId
    private Long accountSpaceId;

    private BalanceTypeEnum balanceType;
    private BigDecimal balanceAmount;
    private LocalDateTime asOfDatetime;

}