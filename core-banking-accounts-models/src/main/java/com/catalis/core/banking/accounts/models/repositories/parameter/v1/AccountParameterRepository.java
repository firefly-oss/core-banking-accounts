package com.catalis.core.banking.accounts.models.repositories.parameter.v1;

import com.catalis.core.banking.accounts.interfaces.enums.parameter.v1.ParamTypeEnum;
import com.catalis.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountParameterRepository extends BaseRepository<AccountParameter, Long> {
    Flux<AccountParameter> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);

    @Query("SELECT * FROM account_parameter WHERE account_id = :accountId AND param_type = :paramType AND " +
            "effective_date <= CURRENT_TIMESTAMP AND (expiry_date IS NULL OR expiry_date > CURRENT_TIMESTAMP)")
    Mono<AccountParameter> findCurrentParameter(Long accountId, ParamTypeEnum paramType);
}
