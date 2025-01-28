package com.catalis.core.banking.accounts.interfaces.dtos.core.v1;

import com.catalis.common.core.filters.FilterableId;
import com.catalis.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.AccountStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountId;

    @FilterableId
    private Long contractId;

    private String accountNumber;
    private String accountType;
    private String currency;
    private LocalDate openDate;
    private LocalDate closeDate;
    private AccountStatusEnum accountStatus;

    @FilterableId
    private Long branchId;

    private String description;
}