package com.catalis.core.banking.accounts.core.mappers.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.models.entities.core.v1.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO (Account account);
    Account toEntity (AccountDTO accountDTO);
}
