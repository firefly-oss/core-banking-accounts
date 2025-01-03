package com.catalis.core.banking.accounts.models.repositories.core.v1;

import com.catalis.core.banking.accounts.models.entities.core.v1.Account;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends BaseRepository<Account, Long> {
    Mono<Account> findByAccountNumber(String accountNumber);
    Mono<Account> findByContractId(Long contractId);

    Flux<Account> findByBranchId(Long branchId, Pageable pageable);
    Mono<Long> countByBranchId(Long branchId);
}
