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
}
