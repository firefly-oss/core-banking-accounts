package com.catalis.core.banking.accounts.models.repositories.provider.v1;

import com.catalis.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountProviderRepository extends BaseRepository<AccountProvider, Long> {
    Flux<AccountProvider> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);
}