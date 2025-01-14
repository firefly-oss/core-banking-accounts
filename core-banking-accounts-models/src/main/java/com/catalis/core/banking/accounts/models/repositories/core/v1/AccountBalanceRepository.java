package com.catalis.core.banking.accounts.models.repositories.core.v1;

import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.catalis.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AccountBalanceRepository extends BaseRepository<AccountBalance, Long> {
    Flux<AccountBalance> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);
}