package com.catalis.core.banking.accounts.web.controllers.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.statement.v1.AccountStatementService;
import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Controller for managing account statements.
 * <p>
 * This controller provides endpoints for generating, retrieving, and managing account statements.
 * It supports both account-level statements and space-specific statements, allowing for comprehensive
 * financial reporting at different levels of granularity.
 * </p>
 * <p>
 * Key features:
 * <ul>
 *   <li>Generate statements for accounts and specific spaces</li>
 *   <li>Retrieve statements with filtering and pagination</li>
 *   <li>Track statement viewing status</li>
 *   <li>Access historical statement data</li>
 *   <li>Generate statements for specific date ranges</li>
 * </ul>
 * </p>
 * <p>
 * Statements provide critical financial information including opening/closing balances,
 * transaction summaries, and period-specific analytics.
 * </p>
 */
@Tag(name = "Account Statements", description = "APIs for generating and managing financial statements for accounts and spaces")
@RestController
@RequestMapping("/api/v1/account-statements")
public class AccountStatementController {

    private static final Logger logger = LoggerFactory.getLogger(AccountStatementController.class);

    @Autowired
    private AccountStatementService service;

    /**
     * Common error handling method for controller endpoints
     * @param e The exception that occurred
     * @param <T> The type of response entity
     * @return A response entity with appropriate status code and error message
     */
    private <T> Mono<ResponseEntity<T>> handleError(Throwable e) {
        logger.error("Error in AccountStatementController: {}", e.getMessage());

        if (e instanceof IllegalArgumentException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else if (e instanceof IllegalStateException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else {
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }

    @Operation(
            summary = "Filter Account Statements",
            description = "Retrieve a paginated list of all account statements based on filter criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No statements found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountStatementDTO>>> filterAccountStatements(
            @ParameterObject @ModelAttribute FilterRequest<AccountStatementDTO> filterRequest
    ) {
        return service.listAccountStatements(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Account Statement",
            description = "Create a new account statement with the specified details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> createAccountStatement(
            @Parameter(description = "Data for the new account statement", required = true,
                    schema = @Schema(implementation = AccountStatementDTO.class))
            @RequestBody AccountStatementDTO accountStatementDTO
    ) {
        return service.createAccountStatement(accountStatementDTO)
                .map(createdStatement -> ResponseEntity.status(201).body(createdStatement))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Get Account Statement by ID",
            description = "Retrieve an existing account statement by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Statement not found",
                    content = @Content)
    })
    @GetMapping(value = "/{accountStatementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> getAccountStatement(
            @Parameter(description = "Unique identifier of the account statement", required = true)
            @PathVariable("accountStatementId") Long accountStatementId
    ) {
        return service.getAccountStatement(accountStatementId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error retrieving account statement {}: {}", accountStatementId, e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @Operation(
            summary = "Update Account Statement",
            description = "Update an existing account statement with the specified details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Statement not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PutMapping(value = "/{accountStatementId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> updateAccountStatement(
            @Parameter(description = "Unique identifier of the account statement to update", required = true)
            @PathVariable("accountStatementId") Long accountStatementId,

            @Parameter(description = "Updated data for the account statement", required = true,
                    schema = @Schema(implementation = AccountStatementDTO.class))
            @RequestBody AccountStatementDTO accountStatementDTO
    ) {
        return service.updateAccountStatement(accountStatementId, accountStatementDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error updating account statement {}: {}", accountStatementId, e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @Operation(
            summary = "Delete Account Statement",
            description = "Delete an existing account statement."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the statement"),
            @ApiResponse(responseCode = "404", description = "Statement not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{accountStatementId}")
    public Mono<ResponseEntity<Void>> deleteAccountStatement(
            @Parameter(description = "Unique identifier of the account statement to delete", required = true)
            @PathVariable("accountStatementId") Long accountStatementId
    ) {
        return service.deleteAccountStatement(accountStatementId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    logger.error("Error deleting account statement {}: {}", accountStatementId, e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.notFound().<Void>build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().<Void>build());
                });
    }

    @Operation(
            summary = "Get Account Statements by Account ID",
            description = "Retrieve all statements for a specific account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid account ID",
                    content = @Content)
    })
    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountStatementDTO>>> getAccountStatementsByAccountId(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return Mono.just(service.getAccountStatementsByAccountId(accountId))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error retrieving statements for account {}: {}", accountId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Get Account Statements by Date Range",
            description = "Retrieve all statements for a specific account within a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping(value = "/account/{accountId}/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountStatementDTO>>> getAccountStatementsByDateRange(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Start date of the range (inclusive)", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date of the range (inclusive)", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return Mono.just(service.getAccountStatementsByDateRange(accountId, startDate, endDate))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error retrieving statements for account {} between {} and {}: {}",
                            accountId, startDate, endDate, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Get Account Statement by Number",
            description = "Retrieve an account statement by its statement number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Statement not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid statement number",
                    content = @Content)
    })
    @GetMapping(value = "/number/{statementNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> getAccountStatementByNumber(
            @Parameter(description = "Statement number", required = true)
            @PathVariable("statementNumber") String statementNumber
    ) {
        return service.getAccountStatementByNumber(statementNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    logger.error("Error retrieving statement with number {}: {}", statementNumber, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Mark Statement as Viewed",
            description = "Mark an account statement as viewed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully marked the statement as viewed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "404", description = "Statement not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Statement already viewed",
                    content = @Content)
    })
    @PostMapping(value = "/{accountStatementId}/mark-viewed", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> markStatementAsViewed(
            @Parameter(description = "Unique identifier of the account statement", required = true)
            @PathVariable("accountStatementId") Long accountStatementId
    ) {
        return service.markStatementAsViewed(accountStatementId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error marking statement {} as viewed: {}", accountStatementId, e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.notFound().build());
                    } else if (e instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @Operation(
            summary = "Get Unviewed Statements",
            description = "Retrieve all unviewed statements for a specific account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the unviewed statements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid account ID",
                    content = @Content)
    })
    @GetMapping(value = "/account/{accountId}/unviewed", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountStatementDTO>>> getUnviewedStatements(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return Mono.just(service.getUnviewedStatements(accountId))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error retrieving unviewed statements for account {}: {}", accountId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Generate Account Statement",
            description = "Generates a comprehensive financial statement for an account covering a specific date range.\n\n" +
                    "This endpoint creates a new statement that summarizes all financial activity for the specified account " +
                    "during the given period. The statement includes opening and closing balances, transaction summaries " +
                    "(deposits, withdrawals, fees, interest), and other relevant financial information.\n\n" +
                    "**Statement Contents:**\n" +
                    "* Account identification information\n" +
                    "* Statement period (start and end dates)\n" +
                    "* Opening and closing balances\n" +
                    "* Total deposits and withdrawals\n" +
                    "* Total fees and interest\n" +
                    "* Generation timestamp\n\n" +
                    "Statements are immutable once generated and can be retrieved later using their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully generated the statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping(value = "/accounts/{accountId}/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> generateStatement(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Start date of the statement period", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date of the statement period", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return service.generateStatement(accountId, startDate, endDate)
                .map(generatedStatement -> ResponseEntity.status(201).body(generatedStatement))
                .onErrorResume(e -> {
                    logger.error("Error generating statement for account {} between {} and {}: {}",
                            accountId, startDate, endDate, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Generate Space Statement",
            description = "Generates a financial statement for a specific account space covering a date range.\n\n" +
                    "This endpoint creates a new statement that summarizes all financial activity for the specified " +
                    "account space during the given period. This allows for more granular financial reporting at the " +
                    "space level rather than the entire account.\n\n" +
                    "Space statements are particularly useful for:\n" +
                    "* Tracking progress toward savings goals\n" +
                    "* Monitoring spending in specific categories\n" +
                    "* Analyzing usage patterns of different account spaces\n" +
                    "* Providing targeted financial reports for specific purposes\n\n" +
                    "The statement includes space-specific metadata and analytics in addition to standard statement information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully generated the space statement",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account space not found",
                    content = @Content)
    })
    @PostMapping(value = "/accounts/{accountId}/spaces/{accountSpaceId}/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountStatementDTO>> generateSpaceStatement(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,

            @Parameter(description = "Start date of the statement period", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date of the statement period", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return service.generateSpaceStatement(accountId, accountSpaceId, startDate, endDate)
                .map(generatedStatement -> ResponseEntity.status(201).body(generatedStatement))
                .onErrorResume(e -> {
                    logger.error("Error generating space statement for account {} space {} between {} and {}: {}",
                            accountId, accountSpaceId, startDate, endDate, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Get Statements by Account Space",
            description = "Retrieves all statements that are associated with a specific account space.\n\n" +
                    "This endpoint returns all statements that contain information about the specified account space, " +
                    "including both dedicated space statements and account-level statements that include space-specific " +
                    "analytics or metadata.\n\n" +
                    "This is useful for:\n" +
                    "* Retrieving the complete financial history of a space\n" +
                    "* Analyzing space performance over time\n" +
                    "* Gathering all financial documentation related to a specific space\n\n" +
                    "The response is a stream of statement DTOs that can be processed individually as they arrive."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the statements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid account space ID",
                    content = @Content)
    })
    @GetMapping(value = "/spaces/{accountSpaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountStatementDTO>>> getStatementsByAccountSpace(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId
    ) {
        return Mono.just(service.getStatementsByAccountSpace(accountSpaceId))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error retrieving statements for account space {}: {}", accountSpaceId, e.getMessage());
                    return handleError(e);
                });
    }
}
