package com.catalis.core.banking.accounts.core.services.provider.v1;

import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountProviderDeleteService {

    @Autowired
    private AccountProviderRepository repository;

    /**
     * Deletes an account provider identified by its unique ID.
     * This method retrieves the account provider entity by its ID, and if found, deletes the entity.
     * Emits an error if the specified account provider ID does not exist.
     *
     * @param accountProviderId the unique identifier of the account provider to delete
     * @return a {@link Mono} emitting {@code Void} upon successful deletion,
     *         or an error if the account provider ID is not found
     */
    public Mono<Void> deleteAccountProvider(Long accountProviderId) {
        return repository.findById(accountProviderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account provider not found for id: " + accountProviderId)))
                .flatMap(existingProvider -> repository.delete(existingProvider))
                .then();
    }

}
