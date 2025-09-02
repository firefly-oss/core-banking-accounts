package com.firefly.core.banking.accounts.core.services.provider.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountProviderService {

    /**
     * Retrieve a paginated list of providers for a specific account.
     */
    Mono<PaginationResponse<AccountProviderDTO>> listProviders(UUID accountId, PaginationRequest paginationRequest);

    /**
     * Create a new provider record for a specific account.
     */
    Mono<AccountProviderDTO> createProvider(UUID accountId, AccountProviderDTO providerDTO);

    /**
     * Retrieve a specific provider by its ID, ensuring it belongs to the specified account.
     */
    Mono<AccountProviderDTO> getProvider(UUID accountId, UUID providerId);

    /**
     * Update an existing provider associated with a specific account.
     */
    Mono<AccountProviderDTO> updateProvider(UUID accountId, UUID providerId, AccountProviderDTO providerDTO);

    /**
     * Delete a specific account provider by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteProvider(UUID accountId, UUID providerId);

    /**
     * Retrieve a paginated list of providers for a specific account space.
     */
    Mono<PaginationResponse<AccountProviderDTO>> listProvidersForSpace(UUID accountId, UUID accountSpaceId, PaginationRequest paginationRequest);

    /**
     * Create a new provider record for a specific account space.
     */
    Mono<AccountProviderDTO> createProviderForSpace(UUID accountId, UUID accountSpaceId, AccountProviderDTO providerDTO);

    /**
     * Retrieve a specific provider by its ID, ensuring it belongs to the specified account space.
     */
    Mono<AccountProviderDTO> getProviderForSpace(UUID accountId, UUID accountSpaceId, UUID providerId);

    /**
     * Update an existing provider associated with a specific account space.
     */
    Mono<AccountProviderDTO> updateProviderForSpace(UUID accountId, UUID accountSpaceId, UUID providerId, AccountProviderDTO providerDTO);

    /**
     * Delete a specific account provider by its unique ID, ensuring it belongs to the specified account space.
     */
    Mono<Void> deleteProviderForSpace(UUID accountId, UUID accountSpaceId, UUID providerId);
}
