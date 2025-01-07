package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountDeleteService {

    @Autowired
    private AccountRepository repository;

    /**
     * Deletes an account by its accountId. If the account with the specified ID does
     * not exist, an error is emitted.
     *
     * @param accountId the ID of the account to be deleted
     * @return a Mono indicating completion of the account deletion process
     */
    public Mono<Void> deleteAccount(Long accountId) {
        return repository.findById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("Account with ID " + accountId + " not found")))
                .flatMap(account -> repository.deleteById(accountId))
                .then();
    }

}
