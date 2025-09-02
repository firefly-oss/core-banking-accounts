package com.firefly.core.banking.accounts.models.repositories.parameter.v1;

import com.firefly.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountParameterRepository extends BaseRepository<AccountParameter, UUID> {
    Flux<AccountParameter> findByAccountId(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountId(UUID accountId);
}
