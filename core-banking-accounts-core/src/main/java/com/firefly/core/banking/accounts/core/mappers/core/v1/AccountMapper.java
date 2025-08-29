package com.firefly.core.banking.accounts.core.mappers.core.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.firefly.core.banking.accounts.models.entities.core.v1.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO (Account account);
    Account toEntity (AccountDTO accountDTO);

    @Mapping(target = "dateCreated", ignore = true)
    void updateEntityFromDto(AccountDTO dto, @MappingTarget Account entity);

}
