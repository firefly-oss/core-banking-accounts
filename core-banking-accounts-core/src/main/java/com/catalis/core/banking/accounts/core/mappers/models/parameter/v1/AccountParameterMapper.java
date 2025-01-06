package com.catalis.core.banking.accounts.core.mappers.models.parameter.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountParameterMapper {
    AccountParameterDTO toDTO (AccountParameter accountParameter);
    AccountParameter toEntity (AccountParameterDTO accountParameterDTO);
}
