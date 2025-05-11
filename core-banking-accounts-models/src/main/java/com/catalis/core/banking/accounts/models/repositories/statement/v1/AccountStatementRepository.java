package com.catalis.core.banking.accounts.models.repositories.statement.v1;

import com.catalis.core.banking.accounts.models.entities.statement.v1.AccountStatement;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface AccountStatementRepository extends BaseRepository<AccountStatement, Long> {
    
    /**
     * Find all statements for a specific account
     * @param accountId The account ID
     * @return Flux of AccountStatement
     */
    Flux<AccountStatement> findByAccountId(Long accountId);
    
    /**
     * Find statement by statement number
     * @param statementNumber The statement number
     * @return Mono of AccountStatement
     */
    Mono<AccountStatement> findByStatementNumber(String statementNumber);
    
    /**
     * Find statements for a specific account within a date range
     * @param accountId The account ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Flux of AccountStatement
     */
    Flux<AccountStatement> findByAccountIdAndPeriodEndDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find unviewed statements for a specific account
     * @param accountId The account ID
     * @param isViewed Whether the statement has been viewed
     * @return Flux of AccountStatement
     */
    Flux<AccountStatement> findByAccountIdAndIsViewed(Long accountId, Boolean isViewed);
    
    /**
     * Count unviewed statements for a specific account
     * @param accountId The account ID
     * @param isViewed Whether the statement has been viewed
     * @return Mono of Long representing the count
     */
    Mono<Long> countByAccountIdAndIsViewed(Long accountId, Boolean isViewed);
}
