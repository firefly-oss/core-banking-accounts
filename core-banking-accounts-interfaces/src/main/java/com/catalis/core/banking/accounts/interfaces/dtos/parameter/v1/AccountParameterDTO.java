package com.catalis.core.banking.accounts.interfaces.dtos.parameter.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.accounts.interfaces.enums.parameter.v1.ParamTypeEnum;
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
public class AccountParameterDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountParameterId;

    private Long accountId;
    private ParamTypeEnum paramType;
    private BigDecimal paramValue;
    private String paramUnit;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private String description;
}
