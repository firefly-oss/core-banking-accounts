package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Client for interacting with the Account Statement API.
 * This client provides methods for generating and retrieving account statements.
 */
public class AccountStatementClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/account-statements";

    /**
     * Constructs a new AccountStatementClient with the given WebClient.
     *
     * @param webClient the WebClient to use for API requests
     */
    public AccountStatementClient(WebClient webClient) {
        super(webClient);
    }

    /**
     * Generate a statement for an account.
     *
     * @param accountId the ID of the account
     * @param startDate the start date of the statement period
     * @param endDate the end date of the statement period
     * @return a Mono containing the generated AccountStatementDTO
     */
    public Mono<AccountStatementDTO> generateStatement(
            Long accountId,
            LocalDate startDate,
            LocalDate endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/accounts/{accountId}/generate")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .build(accountId))
                .retrieve()
                .bodyToMono(AccountStatementDTO.class);
    }

    /**
     * Generate a statement for a specific account space.
     *
     * @param accountId the ID of the account
     * @param accountSpaceId the ID of the account space
     * @param startDate the start date of the statement period
     * @param endDate the end date of the statement period
     * @return a Mono containing the generated AccountStatementDTO
     */
    public Mono<AccountStatementDTO> generateSpaceStatement(
            Long accountId,
            Long accountSpaceId,
            LocalDate startDate,
            LocalDate endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/accounts/{accountId}/spaces/{accountSpaceId}/generate")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .build(accountId, accountSpaceId))
                .retrieve()
                .bodyToMono(AccountStatementDTO.class);
    }

    /**
     * Get a statement by ID.
     *
     * @param statementId the ID of the statement
     * @return a Mono containing the AccountStatementDTO
     */
    public Mono<AccountStatementDTO> getStatement(Long statementId) {
        return webClient.get()
                .uri(BASE_PATH + "/{statementId}", statementId)
                .retrieve()
                .bodyToMono(AccountStatementDTO.class);
    }

    /**
     * Get statements for an account with pagination.
     *
     * @param accountId the ID of the account
     * @param page the page number (0-based)
     * @param size the page size
     * @return a Mono containing a PaginationResponse of AccountStatementDTO
     */
    public Mono<PaginationResponse<AccountStatementDTO>> getStatementsByAccountId(
            Long accountId,
            int page,
            int size) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/account/{accountId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(accountId))
                .retrieve()
                .bodyToMono(createPaginationResponseType(AccountStatementDTO.class));
    }

    /**
     * Get statements for a specific account space.
     *
     * @param accountSpaceId the ID of the account space
     * @return a Flux of AccountStatementDTO
     */
    public Flux<AccountStatementDTO> getStatementsByAccountSpace(Long accountSpaceId) {
        return webClient.get()
                .uri(BASE_PATH + "/spaces/{accountSpaceId}", accountSpaceId)
                .retrieve()
                .bodyToFlux(AccountStatementDTO.class);
    }

    /**
     * Mark a statement as viewed.
     *
     * @param statementId the ID of the statement
     * @return a Mono containing the updated AccountStatementDTO
     */
    public Mono<AccountStatementDTO> markStatementAsViewed(Long statementId) {
        return webClient.patch()
                .uri(BASE_PATH + "/{statementId}/mark-viewed", statementId)
                .retrieve()
                .bodyToMono(AccountStatementDTO.class);
    }

    /**
     * Get unviewed statements for an account.
     *
     * @param accountId the ID of the account
     * @return a Flux of AccountStatementDTO
     */
    public Flux<AccountStatementDTO> getUnviewedStatements(Long accountId) {
        return webClient.get()
                .uri(BASE_PATH + "/account/{accountId}/unviewed", accountId)
                .retrieve()
                .bodyToFlux(AccountStatementDTO.class);
    }

    /**
     * Get statements for an account within a date range with pagination.
     *
     * @param accountId the ID of the account
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param page the page number (0-based)
     * @param size the page size
     * @return a Mono containing a PaginationResponse of AccountStatementDTO
     */
    public Mono<PaginationResponse<AccountStatementDTO>> getStatementsByDateRange(
            Long accountId,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/account/{accountId}/date-range")
                        .queryParam("startDate", startDate.format(formatter))
                        .queryParam("endDate", endDate.format(formatter))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(accountId))
                .retrieve()
                .bodyToMono(createPaginationResponseType(AccountStatementDTO.class));
    }

    /**
     * Get a statement by statement number.
     *
     * @param statementNumber the statement number
     * @return a Mono containing the AccountStatementDTO
     */
    public Mono<AccountStatementDTO> getStatementByNumber(String statementNumber) {
        return webClient.get()
                .uri(BASE_PATH + "/number/{statementNumber}", statementNumber)
                .retrieve()
                .bodyToMono(AccountStatementDTO.class);
    }
}
