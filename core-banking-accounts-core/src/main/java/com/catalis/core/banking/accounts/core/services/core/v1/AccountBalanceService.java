package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import reactor.core.publisher.Mono;

public interface AccountBalanceService {

    /**
     * Retrieve a paginated list of account balances for a specified account.
     */
    Mono<PaginationResponse<AccountBalanceDTO>> getAllBalances(Long accountId, PaginationRequest paginationRequest);

    /**
     * Create a new balance record for a specific account.
     */
    Mono<AccountBalanceDTO> createBalance(Long accountId, AccountBalanceDTO balanceDTO);

    /**
     * Retrieve a specific account balance by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<AccountBalanceDTO> getBalance(Long accountId, Long balanceId);

    /**
     * Update an existing account balance for a specified account.
     */
    Mono<AccountBalanceDTO> updateBalance(Long accountId, Long balanceId, AccountBalanceDTO balanceDTO);

    /**
     * Delete a specific account balance by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteBalance(Long accountId, Long balanceId);
}