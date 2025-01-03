package com.catalis.core.banking.accounts.web.controllers.provider.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.provider.v1.AccountProviderCreateService;
import com.catalis.core.banking.accounts.core.services.provider.v1.AccountProviderDeleteService;
import com.catalis.core.banking.accounts.core.services.provider.v1.AccountProviderGetService;
import com.catalis.core.banking.accounts.core.services.provider.v1.AccountProviderUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
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
@RequestMapping("/api/v1/account-providers")
@Tag(name = "Account Provider Management API", description = "Endpoints for managing account providers.")
public class AccountProviderController {

    @Autowired
    private AccountProviderGetService accountProviderGetService;

    @Autowired
    private AccountProviderCreateService accountProviderCreateService;

    @Autowired
    private AccountProviderUpdateService accountProviderUpdateService;

    @Autowired
    private AccountProviderDeleteService accountProviderDeleteService;

    /**
     * Retrieves a paginated list of account providers for a specific account ID.
     *
     * @param accountId the account ID to retrieve providers for
     * @param page      the page number (default: 0)
     * @param size      the page size (default: 10)
     * @return a paginated list of account providers
     */
    @Operation(summary = "Get Account Providers", description = "Retrieve a list of account providers for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account providers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<PaginationResponse<AccountProviderDTO>>> getAccountProviders(
            @PathVariable("accountId") Long accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);

        return accountProviderGetService.getAccountProviders(accountId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves a paginated list of active providers filtered by provider name.
     *
     * @param providerName the provider name to filter active providers
     * @param page         the page number (default: 0)
     * @param size         the page size (default: 10)
     * @return a paginated list of active providers
     */
    @Operation(summary = "Get Active Providers", description = "Retrieve a list of active account providers filtered by provider name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active providers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No active providers found", content = @Content)
    })
    @GetMapping("/active")
    public Mono<ResponseEntity<PaginationResponse<AccountProviderDTO>>> getActiveProvidersByName(
            @RequestParam(name = "providerName") String providerName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size);

        return accountProviderGetService.getActiveProvidersByName(providerName, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a new account provider.
     *
     * @param accountProvider the account provider details
     * @return the created account provider
     */
    @Operation(summary = "Create Account Provider", description = "Creates a new account provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account provider created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountProviderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountProviderDTO>> createAccountProvider(
            @RequestBody(description = "Account provider details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountProviderDTO.class))) AccountProviderDTO accountProvider) {
        return accountProviderCreateService.createAccountProvider(accountProvider)
                .map(createdProvider -> ResponseEntity.status(201).body(createdProvider));
    }

    /**
     * Updates an existing account provider.
     *
     * @param accountProviderId the ID of the account provider to be updated
     * @param updatedProvider   the updated account provider details
     * @return the updated account provider
     */
    @Operation(summary = "Update Account Provider", description = "Updates an existing account provider.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated account provider",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account provider not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PutMapping("/{accountProviderId}")
    public Mono<ResponseEntity<AccountProviderDTO>> updateAccountProvider(
            @PathVariable("accountProviderId") Long accountProviderId,
            @RequestBody(description = "Updated account provider details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountProviderDTO.class))) AccountProviderDTO updatedProvider) {
        return accountProviderUpdateService.updateAccountProvider(accountProviderId, updatedProvider)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account provider by its ID.
     *
     * @param accountProviderId the ID of the account provider to delete
     * @return a response indicating successful deletion
     */
    @Operation(summary = "Delete Account Provider", description = "Deletes an account provider by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account provider deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account provider not found", content = @Content)
    })
    @DeleteMapping("/{accountProviderId}")
    public Mono<ResponseEntity<Void>> deleteAccountProvider(@PathVariable("accountProviderId") Long accountProviderId) {
        return accountProviderDeleteService.deleteAccountProvider(accountProviderId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}