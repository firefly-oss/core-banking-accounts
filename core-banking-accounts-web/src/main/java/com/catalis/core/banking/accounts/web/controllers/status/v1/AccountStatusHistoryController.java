package com.catalis.core.banking.accounts.web.controllers.status.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.services.status.v1.AccountStatusHistoryCreateService;
import com.catalis.core.banking.accounts.core.services.status.v1.AccountStatusHistoryDeleteService;
import com.catalis.core.banking.accounts.core.services.status.v1.AccountStatusHistoryGetService;
import com.catalis.core.banking.accounts.core.services.status.v1.AccountStatusHistoryUpdateService;
import com.catalis.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
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
@RequestMapping("/api/v1/accounts/{accountId}/status-history")
@Tag(name = "Account Management API - Status History", description = "Endpoints for account status history.")
public class AccountStatusHistoryController {

    @Autowired
    private AccountStatusHistoryGetService getService;

    @Autowired
    private AccountStatusHistoryCreateService createService;

    @Autowired
    private AccountStatusHistoryUpdateService updateService;

    @Autowired
    private AccountStatusHistoryDeleteService deleteService;

    /**
     * Retrieves a paginated list of account status history records for a specific account.
     */
    @Operation(summary = "Get Account Status History", description = "Fetches a paginated list of status history for a specific account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved status history",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<PaginationResponse<AccountStatusHistoryDTO>>> getStatusHistory(
            @Parameter(description = "ID of the account", name = "accountId")
            @PathVariable("accountId") Long accountId,
            @ParameterObject @ModelAttribute PaginationRequest paginationRequest) {

        return getService.getStatusHistory(accountId, paginationRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a new account status history entry for a specific account.
     */
    @Operation(summary = "Create Account Status History", description = "Creates a new account status history record for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created account status history",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatusHistoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<AccountStatusHistoryDTO>> createStatusHistory(
            @Parameter(description = "ID of the account", name = "accountId")
            @PathVariable("accountId") Long accountId,
            @Parameter(description = "Details of the account status history to be created", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountStatusHistoryDTO.class)), name = "request")
            @RequestBody AccountStatusHistoryDTO request) {
        request.setAccountId(accountId); // Ensure the accountId is set in the DTO
        return createService.createAccountStatusHistory(request)
                .map(record -> ResponseEntity.status(201).body(record));
    }

    /**
     * Updates an existing account status history record for a specific account.
     */
    @Operation(summary = "Update Account Status History", description = "Updates an existing account status history record for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated status history",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountStatusHistoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Status history record not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PutMapping("/{statusHistoryId}")
    public Mono<ResponseEntity<AccountStatusHistoryDTO>> updateStatusHistory(
            @Parameter(description = "ID of the account", name = "accountId")
            @PathVariable("accountId") Long accountId,
            @Parameter(description = "ID of the status history to update", name = "statusHistoryId")
            @PathVariable("statusHistoryId") Long statusHistoryId,
            @Parameter(description = "Updated details of the account status history", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountStatusHistoryDTO.class)), name = "request")
            @RequestBody AccountStatusHistoryDTO request) {
        request.setAccountId(accountId); // Ensure the accountId is validated during the update
        return updateService.updateAccountStatusHistory(statusHistoryId, request)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account status history record by its ID for a specific account.
     */
    @Operation(summary = "Delete Account Status History", description = "Deletes an account status history record for an account.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted status history", content = @Content),
            @ApiResponse(responseCode = "404", description = "Status history record not found", content = @Content)
    })
    @DeleteMapping("/{statusHistoryId}")
    public Mono<ResponseEntity<Void>> deleteStatusHistory(
            @Parameter(description = "ID of the account", name = "accountId")
            @PathVariable("accountId") Long accountId,
            @Parameter(description = "ID of the status history record to delete", name = "statusHistoryId")
            @PathVariable("statusHistoryId") Long statusHistoryId) {
        // Optionally verify that the statusHistoryId belongs to the accountId
        return deleteService.deleteAccountStatusHistory(statusHistoryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}