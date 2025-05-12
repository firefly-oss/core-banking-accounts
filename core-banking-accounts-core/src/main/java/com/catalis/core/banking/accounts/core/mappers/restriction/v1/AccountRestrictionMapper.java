package com.catalis.core.banking.accounts.core.mappers.models.restriction.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.restriction.v1.AccountRestrictionDTO;
import com.catalis.core.banking.accounts.models.entities.restriction.v1.AccountRestriction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountRestrictionMapper {
    AccountRestrictionDTO toDTO(AccountRestriction accountRestriction);
    AccountRestriction toEntity(AccountRestrictionDTO accountRestrictionDTO);
}
