package com.firefly.core.banking.accounts.core.mappers.space.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.firefly.core.banking.accounts.models.entities.space.v1.AccountSpace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountSpaceMapper {
    AccountSpaceDTO toDTO(AccountSpace accountSpace);
    AccountSpace toEntity(AccountSpaceDTO accountSpaceDTO);
}