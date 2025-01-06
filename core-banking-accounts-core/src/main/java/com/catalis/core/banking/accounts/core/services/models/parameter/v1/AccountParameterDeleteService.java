package com.catalis.core.banking.accounts.core.services.models.parameter.v1;

import com.catalis.core.banking.accounts.models.repositories.parameter.v1.AccountParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountParameterDeleteService {

    @Autowired
    private AccountParameterRepository repository;

    /**
     * Deletes an account parameter identified by the provided ID.
     * If the account parameter does not exist in the repository, an {@code IllegalArgumentException} is thrown.
     *
     * @param accountParameterId the ID of the account parameter to be deleted
     * @return a {@code Mono<Void>} indicating the completion of the operation
     */
    public Mono<Void> deleteAccountParameter(Long accountParameterId) {
        return repository.findById(accountParameterId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account parameter not found with ID: " + accountParameterId)))
                .flatMap(existingParameter -> repository.delete(existingParameter));
    }
    
}
