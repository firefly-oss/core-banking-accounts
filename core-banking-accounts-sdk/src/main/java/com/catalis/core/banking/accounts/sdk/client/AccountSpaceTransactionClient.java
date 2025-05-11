package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.SpaceTransactionDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Client for interacting with the Account Space Transaction API.
 * This client provides methods for recording and retrieving transactions for account spaces.
 */
public class AccountSpaceTransactionClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/account-spaces/{accountSpaceId}/transactions";
    
    /**
     * Constructs a new AccountSpaceTransactionClient with the given WebClient.
     *
     * @param webClient the WebClient to use for API requests
     */
    public AccountSpaceTransactionClient(WebClient webClient) {
        super(webClient);
    }
    
    /**
     * Record a transaction for an account space.
     *
     * @param accountSpaceId the ID of the account space
     * @param amount the transaction amount (positive for deposits, negative for withdrawals)
     * @param description the transaction description (optional)
     * @param referenceId the reference ID for cross-system tracking (optional)
     * @return a Mono containing the created SpaceTransactionDTO
     */
    public Mono<SpaceTransactionDTO> recordTransaction(
            Long accountSpaceId, 
            BigDecimal amount, 
            String description, 
            String referenceId) {
        
        StringBuilder uriBuilder = new StringBuilder(BASE_PATH);
        uriBuilder.append("?amount=").append(amount);
        
        if (description != null && !description.isEmpty()) {
            uriBuilder.append("&description=").append(encodeParam(description));
        }
        
        if (referenceId != null && !referenceId.isEmpty()) {
            uriBuilder.append("&referenceId=").append(encodeParam(referenceId));
        }
        
        return webClient.post()
                .uri(uriBuilder.toString(), accountSpaceId)
                .retrieve()
                .bodyToMono(SpaceTransactionDTO.class);
    }
    
    /**
     * Get transactions for an account space with pagination.
     *
     * @param accountSpaceId the ID of the account space
     * @param page the page number (0-based)
     * @param size the page size
     * @return a Mono containing a PaginationResponse of SpaceTransactionDTO
     */
    public Mono<PaginationResponse<SpaceTransactionDTO>> getTransactions(
            Long accountSpaceId, 
            int page, 
            int size) {
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(accountSpaceId))
                .retrieve()
                .bodyToMono(createPaginationResponseType(SpaceTransactionDTO.class));
    }
    
    /**
     * Get transactions for an account space within a date range with pagination.
     *
     * @param accountSpaceId the ID of the account space
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param page the page number (0-based)
     * @param size the page size
     * @return a Mono containing a PaginationResponse of SpaceTransactionDTO
     */
    public Mono<PaginationResponse<SpaceTransactionDTO>> getTransactionsByDateRange(
            Long accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            int page, 
            int size) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/date-range")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(accountSpaceId))
                .retrieve()
                .bodyToMono(createPaginationResponseType(SpaceTransactionDTO.class));
    }
    
    /**
     * Calculate total deposits for an account space within a date range.
     *
     * @param accountSpaceId the ID of the account space
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Mono containing the total deposits amount
     */
    public Mono<BigDecimal> calculateTotalDeposits(
            Long accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/total-deposits")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .build(accountSpaceId))
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }
    
    /**
     * Calculate total withdrawals for an account space within a date range.
     *
     * @param accountSpaceId the ID of the account space
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a Mono containing the total withdrawals amount
     */
    public Mono<BigDecimal> calculateTotalWithdrawals(
            Long accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/total-withdrawals")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .build(accountSpaceId))
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }
    
    /**
     * Get the balance of an account space at a specific point in time.
     *
     * @param accountSpaceId the ID of the account space
     * @param dateTime the date and time to get the balance at
     * @return a Mono containing the balance amount
     */
    public Mono<BigDecimal> getBalanceAtDateTime(
            Long accountSpaceId, 
            LocalDateTime dateTime) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/balance-at")
                        .queryParam("dateTime", dateTime.format(formatter))
                        .build(accountSpaceId))
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }
}
