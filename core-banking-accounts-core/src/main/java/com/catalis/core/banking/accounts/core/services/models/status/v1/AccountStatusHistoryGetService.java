package com.catalis.core.banking.accounts.core.services.models.status.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.status.v1.AccountStatusHistoryMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.models.repositories.status.v1.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AccountStatusHistoryGetService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    @Autowired
    private AccountStatusHistoryMapper mapper;

    /**
     * Retrieves a paginated list of account status history records for a specific account ID.
     *
     * @param accountId         the account ID to fetch status history for
     * @param paginationRequest the pagination request containing page size and page number
     * @return a Mono emitting a PaginationResponse containing AccountStatusHistoryDTOs
     */
    public Mono<PaginationResponse<AccountStatusHistoryDTO>> getStatusHistory(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    /**
     * Counts the total number of account status history records for a specific account ID.
     *
     * @param accountId the account ID to count status history records
     * @return a Mono emitting the total count of status history records
     */
    public Mono<Long> countStatusHistory(Long accountId) {
        return repository.countByAccountId(accountId)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to count status history records for Account ID: "
                        + accountId, e)));
    }

    /**
     * Retrieves a list of account status history entries filtered by a date range.
     *
     * @param accountId the account ID whose status history is to be fetched
     * @param startDate the start date to filter status changes
     * @return a Flux emitting a stream of AccountStatusHistoryDTOs
     */
    public Flux<AccountStatusHistoryDTO> getStatusChangesSince(Long accountId, LocalDateTime startDate) {
        return repository.findStatusChangesSince(accountId, startDate)
                .map(mapper::toDTO)
                .onErrorResume(e -> Flux.error(new RuntimeException("Failed to fetch status changes for Account ID: "
                        + accountId + " since " + startDate, e)));
    }

    /**
     * Counts account status history changes filtering by a date range.
     *
     * @param accountId the account ID whose status changes are to be counted
     * @param startDate the start date to filter status changes
     * @return a Mono emitting the total count of status changes since the given start date
     */
    public Mono<Long> countStatusChangesSince(Long accountId, LocalDateTime startDate) {
        return repository.countStatusChangesSince(accountId, startDate)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to count status changes for Account ID: "
                        + accountId + " since " + startDate, e)));
    }
}