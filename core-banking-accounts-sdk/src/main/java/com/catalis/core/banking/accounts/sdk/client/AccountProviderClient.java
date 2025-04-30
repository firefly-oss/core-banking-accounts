package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account Provider API endpoints.
 */
public class AccountProviderClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/accounts";

    /**
     * Creates a new AccountProviderClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    public AccountProviderClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Retrieves a paginated list of providers for an account.
     *
     * @param accountId The account ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountProviderDTOs
     */
    public Mono<PaginationResponse<AccountProviderDTO>> listProviders(Long accountId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/" + accountId + "/providers", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountProviderDTO>>() {});
    }

    /**
     * Creates a new provider for an account.
     *
     * @param accountId The account ID
     * @param providerDTO The provider data
     * @return A Mono of the created AccountProviderDTO
     */
    public Mono<AccountProviderDTO> createProvider(Long accountId, AccountProviderDTO providerDTO) {
        return post(BASE_PATH + "/" + accountId + "/providers", providerDTO, AccountProviderDTO.class);
    }

    /**
     * Retrieves a provider by its ID.
     *
     * @param accountId The account ID
     * @param providerId The provider ID
     * @return A Mono of the AccountProviderDTO
     */
    public Mono<AccountProviderDTO> getProvider(Long accountId, Long providerId) {
        return get(BASE_PATH + "/" + accountId + "/providers/" + providerId, AccountProviderDTO.class);
    }

    /**
     * Updates a provider.
     *
     * @param accountId The account ID
     * @param providerId The provider ID
     * @param providerDTO The updated provider data
     * @return A Mono of the updated AccountProviderDTO
     */
    public Mono<AccountProviderDTO> updateProvider(Long accountId, Long providerId, AccountProviderDTO providerDTO) {
        return put(BASE_PATH + "/" + accountId + "/providers/" + providerId, providerDTO, AccountProviderDTO.class);
    }

    /**
     * Deletes a provider.
     *
     * @param accountId The account ID
     * @param providerId The provider ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteProvider(Long accountId, Long providerId) {
        return delete(BASE_PATH + "/" + accountId + "/providers/" + providerId);
    }

    /**
     * Retrieves a paginated list of providers for an account space.
     *
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountProviderDTOs
     */
    public Mono<PaginationResponse<AccountProviderDTO>> listProvidersForSpace(Long accountId, Long accountSpaceId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/" + accountId + "/spaces/" + accountSpaceId + "/providers", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountProviderDTO>>() {});
    }

    /**
     * Creates a new provider for an account space.
     *
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param providerDTO The provider data
     * @return A Mono of the created AccountProviderDTO
     */
    public Mono<AccountProviderDTO> createProviderForSpace(Long accountId, Long accountSpaceId, AccountProviderDTO providerDTO) {
        return post(BASE_PATH + "/" + accountId + "/spaces/" + accountSpaceId + "/providers", providerDTO, AccountProviderDTO.class);
    }

    /**
     * Retrieves a provider for an account space by its ID.
     *
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param providerId The provider ID
     * @return A Mono of the AccountProviderDTO
     */
    public Mono<AccountProviderDTO> getProviderForSpace(Long accountId, Long accountSpaceId, Long providerId) {
        return get(BASE_PATH + "/" + accountId + "/spaces/" + accountSpaceId + "/providers/" + providerId, AccountProviderDTO.class);
    }

    /**
     * Updates a provider for an account space.
     *
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param providerId The provider ID
     * @param providerDTO The updated provider data
     * @return A Mono of the updated AccountProviderDTO
     */
    public Mono<AccountProviderDTO> updateProviderForSpace(Long accountId, Long accountSpaceId, Long providerId, AccountProviderDTO providerDTO) {
        return put(BASE_PATH + "/" + accountId + "/spaces/" + accountSpaceId + "/providers/" + providerId, providerDTO, AccountProviderDTO.class);
    }

    /**
     * Deletes a provider for an account space.
     *
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param providerId The provider ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteProviderForSpace(Long accountId, Long accountSpaceId, Long providerId) {
        return delete(BASE_PATH + "/" + accountId + "/spaces/" + accountSpaceId + "/providers/" + providerId);
    }
}