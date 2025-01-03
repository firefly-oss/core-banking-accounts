package com.catalis.core.banking.accounts.web.controllers.parameter.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.parameter.v1.AccountParameterCreateService;
import com.catalis.core.banking.accounts.core.services.parameter.v1.AccountParameterDeleteService;
import com.catalis.core.banking.accounts.core.services.parameter.v1.AccountParameterGetService;
import com.catalis.core.banking.accounts.core.services.parameter.v1.AccountParameterUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.interfaces.enums.parameter.v1.ParamTypeEnum;
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

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/parameters")
@Tag(name = "Account Management API - Parameters", description = "Endpoints for account parameters.")
public class AccountParameterController {

    @Autowired
    private AccountParameterGetService accountParameterGetService;

    @Autowired
    private AccountParameterCreateService accountParameterCreateService;

    @Autowired
    private AccountParameterUpdateService accountParameterUpdateService;

    @Autowired
    private AccountParameterDeleteService accountParameterDeleteService;

    /**
     * Retrieves a paginated list of account parameters for a specific account.
     */
    @Operation(summary = "Get Account Parameters", description = "Retrieves a paginated list of account parameters for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<PaginationResponse<AccountParameterDTO>>> getAccountParameters(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginationRequest paginationRequest = new PaginationRequest(page, size);
        return accountParameterGetService.getAccountParameters(accountId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves the current parameter for a specific account and parameter type.
     */
    @Operation(summary = "Get Current Parameter", description = "Fetches the current valid parameter for an account and its type.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current account parameter",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Parameter not found", content = @Content)
    })
    @GetMapping("/current")
    public Mono<ResponseEntity<AccountParameterDTO>> getCurrentParameter(
            @PathVariable Long accountId,
            @RequestParam ParamTypeEnum paramType) {
        return accountParameterGetService.getCurrentParameter(accountId, paramType)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account parameter for a specific account.
     */
    @Operation(summary = "Create Account Parameter", description = "Creates a new account parameter associated with an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account parameter created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountParameterDTO>> createAccountParameter(
            @PathVariable Long accountId,
            @RequestBody(description = "Account parameter details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountParameterDTO.class)))
            AccountParameterDTO accountParameter) {

        accountParameter.setAccountId(accountId);
        return accountParameterCreateService.createAccountParameter(accountParameter)
                .map(param -> ResponseEntity.status(201).body(param));
    }

    /**
     * Updates an existing account parameter for a specific account.
     */
    @Operation(summary = "Update Account Parameter", description = "Updates an existing account parameter for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated account parameter",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account parameter not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PutMapping("/{accountParameterId}")
    public Mono<ResponseEntity<AccountParameterDTO>> updateAccountParameter(
            @PathVariable Long accountId,
            @PathVariable Long accountParameterId,
            @RequestBody(description = "Updated parameter details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountParameterDTO.class)))
            AccountParameterDTO accountParameter) {

        accountParameter.setAccountId(accountId);
        return accountParameterUpdateService.updateAccountParameter(accountParameterId, accountParameter)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account parameter by its ID for a specific account.
     */
    @Operation(summary = "Delete Account Parameter", description = "Deletes an account parameter by its ID for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account parameter deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account parameter not found", content = @Content)
    })
    @DeleteMapping("/{accountParameterId}")
    public Mono<ResponseEntity<Void>> deleteAccountParameter(
            @PathVariable Long accountId,
            @PathVariable Long accountParameterId) {
        // Verify or check if it belongs to that account if needed
        return accountParameterDeleteService.deleteAccountParameter(accountParameterId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}