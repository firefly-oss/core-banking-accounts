package com.catalis.core.banking.accounts.core.services.models.provider.v1;

import com.catalis.core.banking.accounts.core.mappers.models.provider.v1.AccountProviderMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.models.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountProviderUpdateService {

    @Autowired
    private AccountProviderRepository repository;

    @Autowired
    private AccountProviderMapper mapper;

    /**
     * Updates an existing account provider with the details provided in the {@code accountProvider} object.
     * The method first retrieves the account provider by its ID, then updates its fields
     * with the corresponding values from {@code accountProvider}, saves the updated entity,
     * and maps it to a DTO.
     *
     * @param accountProviderId the unique identifier of the account provider to be updated
     * @param accountProvider the {@link AccountProviderDTO} object containing the new details for the account provider
     * @return a {@link Mono} emitting the updated {@link AccountProviderDTO}
     */
    public Mono<AccountProviderDTO> updateAccountProvider(Long accountProviderId, AccountProviderDTO accountProvider) {
        return repository.findById(accountProviderId)
                .flatMap(existingProvider -> {
                    existingProvider.setAccountId(accountProvider.getAccountId());
                    existingProvider.setProviderName(accountProvider.getProviderName());
                    existingProvider.setExternalReference(accountProvider.getExternalReference());
                    existingProvider.setStatus(accountProvider.getStatus());
                    return repository.save(existingProvider);
                })
                .map(mapper::toDTO);
    }
    
}
