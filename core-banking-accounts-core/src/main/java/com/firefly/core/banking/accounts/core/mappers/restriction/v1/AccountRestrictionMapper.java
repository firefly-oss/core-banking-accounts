package com.firefly.core.banking.accounts.core.mappers.restriction.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.restriction.v1.AccountRestrictionDTO;
import com.firefly.core.banking.accounts.models.entities.restriction.v1.AccountRestriction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountRestrictionMapper {
    AccountRestrictionDTO toDTO(AccountRestriction accountRestriction);
    AccountRestriction toEntity(AccountRestrictionDTO accountRestrictionDTO);
}
