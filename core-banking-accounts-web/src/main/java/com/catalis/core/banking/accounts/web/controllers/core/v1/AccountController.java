package com.catalis.core.banking.accounts.web.controllers.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountService;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Accounts", description = "APIs for managing bank accounts within the system")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @Operation(
            summary = "Filter Accounts",
            description = "Retrieve a paginated list of all bank accounts based on filter criteria."
    )
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountDTO>>> filterAccounts(
            @RequestBody FilterRequest<AccountDTO> filterRequest
    ) {
        return service.filterAccounts(filterRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Create Account",
            description = "Create a new bank account with the specified details."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountDTO>> createAccount(
            @Parameter(description = "Data for the new account", required = true,
                    schema = @Schema(implementation = AccountDTO.class))
            @RequestBody AccountDTO accountDTO
    ) {
        return service.createAccount(accountDTO)
                .map(createdAccount -> ResponseEntity.status(201).body(createdAccount))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Account by ID",
            description = "Retrieve an existing bank account by its unique identifier."
    )
    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountDTO>> getAccount(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return service.getAccount(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Account",
            description = "Update the details of an existing bank account by its unique identifier."
    )
    @PutMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountDTO>> updateAccount(
            @Parameter(description = "Unique identifier of the account to update", required = true)
            @PathVariable("accountId") Long accountId,

            @Parameter(description = "Updated account data", required = true,
                    schema = @Schema(implementation = AccountDTO.class))
            @RequestBody AccountDTO accountDTO
    ) {
        return service.updateAccount(accountId, accountDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Account",
            description = "Delete an existing bank account by its unique identifier."
    )
    @DeleteMapping(value = "/{accountId}")
    public Mono<ResponseEntity<Void>> deleteAccount(
            @Parameter(description = "Unique identifier of the account to delete", required = true)
            @PathVariable("accountId") Long accountId
    ) {
        return service.deleteAccount(accountId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}