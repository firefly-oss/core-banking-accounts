package com.firefly.core.banking.accounts.models.repositories.core.v1;

import com.firefly.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountBalanceRepository extends BaseRepository<AccountBalance, Long> {
    // Find all balances for an account (both global and space-specific)
    Flux<AccountBalance> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);

    // Find global balances for an account (where accountSpaceId is null)
    Flux<AccountBalance> findByAccountIdAndAccountSpaceIdIsNull(Long accountId, Pageable pageable);
    Mono<Long> countByAccountIdAndAccountSpaceIdIsNull(Long accountId);

    // Find balances for a specific account space
    Flux<AccountBalance> findByAccountIdAndAccountSpaceId(Long accountId, Long accountSpaceId, Pageable pageable);
    Mono<Long> countByAccountIdAndAccountSpaceId(Long accountId, Long accountSpaceId);
}
