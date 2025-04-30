package com.catalis.core.banking.accounts.models.repositories.space.v1;

import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.catalis.core.banking.accounts.models.entities.space.v1.AccountSpace;
import com.catalis.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AccountSpaceRepository extends BaseRepository<AccountSpace, Long> {
    /**
     * Find all spaces for a specific account
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountId(Long accountId);

    /**
     * Find all spaces for a specific account with pagination
     * @param accountId the account ID
     * @param pageable pagination information
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountId(Long accountId, Pageable pageable);

    /**
     * Count the number of spaces for a specific account
     * @param accountId the account ID
     * @return a Mono with the count
     */
    Mono<Long> countByAccountId(Long accountId);

    /**
     * Find all spaces of a specific type for an account
     * @param accountId the account ID
     * @param spaceType the space type
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountIdAndSpaceType(Long accountId, AccountSpaceTypeEnum spaceType);

    /**
     * Find all spaces with goals (target amount is not null)
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountIdAndTargetAmountIsNotNull(Long accountId);

    /**
     * Find all spaces with upcoming target dates
     * @param accountId the account ID
     * @param currentDate the current date
     * @return a Flux of AccountSpace entities with target dates after the current date
     */
    Flux<AccountSpace> findByAccountIdAndTargetDateGreaterThanEqual(Long accountId, LocalDateTime currentDate);

    /**
     * Find all spaces with target dates that have passed
     * @param accountId the account ID
     * @param currentDate the current date
     * @return a Flux of AccountSpace entities with target dates before the current date
     */
    Flux<AccountSpace> findByAccountIdAndTargetDateLessThan(Long accountId, LocalDateTime currentDate);

    /**
     * Calculate the total balance across all spaces for an account
     * @param accountId the account ID
     * @return a Mono with the total balance
     */
    @Query("SELECT SUM(balance) FROM account_space WHERE account_id = :accountId")
    Mono<java.math.BigDecimal> calculateTotalBalance(Long accountId);
}
