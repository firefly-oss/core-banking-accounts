package com.catalis.core.banking.accounts.core.mappers.models.statement.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import com.catalis.core.banking.accounts.models.entities.statement.v1.AccountStatement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountStatementMapper {
    AccountStatementDTO toDTO(AccountStatement accountStatement);
    AccountStatement toEntity(AccountStatementDTO accountStatementDTO);
}
