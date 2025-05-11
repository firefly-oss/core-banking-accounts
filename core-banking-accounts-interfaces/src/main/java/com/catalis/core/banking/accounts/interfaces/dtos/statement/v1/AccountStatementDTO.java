package com.catalis.core.banking.accounts.interfaces.dtos.statement.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.statement.v1.StatementDeliveryMethodEnum;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountStatementDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountStatementId;

    @FilterableId
    private Long accountId;

    private String statementNumber;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalFees;
    private BigDecimal totalInterest;
    private LocalDateTime generationDateTime;
    private StatementDeliveryMethodEnum deliveryMethod;
    private LocalDateTime deliveryDateTime;
    private String documentUrl;
    private Boolean isViewed;
    private LocalDateTime firstViewedDateTime;
    private String metadata;
}
