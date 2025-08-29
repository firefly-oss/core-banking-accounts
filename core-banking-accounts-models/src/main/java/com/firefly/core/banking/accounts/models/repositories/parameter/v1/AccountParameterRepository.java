package com.firefly.core.banking.accounts.models.repositories.parameter.v1;

import com.firefly.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountParameterRepository extends BaseRepository<AccountParameter, Long> {
    Flux<AccountParameter> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);
}
