package com.firefly.core.banking.accounts.core.services.parameter.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountParameterService {

    /**
     * List all parameters (paginated) for a specific account.
     */
    Mono<PaginationResponse<AccountParameterDTO>> listParameters(UUID accountId, PaginationRequest paginationRequest);

    /**
     * Create a new parameter for a specific account.
     */
    Mono<AccountParameterDTO> createParameter(UUID accountId, AccountParameterDTO parameterDTO);

    /**
     * Retrieve a single parameter by its ID, ensuring it belongs to the specified account.
     */
    Mono<AccountParameterDTO> getParameter(UUID accountId, UUID paramId);

    /**
     * Update an existing parameter associated with the specified account.
     */
    Mono<AccountParameterDTO> updateParameter(UUID accountId, UUID paramId, AccountParameterDTO parameterDTO);

    /**
     * Delete a specific parameter by its ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteParameter(UUID accountId, UUID paramId);
}