package com.firefly.core.banking.accounts.core.services.status.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountStatusHistoryService {

    /**
     * Retrieve a paginated list of status history entries for a specific account.
     */
    Mono<PaginationResponse<AccountStatusHistoryDTO>> listStatusHistory(UUID accountId, PaginationRequest paginationRequest);

    /**
     * Create a new status history record for a specific account.
     */
    Mono<AccountStatusHistoryDTO> createStatusHistory(UUID accountId, AccountStatusHistoryDTO historyDTO);

    /**
     * Retrieve a specific status history record by ID, ensuring it belongs to the specified account.
     */
    Mono<AccountStatusHistoryDTO> getStatusHistory(UUID accountId, UUID historyId);

    /**
     * Update an existing status history record associated with a specific account.
     */
    Mono<AccountStatusHistoryDTO> updateStatusHistory(UUID accountId, UUID historyId, AccountStatusHistoryDTO historyDTO);

    /**
     * Delete a specific status history record by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteStatusHistory(UUID accountId, UUID historyId);
}
