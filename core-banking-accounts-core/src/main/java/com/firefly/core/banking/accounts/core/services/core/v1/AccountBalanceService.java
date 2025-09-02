package com.firefly.core.banking.accounts.core.services.core.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountBalanceService {

    /**
     * Retrieve a paginated list of account balances for a specified account.
     * This returns all balances for the account, including both global and space-specific balances.
     */
    Mono<PaginationResponse<AccountBalanceDTO>> getAllBalances(UUID accountId, PaginationRequest paginationRequest);

    /**
     * Retrieve a paginated list of global account balances (not associated with any space).
     */
    Mono<PaginationResponse<AccountBalanceDTO>> getGlobalBalances(UUID accountId, PaginationRequest paginationRequest);

    /**
     * Retrieve a paginated list of balances for a specific account space.
     */
    Mono<PaginationResponse<AccountBalanceDTO>> getSpaceBalances(UUID accountId, UUID accountSpaceId, PaginationRequest paginationRequest);

    /**
     * Create a new balance record for a specific account.
     * If accountSpaceId is set in the DTO, this will create a space-specific balance.
     * If accountSpaceId is null, this will create a global account balance.
     */
    Mono<AccountBalanceDTO> createBalance(UUID accountId, AccountBalanceDTO balanceDTO);

    /**
     * Retrieve a specific account balance by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<AccountBalanceDTO> getBalance(UUID accountId, UUID balanceId);

    /**
     * Update an existing account balance for a specified account.
     */
    Mono<AccountBalanceDTO> updateBalance(UUID accountId, UUID balanceId, AccountBalanceDTO balanceDTO);

    /**
     * Delete a specific account balance by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteBalance(UUID accountId, UUID balanceId);
}
