package com.catalis.core.banking.accounts.web.controllers.space.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.space.v1.AccountSpaceTransactionService;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.SpaceTransactionDTO;
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
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Controller for managing account space transactions.
 */
@Tag(name = "Account Space Transactions", description = "APIs for managing transactions for account spaces")
@RestController
@RequestMapping("/api/v1/account-spaces/{accountSpaceId}/transactions")
public class AccountSpaceTransactionController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountSpaceTransactionController.class);
    
    @Autowired
    private AccountSpaceTransactionService service;
    
    /**
     * Common error handling method for controller endpoints
     * @param e The exception that occurred
     * @param <T> The type of response entity
     * @return A response entity with appropriate status code and error message
     */
    private <T> Mono<ResponseEntity<T>> handleError(Throwable e) {
        logger.error("Error in AccountSpaceTransactionController: {}", e.getMessage());
        
        if (e instanceof IllegalArgumentException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else if (e instanceof IllegalStateException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else {
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }
    
    @Operation(
            summary = "Record Transaction",
            description = "Record a new transaction for an account space."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully recorded the transaction",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceTransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account space not found",
                    content = @Content)
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SpaceTransactionDTO>> recordTransaction(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @Parameter(description = "Transaction amount (positive for deposits, negative for withdrawals)", required = true)
            @RequestParam("amount") BigDecimal amount,
            
            @Parameter(description = "Transaction description")
            @RequestParam(value = "description", required = false) String description,
            
            @Parameter(description = "Reference ID for cross-system tracking")
            @RequestParam(value = "referenceId", required = false) String referenceId
    ) {
        return service.recordTransaction(accountSpaceId, amount, description, referenceId)
                .map(transaction -> ResponseEntity.status(201).body(transaction))
                .onErrorResume(this::handleError);
    }
    
    @Operation(
            summary = "Get Transactions",
            description = "Retrieve a paginated list of transactions for an account space."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No transactions found",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<SpaceTransactionDTO>>> getTransactions(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.getTransactions(accountSpaceId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(this::handleError);
    }
    
    @Operation(
            summary = "Get Transactions by Date Range",
            description = "Retrieve a paginated list of transactions for an account space within a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the transactions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No transactions found",
                    content = @Content)
    })
    @GetMapping(value = "/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<SpaceTransactionDTO>>> getTransactionsByDateRange(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @Parameter(description = "Start date of the range (inclusive)", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "End date of the range (inclusive)", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            
            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.getTransactionsByDateRange(accountSpaceId, startDate, endDate, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(this::handleError);
    }
    
    @Operation(
            summary = "Calculate Total Deposits",
            description = "Calculate the total deposits for an account space within a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated the total deposits",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping(value = "/total-deposits", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BigDecimal>> calculateTotalDeposits(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @Parameter(description = "Start date of the range (inclusive)", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "End date of the range (inclusive)", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return service.calculateTotalDeposits(accountSpaceId, startDate, endDate)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }
    
    @Operation(
            summary = "Calculate Total Withdrawals",
            description = "Calculate the total withdrawals for an account space within a date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated the total withdrawals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping(value = "/total-withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BigDecimal>> calculateTotalWithdrawals(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @Parameter(description = "Start date of the range (inclusive)", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "End date of the range (inclusive)", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return service.calculateTotalWithdrawals(accountSpaceId, startDate, endDate)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }
    
    @Operation(
            summary = "Get Balance at Date Time",
            description = "Get the balance of an account space at a specific point in time."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the balance",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping(value = "/balance-at", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BigDecimal>> getBalanceAtDateTime(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,
            
            @Parameter(description = "Date and time to get the balance at", required = true)
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        return service.getBalanceAtDateTime(accountSpaceId, dateTime)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }
}
