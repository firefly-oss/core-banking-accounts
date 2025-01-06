package com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.models.core.v1.BalanceTypeEnum;
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

    private Long accountId;
    private BalanceTypeEnum balanceType;
    private BigDecimal balanceAmount;
    private LocalDateTime asOfDatetime;

}
