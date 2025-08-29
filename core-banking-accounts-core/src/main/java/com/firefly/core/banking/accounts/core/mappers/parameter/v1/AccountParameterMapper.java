package com.firefly.core.banking.accounts.core.mappers.parameter.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import com.firefly.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountParameterMapper {
    AccountParameterDTO toDTO (AccountParameter accountParameter);
    AccountParameter toEntity (AccountParameterDTO accountParameterDTO);
}
