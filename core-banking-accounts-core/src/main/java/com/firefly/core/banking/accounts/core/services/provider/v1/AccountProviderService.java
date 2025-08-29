package com.firefly.core.banking.accounts.core.services.provider.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import reactor.core.publisher.Mono;

public interface AccountProviderService {

    /**
     * Retrieve a paginated list of providers for a specific account.
     */
    Mono<PaginationResponse<AccountProviderDTO>> listProviders(Long accountId, PaginationRequest paginationRequest);

    /**
     * Create a new provider record for a specific account.
     */
    Mono<AccountProviderDTO> createProvider(Long accountId, AccountProviderDTO providerDTO);

    /**
     * Retrieve a specific provider by its ID, ensuring it belongs to the specified account.
     */
    Mono<AccountProviderDTO> getProvider(Long accountId, Long providerId);

    /**
     * Update an existing provider associated with a specific account.
     */
    Mono<AccountProviderDTO> updateProvider(Long accountId, Long providerId, AccountProviderDTO providerDTO);

    /**
     * Delete a specific account provider by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteProvider(Long accountId, Long providerId);

    /**
     * Retrieve a paginated list of providers for a specific account space.
     */
    Mono<PaginationResponse<AccountProviderDTO>> listProvidersForSpace(Long accountId, Long accountSpaceId, PaginationRequest paginationRequest);

    /**
     * Create a new provider record for a specific account space.
     */
    Mono<AccountProviderDTO> createProviderForSpace(Long accountId, Long accountSpaceId, AccountProviderDTO providerDTO);

    /**
     * Retrieve a specific provider by its ID, ensuring it belongs to the specified account space.
     */
    Mono<AccountProviderDTO> getProviderForSpace(Long accountId, Long accountSpaceId, Long providerId);

    /**
     * Update an existing provider associated with a specific account space.
     */
    Mono<AccountProviderDTO> updateProviderForSpace(Long accountId, Long accountSpaceId, Long providerId, AccountProviderDTO providerDTO);

    /**
     * Delete a specific account provider by its unique ID, ensuring it belongs to the specified account space.
     */
    Mono<Void> deleteProviderForSpace(Long accountId, Long accountSpaceId, Long providerId);
}
