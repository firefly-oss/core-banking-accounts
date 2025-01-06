package com.catalis.core.banking.accounts.core.services.models.provider.v1;

import com.catalis.core.banking.accounts.core.mappers.models.provider.v1.AccountProviderMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.models.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountProviderCreateService {
    
    @Autowired
    private AccountProviderRepository repository;
    
    @Autowired
    private AccountProviderMapper mapper;

    /**
     * Creates a new account provider entry in the system.
     * This method maps the provided {@link AccountProviderDTO} to an {@link AccountProvider} entity,
     * persists it using the repository, and maps the saved entity back to a {@link AccountProviderDTO}.
     *
     * @param accountProvider the {@link AccountProviderDTO} object containing the details
     *                        of the account provider to be created
     * @return a {@link Mono} emitting the created {@link AccountProviderDTO} with the generated ID
     */
    public Mono<AccountProviderDTO> createAccountProvider(AccountProviderDTO accountProvider) {
        return Mono.just(accountProvider)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(savedEntity -> {
                    accountProvider.setAccountProviderId(savedEntity.getAccountProviderId());
                    return mapper.toDTO(savedEntity);
                });
    }
    
}