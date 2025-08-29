package com.firefly.core.banking.accounts.web.controllers.parameter.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.core.services.parameter.v1.AccountParameterServiceImpl;
import com.firefly.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
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

@Tag(name = "Account Parameters", description = "APIs for managing parameter records associated with a specific account")
@RestController
@RequestMapping("/api/v1/accounts/{accountId}/parameters")
public class AccountParameterController {

    @Autowired
    private AccountParameterServiceImpl service;

    @Operation(
            summary = "List Account Parameters",
            description = "Retrieve a paginated list of all parameter records associated with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No parameter records found for the specified account",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PaginationResponse<AccountParameterDTO>>> getAllParameters(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @ParameterObject
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        return service.listParameters(accountId, paginationRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create Account Parameter",
            description = "Create a new parameter record and associate it with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parameter created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter data provided",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountParameterDTO>> createParameter(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Data for the new account parameter", required = true,
                    schema = @Schema(implementation = AccountParameterDTO.class))
            @RequestBody AccountParameterDTO parameterDTO
    ) {
        return service.createParameter(accountId, parameterDTO)
                .map(createdParam -> ResponseEntity.status(201).body(createdParam))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get Account Parameter by ID",
            description = "Retrieve a specific parameter record by its unique identifier, ensuring it belongs to the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the account parameter",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account parameter not found",
                    content = @Content)
    })
    @GetMapping(value = "/{paramId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountParameterDTO>> getParameter(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the parameter record", required = true)
            @PathVariable Long paramId
    ) {
        return service.getParameter(accountId, paramId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update Account Parameter",
            description = "Update an existing parameter record associated with the specified account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account parameter updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account parameter not found",
                    content = @Content)
    })
    @PutMapping(value = "/{paramId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AccountParameterDTO>> updateParameter(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the parameter record to update", required = true)
            @PathVariable Long paramId,

            @Parameter(description = "Updated parameter data", required = true,
                    schema = @Schema(implementation = AccountParameterDTO.class))
            @RequestBody AccountParameterDTO parameterDTO
    ) {
        return service.updateParameter(accountId, paramId, parameterDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete Account Parameter",
            description = "Remove an existing parameter record by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account parameter deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account parameter not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{paramId}")
    public Mono<ResponseEntity<Void>> deleteParameter(
            @Parameter(description = "Unique identifier of the account", required = true)
            @PathVariable Long accountId,

            @Parameter(description = "Unique identifier of the parameter record to delete", required = true)
            @PathVariable Long paramId
    ) {
        return service.deleteParameter(accountId, paramId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}