package com.firefly.core.banking.accounts.models.repositories.provider.v1;

import com.firefly.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountProviderRepository extends BaseRepository<AccountProvider, UUID> {
    Flux<AccountProvider> findByAccountId(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountId(UUID accountId);

    Flux<AccountProvider> findByAccountSpaceId(UUID accountSpaceId, Pageable pageable);
    Mono<Long> countByAccountSpaceId(UUID accountSpaceId);

    Flux<AccountProvider> findByAccountIdAndAccountSpaceId(UUID accountId, UUID accountSpaceId, Pageable pageable);
    Mono<Long> countByAccountIdAndAccountSpaceId(UUID accountId, UUID accountSpaceId);
}
