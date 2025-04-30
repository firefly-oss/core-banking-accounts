package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.sdk.model.FilterRequest;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account API endpoints.
 */
public class AccountClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/accounts";
    private static final String BALANCE_PATH = "/api/v1/account-balances";

    /**
     * Creates a new AccountClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    public AccountClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Retrieves a paginated list of accounts based on filter criteria.
     *
     * @param filterRequest The filter request
     * @return A Mono of PaginationResponse containing AccountDTOs
     */
    public Mono<PaginationResponse<AccountDTO>> filterAccounts(FilterRequest<AccountDTO> filterRequest) {
        return get(BASE_PATH, new ParameterizedTypeReference<PaginationResponse<AccountDTO>>() {});
    }

    /**
     * Creates a new account.
     *
     * @param accountDTO The account data
     * @return A Mono of the created AccountDTO
     */
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        return post(BASE_PATH, accountDTO, AccountDTO.class);
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId The account ID
     * @return A Mono of the AccountDTO
     */
    public Mono<AccountDTO> getAccount(Long accountId) {
        return get(BASE_PATH + "/" + accountId, AccountDTO.class);
    }

    /**
     * Updates an account.
     *
     * @param accountId The account ID
     * @param accountDTO The updated account data
     * @return A Mono of the updated AccountDTO
     */
    public Mono<AccountDTO> updateAccount(Long accountId, AccountDTO accountDTO) {
        return put(BASE_PATH + "/" + accountId, accountDTO, AccountDTO.class);
    }

    /**
     * Deletes an account.
     *
     * @param accountId The account ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteAccount(Long accountId) {
        return delete(BASE_PATH + "/" + accountId);
    }

    /**
     * Retrieves accounts by customer ID.
     *
     * @param customerId The customer ID
     * @return A Flux of AccountDTOs
     */
    public Flux<AccountDTO> getAccountsByCustomerId(Long customerId) {
        return getFlux(BASE_PATH + "/by-customer/" + customerId, AccountDTO.class);
    }

    /**
     * Retrieves a paginated list of accounts by customer ID.
     *
     * @param customerId The customer ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountDTOs
     */
    public Mono<PaginationResponse<AccountDTO>> getAccountsByCustomerIdPaginated(Long customerId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/by-customer/" + customerId + "/paginated", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountDTO>>() {});
    }

    /**
     * Retrieves the balance for an account.
     *
     * @param accountId The account ID
     * @return A Mono of AccountBalanceDTO
     */
    public Mono<AccountBalanceDTO> getAccountBalance(Long accountId) {
        return get(BALANCE_PATH + "/" + accountId, AccountBalanceDTO.class);
    }

    /**
     * Retrieves the balance history for an account.
     *
     * @param accountId The account ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountBalanceDTOs
     */
    public Mono<PaginationResponse<AccountBalanceDTO>> getAccountBalanceHistory(Long accountId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BALANCE_PATH + "/history/" + accountId, queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountBalanceDTO>>() {});
    }
}
