package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.sdk.model.FilterRequest;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account Parameter API endpoints.
 */
public class AccountParameterClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/accounts";

    /**
     * Creates a new AccountParameterClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    public AccountParameterClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Retrieves a paginated list of parameters for an account.
     *
     * @param accountId The account ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountParameterDTOs
     */
    public Mono<PaginationResponse<AccountParameterDTO>> listParameters(Long accountId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/" + accountId + "/parameters", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountParameterDTO>>() {});
    }

    /**
     * Creates a new parameter for an account.
     *
     * @param accountId The account ID
     * @param parameterDTO The parameter data
     * @return A Mono of the created AccountParameterDTO
     */
    public Mono<AccountParameterDTO> createParameter(Long accountId, AccountParameterDTO parameterDTO) {
        return post(BASE_PATH + "/" + accountId + "/parameters", parameterDTO, AccountParameterDTO.class);
    }

    /**
     * Retrieves a parameter by its ID.
     *
     * @param accountId The account ID
     * @param parameterId The parameter ID
     * @return A Mono of the AccountParameterDTO
     */
    public Mono<AccountParameterDTO> getParameter(Long accountId, Long parameterId) {
        return get(BASE_PATH + "/" + accountId + "/parameters/" + parameterId, AccountParameterDTO.class);
    }

    /**
     * Updates a parameter.
     *
     * @param accountId The account ID
     * @param parameterId The parameter ID
     * @param parameterDTO The updated parameter data
     * @return A Mono of the updated AccountParameterDTO
     */
    public Mono<AccountParameterDTO> updateParameter(Long accountId, Long parameterId, AccountParameterDTO parameterDTO) {
        return put(BASE_PATH + "/" + accountId + "/parameters/" + parameterId, parameterDTO, AccountParameterDTO.class);
    }

    /**
     * Deletes a parameter.
     *
     * @param accountId The account ID
     * @param parameterId The parameter ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteParameter(Long accountId, Long parameterId) {
        return delete(BASE_PATH + "/" + accountId + "/parameters/" + parameterId);
    }
}