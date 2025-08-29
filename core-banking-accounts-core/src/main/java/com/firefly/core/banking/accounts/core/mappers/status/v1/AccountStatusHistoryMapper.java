package com.firefly.core.banking.accounts.core.mappers.status.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import com.firefly.core.banking.accounts.models.entities.status.v1.AccountStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountStatusHistoryMapper {
    AccountStatusHistoryDTO toDTO (AccountStatusHistory accountStatusHistory);
    AccountStatusHistory toEntity (AccountStatusHistoryDTO accountStatusHistoryDTO);
}
