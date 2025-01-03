package com.catalis.core.banking.accounts.core.services.status.v1;

import com.catalis.core.banking.accounts.models.repositories.status.v1.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountStatusHistoryDeleteService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    /**
     * Deletes an account status history record identified by its ID.
     * If the record with the specified ID is not found, an error is emitted.
     *
     * @param accountStatusHistoryId the unique identifier of the account status history record to delete
     * @return a {@link Mono} that completes when the deletion is successful, or emits an error if the record is not found
     */
    public Mono<Void> deleteAccountStatusHistory(Long accountStatusHistoryId) {
        return repository.findById(accountStatusHistoryId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("AccountStatusHistory not found with id: " + accountStatusHistoryId)))
                .flatMap(repository::delete);
    }
    
}
