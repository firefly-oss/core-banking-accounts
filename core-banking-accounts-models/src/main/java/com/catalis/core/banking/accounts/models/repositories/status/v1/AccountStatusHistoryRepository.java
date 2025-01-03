package com.catalis.core.banking.accounts.models.repositories.status.v1;

import com.catalis.core.banking.accounts.models.entities.status.v1.AccountStatusHistory;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AccountStatusHistoryRepository extends BaseRepository<AccountStatusHistory, Long> {
    Flux<AccountStatusHistory> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);

    @Query("SELECT * FROM account_status_history WHERE account_id = :accountId AND" +
            " status_start_datetime >= :startDate")
    Flux<AccountStatusHistory> findStatusChangesSince(Long accountId, LocalDateTime startDate);

    @Query("SELECT COUNT(*) FROM account_status_history WHERE account_id = :accountId AND " +
            "status_start_datetime >= :startDate")
    Mono<Long> countStatusChangesSince(Long accountId, LocalDateTime startDate);
}
