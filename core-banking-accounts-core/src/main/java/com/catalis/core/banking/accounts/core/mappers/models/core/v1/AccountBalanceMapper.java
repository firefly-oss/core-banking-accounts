package com.catalis.core.banking.accounts.core.mappers.models.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.models.entities.core.v1.AccountBalance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBalanceMapper {
    AccountBalanceDTO toDTO (AccountBalance accountBalance);
    AccountBalance toEntity (AccountBalanceDTO accountBalanceDTO);
}
