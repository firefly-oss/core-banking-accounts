package com.catalis.core.banking.accounts.core.services.status.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import reactor.core.publisher.Mono;

public interface AccountStatusHistoryService {

    /**
     * Retrieve a paginated list of status history entries for a specific account.
     */
    Mono<PaginationResponse<AccountStatusHistoryDTO>> listStatusHistory(Long accountId, PaginationRequest paginationRequest);

    /**
     * Create a new status history record for a specific account.
     */
    Mono<AccountStatusHistoryDTO> createStatusHistory(Long accountId, AccountStatusHistoryDTO historyDTO);

    /**
     * Retrieve a specific status history record by ID, ensuring it belongs to the specified account.
     */
    Mono<AccountStatusHistoryDTO> getStatusHistory(Long accountId, Long historyId);

    /**
     * Update an existing status history record associated with a specific account.
     */
    Mono<AccountStatusHistoryDTO> updateStatusHistory(Long accountId, Long historyId, AccountStatusHistoryDTO historyDTO);

    /**
     * Delete a specific status history record by its unique ID, ensuring it belongs to the specified account.
     */
    Mono<Void> deleteStatusHistory(Long accountId, Long historyId);
}
