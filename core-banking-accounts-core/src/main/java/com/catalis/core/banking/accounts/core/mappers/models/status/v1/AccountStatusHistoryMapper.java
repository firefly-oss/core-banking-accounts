package com.catalis.core.banking.accounts.core.mappers.models.status.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.models.entities.status.v1.AccountStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountStatusHistoryMapper {
    AccountStatusHistoryDTO toDTO (AccountStatusHistory accountStatusHistory);
    AccountStatusHistory toEntity (AccountStatusHistoryDTO accountStatusHistoryDTO);
}
