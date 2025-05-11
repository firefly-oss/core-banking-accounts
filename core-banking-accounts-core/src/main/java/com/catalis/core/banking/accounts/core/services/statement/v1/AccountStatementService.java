package com.catalis.core.banking.accounts.core.services.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface AccountStatementService {

    /**
     * Create a new account statement
     * @param accountStatementDTO The account statement DTO
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> createAccountStatement(AccountStatementDTO accountStatementDTO);

    /**
     * Get an account statement by ID
     * @param accountStatementId The account statement ID
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> getAccountStatement(Long accountStatementId);

    /**
     * Update an account statement
     * @param accountStatementId The account statement ID
     * @param accountStatementDTO The account statement DTO
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> updateAccountStatement(Long accountStatementId, AccountStatementDTO accountStatementDTO);

    /**
     * Delete an account statement
     * @param accountStatementId The account statement ID
     * @return Mono of Void
     */
    Mono<Void> deleteAccountStatement(Long accountStatementId);

    /**
     * Get all account statements for an account
     * @param accountId The account ID
     * @return Flux of AccountStatementDTO
     */
    Flux<AccountStatementDTO> getAccountStatementsByAccountId(Long accountId);

    /**
     * Get account statements for an account within a date range
     * @param accountId The account ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Flux of AccountStatementDTO
     */
    Flux<AccountStatementDTO> getAccountStatementsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * Get an account statement by statement number
     * @param statementNumber The statement number
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> getAccountStatementByNumber(String statementNumber);

    /**
     * Mark an account statement as viewed
     * @param accountStatementId The account statement ID
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> markStatementAsViewed(Long accountStatementId);

    /**
     * Get unviewed statements for an account
     * @param accountId The account ID
     * @return Flux of AccountStatementDTO
     */
    Flux<AccountStatementDTO> getUnviewedStatements(Long accountId);

    /**
     * Generate a statement for an account
     * @param accountId The account ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> generateStatement(Long accountId, LocalDate startDate, LocalDate endDate);

    /**
     * List account statements with pagination and filtering
     * @param filterRequest The filter request
     * @return Mono of PaginationResponse containing AccountStatementDTO
     */
    Mono<PaginationResponse<AccountStatementDTO>> listAccountStatements(FilterRequest filterRequest);

    /**
     * Generate a statement for a specific account space
     * @param accountId The account ID
     * @param accountSpaceId The account space ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Mono of AccountStatementDTO
     */
    Mono<AccountStatementDTO> generateSpaceStatement(Long accountId, Long accountSpaceId, LocalDate startDate, LocalDate endDate);

    /**
     * Get all statements that include space-specific analytics
     * @param accountSpaceId The account space ID
     * @return Flux of AccountStatementDTO
     */
    Flux<AccountStatementDTO> getStatementsByAccountSpace(Long accountSpaceId);
}
