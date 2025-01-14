package com.catalis.core.banking.accounts.core.services.provider.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.provider.v1.AccountProviderMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountProviderServiceImpl implements AccountProviderService {

    @Autowired
    private AccountProviderRepository repository;

    @Autowired
    private AccountProviderMapper mapper;

    @Override
    public Mono<PaginationResponse<AccountProviderDTO>> listProviders(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<AccountProviderDTO> createProvider(Long accountId, AccountProviderDTO providerDTO) {
        AccountProvider accountProvider = mapper.toEntity(providerDTO);
        accountProvider.setAccountId(accountId);
        return repository.save(accountProvider)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountProviderDTO> getProvider(Long accountId, Long providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountProviderDTO> updateProvider(Long accountId, Long providerId, AccountProviderDTO providerDTO) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId))
                .flatMap(existingProvider -> {
                    AccountProvider updatedProvider = mapper.toEntity(providerDTO);
                    updatedProvider.setAccountProviderId(existingProvider.getAccountProviderId());
                    updatedProvider.setAccountId(existingProvider.getAccountId());
                    return repository.save(updatedProvider);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteProvider(Long accountId, Long providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId))
                .flatMap(repository::delete);
    }
}