package com.catalis.core.banking.accounts.core.services.space.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.SpaceTransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service for managing transaction history for account spaces.
 * This service tracks and retrieves transactions specific to account spaces.
 */
public interface AccountSpaceTransactionService {
    
    /**
     * Record a transaction for an account space
     * @param accountSpaceId The account space ID
     * @param amount The transaction amount (positive for deposits, negative for withdrawals)
     * @param description The transaction description
     * @param referenceId Optional reference ID (e.g., transaction ID from another system)
     * @return Mono of SpaceTransactionDTO
     */
    Mono<SpaceTransactionDTO> recordTransaction(Long accountSpaceId, BigDecimal amount, String description, String referenceId);
    
    /**
     * Get all transactions for an account space
     * @param accountSpaceId The account space ID
     * @param paginationRequest The pagination request
     * @return Mono of PaginationResponse containing SpaceTransactionDTO
     */
    Mono<PaginationResponse<SpaceTransactionDTO>> getTransactions(Long accountSpaceId, PaginationRequest paginationRequest);
    
    /**
     * Get transactions for an account space within a date range
     * @param accountSpaceId The account space ID
     * @param startDate The start date
     * @param endDate The end date
     * @param paginationRequest The pagination request
     * @return Mono of PaginationResponse containing SpaceTransactionDTO
     */
    Mono<PaginationResponse<SpaceTransactionDTO>> getTransactionsByDateRange(
            Long accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            PaginationRequest paginationRequest);
    
    /**
     * Calculate total deposits for an account space within a date range
     * @param accountSpaceId The account space ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Mono of BigDecimal
     */
    Mono<BigDecimal> calculateTotalDeposits(Long accountSpaceId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Calculate total withdrawals for an account space within a date range
     * @param accountSpaceId The account space ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Mono of BigDecimal
     */
    Mono<BigDecimal> calculateTotalWithdrawals(Long accountSpaceId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get the balance of an account space at a specific point in time
     * @param accountSpaceId The account space ID
     * @param dateTime The date and time
     * @return Mono of BigDecimal
     */
    Mono<BigDecimal> getBalanceAtDateTime(Long accountSpaceId, LocalDateTime dateTime);
}
