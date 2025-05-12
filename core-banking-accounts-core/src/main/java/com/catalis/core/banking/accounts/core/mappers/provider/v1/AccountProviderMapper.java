package com.catalis.core.banking.accounts.core.mappers.provider.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountProviderMapper {
    AccountProviderDTO toDTO (AccountProvider accountProvider);
    AccountProvider toEntity (AccountProviderDTO accountProviderDTO);
}
