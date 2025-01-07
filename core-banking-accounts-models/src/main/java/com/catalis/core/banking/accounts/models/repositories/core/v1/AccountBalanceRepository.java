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

    @Query("SELECT * FROM account_balance WHERE account_id = :accountId AND balance_type = :balanceType AND " +
            "as_of_datetime BETWEEN :startDate AND :endDate OFFSET :#{#pageable.offset} LIMIT :#{#pageable.pageSize}")
    Flux<AccountBalance> findBalanceHistory(Long accountId, BalanceTypeEnum balanceType, LocalDateTime startDate,
                                            LocalDateTime endDate, Pageable pageable);

    @Query("SELECT COUNT(*) FROM account_balance WHERE account_id = :accountId AND balance_type = :balanceType AND " +
            "as_of_datetime BETWEEN :startDate AND :endDate")
    Mono<Long> countBalanceHistory(Long accountId, BalanceTypeEnum balanceType, LocalDateTime startDate,
                                   LocalDateTime endDate);

    @Query("SELECT * FROM account_balance WHERE account_id = :accountId AND balance_type = 'CURRENT' AND " +
            "as_of_datetime <= CURRENT_TIMESTAMP ORDER BY as_of_datetime DESC LIMIT 1")
    Mono<AccountBalance> findCurrentBalance(Long accountId);

}