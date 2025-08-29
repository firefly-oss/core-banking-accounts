package com.firefly.core.banking.accounts.web.controllers.provider.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.core.services.provider.v1.AccountProviderServiceImpl;
import com.firefly.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Account Providers", description = "APIs for managing provider records associated with a specific account")
@RestController
@RequestMapping("/api/v1/accounts/{accountId}/providers")
public class AccountProviderController {

    @Autowired
    private AccountProviderServiceImpl service;

    @Operation(
            summary = "List Account Providers",
            description = "Retrieve a paginated list of provider records associated with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the provider records",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No providers found for the specified account",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountProviderDTO>>> getAllProviders(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listProviders(accountId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Account Provider",
            description = "Create a new provider record and associate it with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provider created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountProviderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid provider data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountProviderDTO>> createProvider(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Data for the new account provider record", required = true,
                    schema = @Schema(implementation = AccountProviderDTO.class))
            @RequestBody AccountProviderDTO providerDTO
    ) {
        return service.createProvider(accountId, providerDTO)
                .map(createdProvider -> ResponseEntity.status(201).body(createdProvider))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Account Provider by ID",
            description = "Retrieve a specific provider record by its unique identifier, ensuring it belongs to the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the account provider",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider record not found",
                    content = @Content)
    })
    @GetMapping(value = "/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountProviderDTO>> getProvider(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the provider record", required = true)
            @PathVariable Long providerId
    ) {
        return service.getProvider(accountId, providerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Account Provider",
            description = "Update an existing provider record associated with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider record not found",
                    content = @Content)
    })
    @PutMapping(value = "/{providerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountProviderDTO>> updateProvider(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the provider record to update", required = true)
            @PathVariable Long providerId,

            @Parameter(description = "Updated provider data", required = true,
                    schema = @Schema(implementation = AccountProviderDTO.class))
            @RequestBody AccountProviderDTO providerDTO
    ) {
        return service.updateProvider(accountId, providerId, providerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Account Provider",
            description = "Remove an existing provider record from the specified account by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provider deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Provider record not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{providerId}")
    public Mono<ResponseEntity<Void>> deleteProvider(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the provider record to delete", required = true)
            @PathVariable Long providerId
    ) {
        return service.deleteProvider(accountId, providerId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
