package com.firefly.core.banking.accounts.core.services.provider.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.accounts.core.mappers.provider.v1.AccountProviderMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.firefly.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.firefly.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
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

    @Override
    public Mono<PaginationResponse<AccountProviderDTO>> listProvidersForSpace(Long accountId, Long accountSpaceId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndAccountSpaceId(accountId, accountSpaceId, pageable),
                () -> repository.countByAccountIdAndAccountSpaceId(accountId, accountSpaceId)
        );
    }

    @Override
    public Mono<AccountProviderDTO> createProviderForSpace(Long accountId, Long accountSpaceId, AccountProviderDTO providerDTO) {
        AccountProvider accountProvider = mapper.toEntity(providerDTO);
        accountProvider.setAccountId(accountId);
        accountProvider.setAccountSpaceId(accountSpaceId);
        return repository.save(accountProvider)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountProviderDTO> getProviderForSpace(Long accountId, Long accountSpaceId, Long providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId) && 
                        (provider.getAccountSpaceId() != null && provider.getAccountSpaceId().equals(accountSpaceId)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountProviderDTO> updateProviderForSpace(Long accountId, Long accountSpaceId, Long providerId, AccountProviderDTO providerDTO) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId) && 
                        (provider.getAccountSpaceId() != null && provider.getAccountSpaceId().equals(accountSpaceId)))
                .flatMap(existingProvider -> {
                    AccountProvider updatedProvider = mapper.toEntity(providerDTO);
                    updatedProvider.setAccountProviderId(existingProvider.getAccountProviderId());
                    updatedProvider.setAccountId(existingProvider.getAccountId());
                    updatedProvider.setAccountSpaceId(existingProvider.getAccountSpaceId());
                    return repository.save(updatedProvider);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteProviderForSpace(Long accountId, Long accountSpaceId, Long providerId) {
        return repository.findById(providerId)
                .filter(provider -> provider.getAccountId().equals(accountId) && 
                        (provider.getAccountSpaceId() != null && provider.getAccountSpaceId().equals(accountSpaceId)))
                .flatMap(repository::delete);
    }
}
