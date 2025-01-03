package com.catalis.core.banking.accounts.web.controllers.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountBalanceCreateService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountBalanceDeleteService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountBalanceGetService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountBalanceUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/balances")
@Tag(name = "Account Management API - Balances", description = "Endpoints for managing account balances.")
public class AccountBalanceController {

    @Autowired
    private AccountBalanceGetService accountBalanceGetService;

    @Autowired
    private AccountBalanceCreateService accountBalanceCreateService;

    @Autowired
    private AccountBalanceUpdateService accountBalanceUpdateService;

    @Autowired
    private AccountBalanceDeleteService accountBalanceDeleteService;

    /**
     * Retrieves a paginated list of account balances for the specified account.
     *
     * @param accountId the ID of the parent account
     * @param page      the page number (zero-indexed, default: 0)
     * @param size      the page size (default: 10)
     * @return a paginated response containing AccountBalanceDTOs
     */
    @Operation(summary = "Get Account Balances", description = "Retrieves a paginated list of account balances for the specified account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account balances",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<PaginationResponse<AccountBalanceDTO>>> getAccountBalances(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);

        return accountBalanceGetService.getAccountBalances(accountId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves a paginated list of balance history for the specified account and filters by
     * balance type and date range.
     *
     * @param accountId   the ID of the parent account
     * @param balanceType the balance type to filter (e.g., CURRENT, AVAILABLE)
     * @param startDate   the start date for the history
     * @param endDate     the end date for the history
     * @param page        the page number (default: 0)
     * @param size        the page size (default: 10)
     * @return a paginated response containing AccountBalanceDTOs
     */
    @Operation(summary = "Get Balance History", description = "Retrieves a paginated list of balance history for the specified account, filtered by balance type and date range.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved balance history",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/history")
    public Mono<ResponseEntity<PaginationResponse<AccountBalanceDTO>>> getBalanceHistory(
            @PathVariable Long accountId,
            @RequestParam BalanceTypeEnum balanceType,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);

        return accountBalanceGetService.getBalanceHistory(accountId, balanceType, startDate, endDate, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a new account balance.
     *
     * @param accountId          the ID of the parent account
     * @param accountBalanceDTO  the details of the account balance to be created
     * @return the created AccountBalanceDTO
     */
    @Operation(summary = "Create Account Balance", description = "Creates a new account balance.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account balance created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountBalanceDTO>> createAccountBalance(
            @PathVariable Long accountId,
            @RequestBody(description = "Account balance details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountBalanceDTO.class)))
            AccountBalanceDTO accountBalanceDTO) {
        // You might want to ensure the DTO also references the correct accountId
        accountBalanceDTO.setAccountId(accountId);

        return accountBalanceCreateService.createAccountBalance(accountBalanceDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    /**
     * Updates an existing account balance.
     *
     * @param accountId         the ID of the parent account
     * @param accountBalanceId  the ID of the account balance to be updated
     * @param accountBalanceDTO the updated details of the account balance
     * @return the updated AccountBalanceDTO
     */
    @Operation(summary = "Update Account Balance", description = "Updates an existing account balance.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account balance updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account balance not found", content = @Content)
    })
    @PutMapping("/{accountBalanceId}")
    public Mono<ResponseEntity<AccountBalanceDTO>> updateAccountBalance(
            @PathVariable Long accountId,
            @PathVariable Long accountBalanceId,
            @RequestBody(description = "Updated account balance details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountBalanceDTO.class)))
            AccountBalanceDTO accountBalanceDTO) {
        // Validate or ensure the DTO references the same account
        accountBalanceDTO.setAccountId(accountId);

        return accountBalanceUpdateService.updateAccountBalance(accountBalanceId, accountBalanceDTO)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account balance by its ID.
     *
     * @param accountId        the ID of the parent account
     * @param accountBalanceId the ID of the account balance to be deleted
     * @return a ResponseEntity indicating the operation's result
     */
    @Operation(summary = "Delete Account Balance", description = "Deletes an account balance by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account balance deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account balance not found", content = @Content)
    })
    @DeleteMapping("/{accountBalanceId}")
    public Mono<ResponseEntity<Void>> deleteAccountBalance(
            @PathVariable Long accountId,
            @PathVariable Long accountBalanceId) {
        // Optionally confirm that the balance belongs to the specified account
        return accountBalanceDeleteService.deleteAccountBalance(accountBalanceId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}