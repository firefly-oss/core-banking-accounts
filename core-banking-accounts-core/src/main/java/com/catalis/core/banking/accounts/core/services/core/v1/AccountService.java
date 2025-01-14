package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import reactor.core.publisher.Mono;

public interface AccountService {

    /**
     * Create a new account record.
     */
    Mono<AccountDTO> createAccount(AccountDTO accountDTO);

    /**
     * Retrieve a specific account by its unique ID.
     */
    Mono<AccountDTO> getAccount(Long accountId);

    /**
     * Update an existing account record by its unique ID.
     */
    Mono<AccountDTO> updateAccount(Long accountId, AccountDTO accountDTO);

    /**
     * Delete an existing account record by its unique ID.
     */
    Mono<Void> deleteAccount(Long accountId);
}
