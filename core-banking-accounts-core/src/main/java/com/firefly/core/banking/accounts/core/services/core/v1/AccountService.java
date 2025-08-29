package com.firefly.core.banking.accounts.core.services.core.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import reactor.core.publisher.Mono;

public interface AccountService {
    /**
     * Retrieves a paginated list of accounts based on the provided filter criteria.
     *
     * @param filterRequest the filter and pagination criteria for retrieving accounts
     * @return a reactive stream emitting a PaginationResponse containing a list of AccountDTO objects
     */
    Mono<PaginationResponse<AccountDTO>> filterAccounts(FilterRequest<AccountDTO> filterRequest);
    /**
     * Creates a new account with the provided details.
     *
     * @param accountDTO The account data transfer object containing the account details to be created.
     * @return A Mono emitting the created AccountDTO object, or an error if the creation fails.
     */
    Mono<AccountDTO> createAccount(AccountDTO accountDTO);
    /**
     * Retrieves the account details for a given account ID.
     *
     * @param accountId the unique identifier of the account to retrieve
     * @return a Mono emitting the AccountDTO containing account details, or an empty Mono if the account is not found
     */
    Mono<AccountDTO> getAccount(Long accountId);
    /**
     * Updates an existing account by its unique ID with the provided details.
     *
     * @param accountId the unique identifier of the account to be updated
     * @param accountDTO the updated account details
     * @return a Mono emitting the updated account details, or an error if the account does not exist
     */
    Mono<AccountDTO> updateAccount(Long accountId, AccountDTO accountDTO);
    /**
     * Deletes an account based on the provided account ID.
     *
     * @param accountId The unique identifier of the account to be deleted.
     * @return A Mono that completes when the account deletion process is finished.
     */
    Mono<Void> deleteAccount(Long accountId);
}
