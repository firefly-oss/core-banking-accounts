package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.TransferFrequencyEnum;
import com.catalis.core.banking.accounts.sdk.model.FilterRequest;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account Space API endpoints.
 */
public class AccountSpaceClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/account-spaces";

    /**
     * Creates a new AccountSpaceClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    public AccountSpaceClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Retrieves a paginated list of account spaces based on filter criteria.
     *
     * @param filterRequest The filter request
     * @return A Mono of PaginationResponse containing AccountSpaceDTOs
     */
    public Mono<PaginationResponse<AccountSpaceDTO>> filterAccountSpaces(FilterRequest<AccountSpaceDTO> filterRequest) {
        return get(BASE_PATH, new ParameterizedTypeReference<PaginationResponse<AccountSpaceDTO>>() {});
    }

    /**
     * Creates a new account space.
     *
     * @param accountSpaceDTO The account space data
     * @return A Mono of the created AccountSpaceDTO
     */
    public Mono<AccountSpaceDTO> createAccountSpace(AccountSpaceDTO accountSpaceDTO) {
        return post(BASE_PATH, accountSpaceDTO, AccountSpaceDTO.class);
    }

    /**
     * Retrieves an account space by its ID.
     *
     * @param accountSpaceId The account space ID
     * @return A Mono of the AccountSpaceDTO
     */
    public Mono<AccountSpaceDTO> getAccountSpace(Long accountSpaceId) {
        return get(BASE_PATH + "/" + accountSpaceId, AccountSpaceDTO.class);
    }

    /**
     * Updates an account space.
     *
     * @param accountSpaceId The account space ID
     * @param accountSpaceDTO The updated account space data
     * @return A Mono of the updated AccountSpaceDTO
     */
    public Mono<AccountSpaceDTO> updateAccountSpace(Long accountSpaceId, AccountSpaceDTO accountSpaceDTO) {
        return put(BASE_PATH + "/" + accountSpaceId, accountSpaceDTO, AccountSpaceDTO.class);
    }

    /**
     * Deletes an account space.
     *
     * @param accountSpaceId The account space ID
     * @return A Mono of Void
     */
    public Mono<Void> deleteAccountSpace(Long accountSpaceId) {
        return delete(BASE_PATH + "/" + accountSpaceId);
    }

    /**
     * Retrieves account spaces by account ID.
     *
     * @param accountId The account ID
     * @return A Flux of AccountSpaceDTOs
     */
    public Flux<AccountSpaceDTO> getAccountSpacesByAccountId(Long accountId) {
        return getFlux(BASE_PATH + "/by-account/" + accountId, AccountSpaceDTO.class);
    }

    /**
     * Retrieves a paginated list of account spaces by account ID.
     *
     * @param accountId The account ID
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Mono of PaginationResponse containing AccountSpaceDTOs
     */
    public Mono<PaginationResponse<AccountSpaceDTO>> getAccountSpacesByAccountIdPaginated(Long accountId, int page, int size) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", page);
        queryParams.put("size", size);
        return get(BASE_PATH + "/by-account/" + accountId + "/paginated", queryParams, 
                new ParameterizedTypeReference<PaginationResponse<AccountSpaceDTO>>() {});
    }

    /**
     * Transfers funds between two account spaces.
     *
     * @param fromAccountSpaceId The source account space ID
     * @param toAccountSpaceId The destination account space ID
     * @param amount The amount to transfer
     * @return A Mono of Boolean indicating success
     */
    public Mono<Boolean> transferBetweenSpaces(Long fromAccountSpaceId, Long toAccountSpaceId, BigDecimal amount) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("fromSpaceId", fromAccountSpaceId);
        queryParams.put("toSpaceId", toAccountSpaceId);
        queryParams.put("amount", amount);
        return post(BASE_PATH + "/transfer", queryParams, null, Boolean.class);
    }

    /**
     * Calculates the progress towards a goal for a specific space.
     *
     * @param accountSpaceId The account space ID
     * @return A Mono of the AccountSpaceDTO with goal progress information
     */
    public Mono<AccountSpaceDTO> calculateGoalProgress(Long accountSpaceId) {
        return get(BASE_PATH + "/" + accountSpaceId + "/goal-progress", AccountSpaceDTO.class);
    }

    /**
     * Retrieves spaces with goals for a specific account.
     *
     * @param accountId The account ID
     * @return A Flux of AccountSpaceDTOs with goals
     */
    public Flux<AccountSpaceDTO> getSpacesWithGoals(Long accountId) {
        return getFlux(BASE_PATH + "/with-goals/" + accountId, AccountSpaceDTO.class);
    }

    /**
     * Retrieves spaces with upcoming target dates for a specific account.
     *
     * @param accountId The account ID
     * @param daysThreshold The number of days in the future to consider 'upcoming'
     * @return A Flux of AccountSpaceDTOs with upcoming target dates
     */
    public Flux<AccountSpaceDTO> getSpacesWithUpcomingTargetDates(Long accountId, int daysThreshold) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("daysThreshold", daysThreshold);
        return getFlux(BASE_PATH + "/with-upcoming-targets/" + accountId, queryParams, AccountSpaceDTO.class);
    }

    /**
     * Configures automatic transfers for a specific space.
     *
     * @param accountSpaceId The account space ID
     * @param enabled Whether automatic transfers are enabled
     * @param frequency The transfer frequency
     * @param amount The transfer amount
     * @param sourceSpaceId The source space ID (null for main account)
     * @return A Mono of the updated AccountSpaceDTO
     */
    public Mono<AccountSpaceDTO> configureAutomaticTransfers(Long accountSpaceId, Boolean enabled, 
            TransferFrequencyEnum frequency, BigDecimal amount, Long sourceSpaceId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("enabled", enabled);
        if (frequency != null) queryParams.put("frequency", frequency);
        if (amount != null) queryParams.put("amount", amount);
        if (sourceSpaceId != null) queryParams.put("sourceSpaceId", sourceSpaceId);
        return post(BASE_PATH + "/" + accountSpaceId + "/configure-transfers", queryParams, null, AccountSpaceDTO.class);
    }

    /**
     * Executes all pending automatic transfers for a specific account.
     *
     * @param accountId The account ID
     * @return A Mono of Integer representing the number of transfers executed
     */
    public Mono<Integer> executeAutomaticTransfers(Long accountId) {
        return post(BASE_PATH + "/execute-transfers/" + accountId, null, Integer.class);
    }

    /**
     * Simulates future balances based on automatic transfers.
     *
     * @param accountId The account ID
     * @param months The number of months to simulate
     * @return A Mono of Map containing space IDs and their simulated balances
     */
    public Mono<Map<Long, BigDecimal>> simulateFutureBalances(Long accountId, int months) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("months", months);
        return get(BASE_PATH + "/simulate-balances/" + accountId, queryParams, 
                new ParameterizedTypeReference<Map<Long, BigDecimal>>() {});
    }

    /**
     * Calculates the distribution of funds across spaces for a specific account.
     *
     * @param accountId The account ID
     * @return A Mono of Map containing space IDs and their balance distribution
     */
    public Mono<Map<Long, BigDecimal>> calculateBalanceDistribution(Long accountId) {
        return get(BASE_PATH + "/balance-distribution/" + accountId, 
                new ParameterizedTypeReference<Map<Long, BigDecimal>>() {});
    }

    /**
     * Calculates the growth rate for each space over a period.
     *
     * @param accountId The account ID
     * @param startDate The start date for the calculation
     * @param endDate The end date for the calculation
     * @return A Mono of Map containing space IDs and their growth rates
     */
    public Mono<Map<Long, BigDecimal>> calculateGrowthRates(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("startDate", startDate);
        queryParams.put("endDate", endDate);
        return get(BASE_PATH + "/growth-rates/" + accountId, queryParams, 
                new ParameterizedTypeReference<Map<Long, BigDecimal>>() {});
    }

    /**
     * Retrieves spaces of a specific type for an account.
     *
     * @param accountId The account ID
     * @param spaceType The space type
     * @return A Flux of AccountSpaceDTOs of the specified type
     */
    public Flux<AccountSpaceDTO> getSpacesByType(Long accountId, AccountSpaceTypeEnum spaceType) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("spaceType", spaceType);
        return getFlux(BASE_PATH + "/by-type/" + accountId, queryParams, AccountSpaceDTO.class);
    }
}