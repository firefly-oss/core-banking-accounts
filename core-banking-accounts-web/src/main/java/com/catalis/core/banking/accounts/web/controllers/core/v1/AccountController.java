package com.catalis.core.banking.accounts.web.controllers.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountCreateService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountDeleteService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountGetService;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account Management API", description = "Endpoints for account CRUD operations and retrieval.")
public class AccountController {

    @Autowired
    private AccountGetService accountGetService;

    @Autowired
    private AccountCreateService accountCreateService;

    @Autowired
    private AccountUpdateService accountUpdateService;

    @Autowired
    private AccountDeleteService accountDeleteService;

    /**
     * Retrieves a paginated list of accounts by branchId.
     * <p>
     * Example:
     * GET /api/v1/accounts?branchId=123&page=0&size=10
     *
     * @param branchId optional branchId for filtering
     * @return paginated list of accounts
     */
    @Operation(summary = "Get Account", description = "Retrieves a paginated list of accounts filtering by branch ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved accounts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping
    public Mono<ResponseEntity<PaginationResponse<AccountDTO>>> getAccounts(
            @RequestParam(required = false) Long branchId,
            @ParameterObject PaginationRequest paginationRequest) {

        return accountGetService.getAccountsByBranchId(branchId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account
     * @return the account details if found
     */
    @Operation(summary = "Get Account by ID", description = "Retrieves an account by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<AccountDTO>> getAccountById(@PathVariable Long accountId) {
        return accountGetService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Retrieves account information by account number.
     *
     * @param accountNumber the account number to search for
     * @return the account details if found
     */
    @Operation(summary = "Get Account by Account Number", description = "Retrieves account information based on account number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/by-account-number/{accountNumber}")
    public Mono<ResponseEntity<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber) {
        return accountGetService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves account information by contract ID.
     *
     * @param contractId the contract ID linked to the account
     * @return the account details if found
     */
    @Operation(summary = "Get Account by Contract ID", description = "Retrieves account information by contract ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/by-contract-id/{contractId}")
    public Mono<ResponseEntity<AccountDTO>> getAccountByContractId(@PathVariable Long contractId) {
        return accountGetService.getAccountByContractId(contractId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account.
     *
     * @param accountDTO the details of the account to be created
     * @return the created account details
     */
    @Operation(summary = "Create an Account", description = "Creates a new account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountDTO>> createAccount(
            @RequestBody(description = "Details of the account to be created", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)))
            AccountDTO accountDTO) {
        return accountCreateService.createAccount(accountDTO)
                .map(account -> ResponseEntity.status(201).body(account));
    }

    /**
     * Updates an existing account.
     *
     * @param accountId  the ID of the account to be updated
     * @param accountDTO the updated account details
     * @return the updated account details
     */
    @Operation(summary = "Update an Account", description = "Updates an existing account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PutMapping("/{accountId}")
    public Mono<ResponseEntity<AccountDTO>> updateAccount(
            @PathVariable Long accountId,
            @RequestBody(description = "Updated account details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class)))
            AccountDTO accountDTO) {
        return accountUpdateService.updateAccount(accountId, accountDTO)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account by its ID.
     *
     * @param accountId the ID of the account to be deleted
     * @return a response indicating the deletion status
     */
    @Operation(summary = "Delete an Account", description = "Deletes an account by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @DeleteMapping("/{accountId}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable Long accountId) {
        return accountDeleteService.deleteAccount(accountId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}