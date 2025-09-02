package com.firefly.core.banking.accounts.models.repositories.status.v1;

import com.firefly.core.banking.accounts.models.entities.status.v1.AccountStatusHistory;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountStatusHistoryRepository extends BaseRepository<AccountStatusHistory, UUID> {
    Flux<AccountStatusHistory> findByAccountId(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountId(UUID accountId);
}
