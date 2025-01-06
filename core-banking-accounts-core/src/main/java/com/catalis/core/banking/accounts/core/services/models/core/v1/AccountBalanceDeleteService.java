package com.catalis.core.banking.accounts.core.services.models.core.v1;

import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountBalanceDeleteService {

    @Autowired
    private AccountBalanceRepository repository;

    /**
     * Deletes an account balance record from the repository based on the provided ID.
     * If an error occurs during the deletion, a RuntimeException is propagated.
     *
     * @param accountBalanceId the unique identifier of the account balance to be deleted
     * @return a Mono that completes when the account balance is successfully deleted,
     * or propagates an error if the deletion fails
     */
    public Mono<Void> deleteAccountBalance(Long accountBalanceId) {
        return repository.deleteById(accountBalanceId)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to delete account balance with ID: " + accountBalanceId, e)))
                .then();
    }

}
