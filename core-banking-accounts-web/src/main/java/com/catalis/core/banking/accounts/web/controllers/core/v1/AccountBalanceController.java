package com.catalis.core.banking.accounts.web.controllers.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.models.core.v1.AccountBalanceCreateService;
import com.catalis.core.banking.accounts.core.services.models.core.v1.AccountBalanceDeleteService;
import com.catalis.core.banking.accounts.core.services.models.core.v1.AccountBalanceGetService;
import com.catalis.core.banking.accounts.core.services.models.core.v1.AccountBalanceUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/balances")
@Tag(name = "Account Management API - Balances", description = "Endpoints for account balances.")
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
     * Retrieves a paginated list of account balances for a specific account.
     */
    @Operation(summary = "Get Account Balances", description = "Retrieves a paginated list of account balances for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account balances",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<PaginationResponse<AccountBalanceDTO>>> getAccountBalances(
            @Parameter(name = "accountId", description = "ID of the account")
            @PathVariable("accountId") Long accountId,
            @ParameterObject @ModelAttribute PaginationRequest paginationRequest) {

        return accountBalanceGetService.getAccountBalances(accountId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves the current balance for a specific account.
     */
    @Operation(summary = "Get Current Balance", description = "Fetches the current balance for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current balance",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Balance not found", content = @Content)
    })
    @GetMapping("/current")
    public Mono<ResponseEntity<AccountBalanceDTO>> getCurrentBalance(
            @Parameter(name = "accountId", description = "ID of the account")
            @PathVariable("accountId") Long accountId) {
        return accountBalanceGetService.getCurrentAccountBalance(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account balance entry for a specific account.
     */
    @Operation(summary = "Create Account Balance", description = "Creates a new account balance entry associated with an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account balance created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountBalanceDTO>> createAccountBalance(
            @Parameter(name = "accountId", description = "ID of the account")
            @PathVariable("accountId") Long accountId,
            @Parameter(name = "accountBalance", description = "Account balance details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountBalanceDTO.class)))
            @RequestBody AccountBalanceDTO accountBalanceDTO) {

        accountBalanceDTO.setAccountId(accountId); // Ensure accountId is associated with the balance
        return accountBalanceCreateService.createAccountBalance(accountBalanceDTO)
                .map(created -> ResponseEntity.status(201).body(created));
    }

    /**
     * Updates an existing account balance.
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
            @Parameter(name = "accountId", description = "ID of the account")
            @PathVariable("accountId") Long accountId,
            @Parameter(name = "accountBalanceId", description = "ID of the account balance to update")
            @PathVariable("accountBalanceId") Long accountBalanceId,
            @Parameter(name = "accountBalance", description = "Updated account balance details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountBalanceDTO.class)))
            @RequestBody AccountBalanceDTO accountBalanceDTO) {

        accountBalanceDTO.setAccountId(accountId); // Ensure accountId is validated during the update

        return accountBalanceUpdateService.updateAccountBalance(accountBalanceId, accountBalanceDTO)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account balance by its ID for a specific account.
     */
    @Operation(summary = "Delete Account Balance", description = "Deletes an account balance by its ID for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account balance deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account balance not found", content = @Content)
    })
    @DeleteMapping("/{accountBalanceId}")
    public Mono<ResponseEntity<Void>> deleteAccountBalance(
            @Parameter(name = "accountId", description = "ID of the account")
            @PathVariable("accountId") Long accountId,
            @Parameter(name = "accountBalanceId", description = "ID of the account balance to delete")
            @PathVariable("accountBalanceId") Long accountBalanceId) {
        // Optionally verify that the balance belongs to the accountId
        return accountBalanceDeleteService.deleteAccountBalance(accountBalanceId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}