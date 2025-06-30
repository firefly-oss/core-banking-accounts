package com.catalis.core.banking.accounts.web.controllers.space.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.space.v1.AccountSpaceService;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.SpaceAnalyticsDTO;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.TransferFrequencyEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Account Spaces", description = "APIs for managing account spaces/buckets within bank accounts")
@RestController
@RequestMapping("/api/v1/account-spaces")
public class AccountSpaceController {

    private static final Logger logger = LoggerFactory.getLogger(AccountSpaceController.class);

    /**
     * Common error handling method for controller endpoints
     * @param e The exception that occurred
     * @param <T> The type of response entity
     * @return A response entity with appropriate status code and error message
     */
    private <T> Mono<ResponseEntity<T>> handleError(Throwable e) {
        logger.error("Error in AccountSpaceController: {}", e.getMessage());

        if (e instanceof IllegalArgumentException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else if (e instanceof IllegalStateException) {
            return Mono.just(ResponseEntity.badRequest().build());
        } else {
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }

    @Autowired
    private AccountSpaceService service;

    @Operation(
            summary = "Filter Account Spaces",
            description = "Retrieve a paginated list of all account spaces based on filter criteria."
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountSpaceDTO>>> filterAccountSpaces(
            @RequestBody FilterRequest<AccountSpaceDTO> filterRequest
    ) {
        return service.filterAccountSpaces(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Account Space",
            description = "Create a new account space with the specified details."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> createAccountSpace(
            @Parameter(description = "Data for the new account space", required = true,
                    schema = @Schema(implementation = AccountSpaceDTO.class))
            @RequestBody AccountSpaceDTO accountSpaceDTO
    ) {
        return service.createAccountSpace(accountSpaceDTO)
                .map(createdSpace -> ResponseEntity.status(201).body(createdSpace))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Get Account Space by ID",
            description = "Retrieve an existing account space by its unique identifier."
    )
    @GetMapping(value = "/{accountSpaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> getAccountSpace(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId
    ) {
        return service.getAccountSpace(accountSpaceId)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update Account Space",
            description = "Update the details of an existing account space by its unique identifier."
    )
    @PutMapping(value = "/{accountSpaceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> updateAccountSpace(
            @Parameter(description = "Unique identifier of the account space to update", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,

            @Parameter(description = "Updated account space data", required = true,
                    schema = @Schema(implementation = AccountSpaceDTO.class))
            @RequestBody AccountSpaceDTO accountSpaceDTO
    ) {
        return service.updateAccountSpace(accountSpaceId, accountSpaceDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Delete Account Space",
            description = "Delete an existing account space by its unique identifier. Main spaces cannot be deleted."
    )
    @DeleteMapping(value = "/{accountSpaceId}")
    public Mono<ResponseEntity<Void>> deleteAccountSpace(
            @Parameter(description = "Unique identifier of the account space to delete", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId
    ) {
        return service.deleteAccountSpace(accountSpaceId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    logger.error("Error deleting account space {}: {}", accountSpaceId, e.getMessage());
                    if (e instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.badRequest().<Void>build());
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().<Void>build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().<Void>build());
                });
    }

    @Operation(
            summary = "Get Account Spaces by Account ID",
            description = "Retrieve all account spaces for a specific account."
    )
    @GetMapping(value = "/by-account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountSpaceDTO>>> getAccountSpacesByAccountId(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        Flux<AccountSpaceDTO> spaces = service.getAccountSpacesByAccountId(accountId);
        return Mono.just(ResponseEntity.ok(spaces));
    }

    @Operation(
            summary = "Get Paginated Account Spaces by Account ID",
            description = "Retrieve a paginated list of account spaces for a specific account."
    )
    @GetMapping(value = "/by-account/{accountId}/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountSpaceDTO>>> getAccountSpacesByAccountIdPaginated(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Page number (0-based)", required = true)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Page size", required = true)
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return service.getAccountSpacesByAccountId(accountId, page, size)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Transfer Between Spaces",
            description = "Transfer funds between two account spaces within the same account."
    )
    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Boolean>> transferBetweenSpaces(
            @Parameter(description = "Source account space ID", required = true)
            @RequestParam("fromSpaceId") Long fromAccountSpaceId,

            @Parameter(description = "Destination account space ID", required = true)
            @RequestParam("toSpaceId") Long toAccountSpaceId,

            @Parameter(description = "Amount to transfer", required = true)
            @RequestParam("amount") BigDecimal amount
    ) {
        return service.transferBetweenSpaces(fromAccountSpaceId, toAccountSpaceId, amount)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error transferring between spaces {} and {}: {}",
                            fromAccountSpaceId, toAccountSpaceId, e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().body(false));
                    }
                    return Mono.just(ResponseEntity.internalServerError().body(false));
                });
    }

    // ===== Goal Tracking Endpoints =====

    @Operation(
            summary = "Calculate Goal Progress",
            description = "Calculate progress towards a goal for a specific space."
    )
    @GetMapping(value = "/{accountSpaceId}/goal-progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> calculateGoalProgress(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId
    ) {
        return service.calculateGoalProgress(accountSpaceId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get Spaces with Goals",
            description = "Retrieve all spaces with goals for a specific account."
    )
    @GetMapping(value = "/with-goals/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountSpaceDTO>>> getSpacesWithGoals(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        Flux<AccountSpaceDTO> spaces = service.getSpacesWithGoals(accountId);
        return Mono.just(ResponseEntity.ok(spaces));
    }

    @Operation(
            summary = "Get Spaces with Upcoming Target Dates",
            description = "Retrieve all spaces with upcoming target dates for a specific account."
    )
    @GetMapping(value = "/with-upcoming-targets/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountSpaceDTO>>> getSpacesWithUpcomingTargetDates(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Number of days in the future to consider 'upcoming'", required = true)
            @RequestParam(value = "daysThreshold", defaultValue = "30") int daysThreshold
    ) {
        Flux<AccountSpaceDTO> spaces = service.getSpacesWithUpcomingTargetDates(accountId, daysThreshold);
        return Mono.just(ResponseEntity.ok(spaces));
    }

    // ===== Automatic Transfer Endpoints =====

    @Operation(
            summary = "Configure Automatic Transfers",
            description = "Configure automatic transfers for a specific space."
    )
    @PostMapping(value = "/{accountSpaceId}/configure-transfers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> configureAutomaticTransfers(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable("accountSpaceId") Long accountSpaceId,

            @Parameter(description = "Whether automatic transfers are enabled", required = true)
            @RequestParam("enabled") Boolean enabled,

            @Parameter(description = "Transfer frequency", required = false)
            @RequestParam(value = "frequency", required = false) TransferFrequencyEnum frequency,

            @Parameter(description = "Transfer amount", required = false)
            @RequestParam(value = "amount", required = false) BigDecimal amount,

            @Parameter(description = "Source space ID (null for main account)", required = false)
            @RequestParam(value = "sourceSpaceId", required = false) Long sourceSpaceId
    ) {
        return service.configureAutomaticTransfers(accountSpaceId, enabled, frequency, amount, sourceSpaceId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error configuring automatic transfers for space {}: {}",
                            accountSpaceId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Execute Automatic Transfers",
            description = "Execute all pending automatic transfers for a specific account."
    )
    @PostMapping(value = "/execute-transfers/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Integer>> executeAutomaticTransfers(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return service.executeAutomaticTransfers(accountId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error executing automatic transfers for account {}: {}",
                            accountId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Simulate Future Balances",
            description = "Simulate future balances based on automatic transfers."
    )
    @GetMapping(value = "/simulate-balances/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<Long, BigDecimal>>> simulateFutureBalances(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Number of months to simulate", required = true)
            @RequestParam(value = "months", defaultValue = "12") int months
    ) {
        return service.simulateFutureBalances(accountId, months)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error simulating future balances for account {}: {}",
                            accountId, e.getMessage());
                    return handleError(e);
                });
    }

    // ===== Analytics Endpoints =====

    @Operation(
            summary = "Calculate Balance Distribution",
            description = "Calculate the distribution of funds across spaces for a specific account."
    )
    @GetMapping(value = "/balance-distribution/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<Long, BigDecimal>>> calculateBalanceDistribution(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return service.calculateBalanceDistribution(accountId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error calculating balance distribution for account {}: {}",
                            accountId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Calculate Growth Rates",
            description = "Calculate the growth rate for each space over a period."
    )
    @GetMapping(value = "/growth-rates/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<Long, BigDecimal>>> calculateGrowthRates(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Start date for the calculation", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date for the calculation", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return service.calculateGrowthRates(accountId, startDate, endDate)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    logger.error("Error calculating growth rates for account {}: {}",
                            accountId, e.getMessage());
                    return handleError(e);
                });
    }

    @Operation(
            summary = "Get Spaces by Type",
            description = "Retrieve all spaces of a specific type for an account."
    )
    @GetMapping(value = "/by-type/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<AccountSpaceDTO>>> getSpacesByType(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Space type", required = true)
            @RequestParam("spaceType") AccountSpaceTypeEnum spaceType
    ) {
        Flux<AccountSpaceDTO> spaces = service.getSpacesByType(accountId, spaceType);
        return Mono.just(ResponseEntity.ok(spaces));
    }

    // ===== Status Management Endpoints =====

    @Operation(
            summary = "Freeze Account Space",
            description = "Freezes an account space, preventing withdrawals and transfers from it."
    )
    @PostMapping(value = "/{accountSpaceId}/freeze", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> freezeAccountSpace(
            @Parameter(description = "Unique identifier of the account space to freeze", required = true)
            @PathVariable Long accountSpaceId
    ) {
        return service.freezeAccountSpace(accountSpaceId)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Unfreeze Account Space",
            description = "Unfreezes a previously frozen account space, allowing normal operations again."
    )
    @PostMapping(value = "/{accountSpaceId}/unfreeze", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> unfreezeAccountSpace(
            @Parameter(description = "Unique identifier of the account space to unfreeze", required = true)
            @PathVariable Long accountSpaceId
    ) {
        return service.unfreezeAccountSpace(accountSpaceId)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update Account Space Balance",
            description = "Updates the balance of an account space directly. For administrative adjustments only."
    )
    @PutMapping(value = "/{accountSpaceId}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountSpaceDTO>> updateAccountSpaceBalance(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "New balance to set", required = true)
            @RequestParam BigDecimal newBalance,

            @Parameter(description = "Reason for the balance adjustment", required = true)
            @RequestParam String reason
    ) {
        return service.updateAccountSpaceBalance(accountSpaceId, newBalance, reason)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }

    // ===== Analytics Endpoints =====

    @Operation(
            summary = "Get Space Analytics",
            description = "Generates detailed analytics for a specific account space over a period."
    )
    @GetMapping(value = "/{accountSpaceId}/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SpaceAnalyticsDTO>> getSpaceAnalytics(
            @Parameter(description = "Unique identifier of the account space", required = true)
            @PathVariable Long accountSpaceId,

            @Parameter(description = "Start date for the analysis period (optional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date for the analysis period (optional)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return service.getSpaceAnalytics(accountSpaceId, startDate, endDate)
                .map(ResponseEntity::ok)
                .onErrorResume(this::handleError);
    }
}
