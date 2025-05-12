package com.catalis.core.banking.accounts.interfaces.dtos.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.AccountStatusEnum;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.AccountSubTypeEnum;
import com.catalis.core.banking.accounts.interfaces.enums.interest.v1.InterestAccrualMethodEnum;
import com.catalis.core.banking.accounts.interfaces.enums.interest.v1.InterestPaymentFrequencyEnum;
import com.catalis.core.banking.accounts.interfaces.enums.regulatory.v1.RegulatoryStatusEnum;
import com.catalis.core.banking.accounts.interfaces.enums.regulatory.v1.TaxReportingStatusEnum;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
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

    private AccountSubTypeEnum accountSubType;
    private TaxReportingStatusEnum taxReportingStatus;
    private RegulatoryStatusEnum regulatoryStatus;
    private LocalDate maturityDate;
    private InterestAccrualMethodEnum interestAccrualMethod;
    private InterestPaymentFrequencyEnum interestPaymentFrequency;
    private BigDecimal minimumBalance;
    private BigDecimal overdraftLimit;
}