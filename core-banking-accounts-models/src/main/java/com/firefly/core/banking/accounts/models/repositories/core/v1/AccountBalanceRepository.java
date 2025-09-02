package com.firefly.core.banking.accounts.models.repositories.core.v1;

import com.firefly.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountBalanceRepository extends BaseRepository<AccountBalance, UUID> {
    // Find all balances for an account (both global and space-specific)
    Flux<AccountBalance> findByAccountId(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountId(UUID accountId);

    // Find global balances for an account (where accountSpaceId is null)
    Flux<AccountBalance> findByAccountIdAndAccountSpaceIdIsNull(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountIdAndAccountSpaceIdIsNull(UUID accountId);

    // Find balances for a specific account space
    Flux<AccountBalance> findByAccountIdAndAccountSpaceId(UUID accountId, UUID accountSpaceId, Pageable pageable);
    Mono<Long> countByAccountIdAndAccountSpaceId(UUID accountId, UUID accountSpaceId);
}
