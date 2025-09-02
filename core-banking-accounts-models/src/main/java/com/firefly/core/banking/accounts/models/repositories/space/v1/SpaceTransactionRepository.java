package com.firefly.core.banking.accounts.models.repositories.space.v1;

import com.firefly.core.banking.accounts.models.entities.space.v1.SpaceTransaction;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing space transactions.
 */
public interface SpaceTransactionRepository extends BaseRepository<SpaceTransaction, UUID> {
    
    /**
     * Find all transactions for a specific account space
     * @param accountSpaceId the account space ID
     * @param pageable pagination information
     * @return a Flux of SpaceTransaction entities
     */
    Flux<SpaceTransaction> findByAccountSpaceId(UUID accountSpaceId, Pageable pageable);
    
    /**
     * Count the number of transactions for a specific account space
     * @param accountSpaceId the account space ID
     * @return a Mono with the count
     */
    Mono<Long> countByAccountSpaceId(UUID accountSpaceId);
    
    /**
     * Find transactions for a specific account space within a date range
     * @param accountSpaceId the account space ID
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return a Flux of SpaceTransaction entities
     */
    Flux<SpaceTransaction> findByAccountSpaceIdAndTransactionDateTimeBetween(
            UUID accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable);
    
    /**
     * Count the number of transactions for a specific account space within a date range
     * @param accountSpaceId the account space ID
     * @param startDate the start date
     * @param endDate the end date
     * @return a Mono with the count
     */
    Mono<Long> countByAccountSpaceIdAndTransactionDateTimeBetween(
            UUID accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    /**
     * Calculate the sum of all deposits (positive amounts) for a specific account space within a date range
     * @param accountSpaceId the account space ID
     * @param startDate the start date
     * @param endDate the end date
     * @return a Mono with the sum
     */
    @Query("SELECT COALESCE(SUM(amount), 0) FROM space_transaction " +
            "WHERE account_space_id = :accountSpaceId " +
            "AND transaction_date_time BETWEEN :startDate AND :endDate " +
            "AND amount > 0")
    Mono<BigDecimal> calculateTotalDeposits(UUID accountSpaceId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Calculate the sum of all withdrawals (negative amounts) for a specific account space within a date range
     * @param accountSpaceId the account space ID
     * @param startDate the start date
     * @param endDate the end date
     * @return a Mono with the sum (as a positive number)
     */
    @Query("SELECT COALESCE(SUM(ABS(amount)), 0) FROM space_transaction " +
            "WHERE account_space_id = :accountSpaceId " +
            "AND transaction_date_time BETWEEN :startDate AND :endDate " +
            "AND amount < 0")
    Mono<BigDecimal> calculateTotalWithdrawals(UUID accountSpaceId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find the most recent transaction before a specific date and time
     * @param accountSpaceId the account space ID
     * @param dateTime the date and time
     * @return a Mono with the transaction
     */
    Mono<SpaceTransaction> findFirstByAccountSpaceIdAndTransactionDateTimeLessThanEqualOrderByTransactionDateTimeDesc(
            UUID accountSpaceId, 
            LocalDateTime dateTime);
}
