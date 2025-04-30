package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account Status History API endpoints.
 */
public class AccountStatusHistoryClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/accounts";

    /**
     * Creates a new AccountStatusHistoryClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    public AccountStatusHistoryClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Retrieves a paginated list of status history records for an account.
     *
     * @param accountId The account ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountStatusHistoryDTOs
     */
    public Mono<PaginationResponse<AccountStatusHistoryDTO>> listStatusHistory(Long accountId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/" + accountId + "/status-history", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountStatusHistoryDTO>>() {});
    }

    /**
     * Creates a new status history record for an account.
     *
     * @param accountId The account ID
     * @param historyDTO The status history data
     * @return A Mono of the created AccountStatusHistoryDTO
     */
    public Mono<AccountStatusHistoryDTO> createStatusHistory(Long accountId, AccountStatusHistoryDTO historyDTO) {
        return post(BASE_PATH + "/" + accountId + "/status-history", historyDTO, AccountStatusHistoryDTO.class);
    }

    /**
     * Retrieves a status history record by its ID.
     *
     * @param accountId The account ID
     * @param historyId The status history ID
     * @return A Mono of the AccountStatusHistoryDTO
     */
    public Mono<AccountStatusHistoryDTO> getStatusHistory(Long accountId, Long historyId) {
        return get(BASE_PATH + "/" + accountId + "/status-history/" + historyId, AccountStatusHistoryDTO.class);
    }

    /**
     * Updates a status history record.
     *
     * @param accountId The account ID
     * @param historyId The status history ID
     * @param historyDTO The updated status history data
     * @return A Mono of the updated AccountStatusHistoryDTO
     */
    public Mono<AccountStatusHistoryDTO> updateStatusHistory(Long accountId, Long historyId, AccountStatusHistoryDTO historyDTO) {
        return put(BASE_PATH + "/" + accountId + "/status-history/" + historyId, historyDTO, AccountStatusHistoryDTO.class);
    }

    /**
     * Deletes a status history record.
     *
     * @param accountId The account ID
     * @param historyId The status history ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteStatusHistory(Long accountId, Long historyId) {
        return delete(BASE_PATH + "/" + accountId + "/status-history/" + historyId);
    }
}