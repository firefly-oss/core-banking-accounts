package com.catalis.core.banking.accounts.core.services.requests.account.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountProviderCreateAccountService {

    @Autowired
    private AccountProvider accountProvider;

    /**
     * Creates a new account by interacting with the account provider.
     *
     * @param account the {@link AccountDTO} object containing the details of the account to be created
     * @return a {@link Mono} emitting the created {@link AccountDTO} object, or an error if the creation process fails
     */
    public Mono<AccountDTO> createAccount(AccountDTO account) {
        return accountProvider.createAccount(account)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create account", e)));
    }

}