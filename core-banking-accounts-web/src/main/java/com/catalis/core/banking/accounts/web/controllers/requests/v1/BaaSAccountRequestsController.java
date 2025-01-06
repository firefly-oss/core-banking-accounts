package com.catalis.core.banking.accounts.web.controllers.requests.v1;

import com.catalis.common.web.error.models.ErrorResponse;
import com.catalis.core.banking.accounts.core.services.requests.account.v1.AccountProviderCreateAccountService;
import com.catalis.core.banking.accounts.core.services.requests.account.v1.AccountProviderDeleteAccountService;
import com.catalis.core.banking.accounts.core.services.requests.account.v1.AccountProviderGetAccountService;
import com.catalis.core.banking.accounts.core.services.requests.account.v1.AccountProviderUpdateAccountService;
import com.catalis.core.banking.accounts.core.services.requests.balance.v1.AccountProviderGetBalanceService;
import com.catalis.core.banking.accounts.core.services.requests.status.v1.AccountProviderGetStatusService;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/baas-accounts")
@Tag(name = "Banking-as-a-Service Account Operations API", description = "Endpoints for executing account-specific operations directly via Banking-as-a-Service (BaaS).")
public class BaaSAccountRequestsController {

    @Autowired
    private AccountProviderCreateAccountService createAccountService;

    @Autowired
    private AccountProviderDeleteAccountService deleteAccountService;

    @Autowired
    private AccountProviderGetAccountService getAccountService;

    @Autowired
    private AccountProviderUpdateAccountService updateAccountService;

    @Autowired
    private AccountProviderGetBalanceService getBalanceService;

    @Autowired
    private AccountProviderGetStatusService getStatusService;

    /**
     * Create a new account directly using BaaS.
     *
     * @param account the account details to be created
     * @return the created account details (wrapped in ResponseEntity)
     */
    @Operation(summary = "Create Account via BaaS", description = "Creates a new account directly through the Banking-as-a-Service provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid account data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public Mono<ResponseEntity<AccountDTO>> createAccount(@RequestBody AccountDTO account) {
        return createAccountService.createAccount(account)
                .map(createdAccount -> ResponseEntity.status(201).body(createdAccount));
    }

    /**
     * Delete an account directly using BaaS.
     *
     * @param accountId the ID of the account to delete
     * @return a HTTP 204 No Content response if successfully deleted (wrapped in ResponseEntity)
     */
    @Operation(summary = "Delete Account via BaaS", description = "Deletes an account directly through the Banking-as-a-Service provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{accountId}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable(name = "accountId", required = true) Long accountId) {
        return deleteAccountService.deleteAccount(accountId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    /**
     * Retrieve detailed account information directly from BaaS.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account details (wrapped in ResponseEntity)
     */
    @Operation(summary = "Get Account via BaaS", description = "Retrieves detailed account information directly from the Banking-as-a-Service provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/get/{accountId}")
    public Mono<ResponseEntity<AccountDTO>> getAccount(@PathVariable(name = "accountId", required = true) Long accountId) {
        return getAccountService.getAccount(accountId)
                .map(ResponseEntity::ok);
    }

    /**
     * Update account details directly using BaaS.
     *
     * @param accountId the ID of the account to update
     * @param account   the updated account details
     * @return the updated account details (wrapped in ResponseEntity)
     */
    @Operation(summary = "Update Account via BaaS", description = "Updates account details directly through the Banking-as-a-Service provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/update/{accountId}")
    public Mono<ResponseEntity<AccountDTO>> updateAccount(@PathVariable(name = "accountId", required = true) Long accountId, @RequestBody AccountDTO account) {
        return updateAccountService.updateAccount(accountId, account)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieve account balance directly from BaaS.
     *
     * @param accountId the ID of the account to retrieve balance for
     * @return the account balance details (wrapped in a List<AccountBalanceDTO>)
     */
    @Operation(summary = "Get Account Balance via BaaS", description = "Fetches account balance details directly from the BaaS provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account balance",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountBalanceDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/balance/{accountId}")
    public Flux<AccountBalanceDTO> getAccountBalance(@PathVariable(name = "accountId", required = true) Long accountId) {
        return getBalanceService.getAccountBalance(accountId);
    }

    /**
     * Retrieve account status history directly from BaaS.
     *
     * @param accountId the ID of the account to retrieve status history for
     * @return the account status history details (wrapped in a List<AccountStatusHistoryDTO>)
     */
    @Operation(summary = "Get Account Status History via BaaS", description = "Retrieves the status history of an account directly from the BaaS provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account status history",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountStatusHistoryDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/status-history/{accountId}")
    public Flux<AccountStatusHistoryDTO> getAccountStatusHistory(@PathVariable(name = "accountId", required = true) Long accountId) {
        return getStatusService.getAccountStatusHistory(accountId);
    }
}