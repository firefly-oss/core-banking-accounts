package com.firefly.core.banking.accounts.models.repositories.space.v1;

import com.firefly.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.firefly.core.banking.accounts.models.entities.space.v1.AccountSpace;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountSpaceRepository extends BaseRepository<AccountSpace, UUID> {
    /**
     * Find all spaces for a specific account
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountId(UUID accountId);

    /**
     * Find all spaces for a specific account with pagination
     * @param accountId the account ID
     * @param pageable pagination information
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountId(UUID accountId, Pageable pageable);

    /**
     * Count the number of spaces for a specific account
     * @param accountId the account ID
     * @return a Mono with the count
     */
    Mono<Long> countByAccountId(UUID accountId);

    /**
     * Find all spaces of a specific type for an account
     * @param accountId the account ID
     * @param spaceType the space type
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountIdAndSpaceType(UUID accountId, AccountSpaceTypeEnum spaceType);

    /**
     * Find all spaces with goals (target amount is not null)
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities
     */
    Flux<AccountSpace> findByAccountIdAndTargetAmountIsNotNull(UUID accountId);

    /**
     * Find all spaces with upcoming target dates
     * @param accountId the account ID
     * @param currentDate the current date
     * @return a Flux of AccountSpace entities with target dates after the current date
     */
    Flux<AccountSpace> findByAccountIdAndTargetDateGreaterThanEqual(UUID accountId, LocalDateTime currentDate);

    /**
     * Find all spaces with target dates that have passed
     * @param accountId the account ID
     * @param currentDate the current date
     * @return a Flux of AccountSpace entities with target dates before the current date
     */
    Flux<AccountSpace> findByAccountIdAndTargetDateLessThan(UUID accountId, LocalDateTime currentDate);

    /**
     * Calculate the total balance across all spaces for an account
     * @param accountId the account ID
     * @return a Mono with the total balance
     */
    @Query("SELECT SUM(balance) FROM account_space WHERE account_id = :accountId")
    Mono<java.math.BigDecimal> calculateTotalBalance(UUID accountId);

    /**
     * Find all frozen spaces for an account
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities that are frozen
     */
    Flux<AccountSpace> findByAccountIdAndIsFrozenTrue(UUID accountId);

    /**
     * Find all unfrozen spaces for an account
     * @param accountId the account ID
     * @return a Flux of AccountSpace entities that are not frozen
     */
    Flux<AccountSpace> findByAccountIdAndIsFrozenFalse(UUID accountId);

    /**
     * Count the number of frozen spaces for an account
     * @param accountId the account ID
     * @return a Mono with the count of frozen spaces
     */
    Mono<Long> countByAccountIdAndIsFrozenTrue(UUID accountId);

    /**
     * Find spaces that were frozen after a specific date
     * @param accountId the account ID
     * @param dateTime the date/time threshold
     * @return a Flux of AccountSpace entities frozen after the specified date/time
     */
    Flux<AccountSpace> findByAccountIdAndFrozenDateTimeGreaterThanEqual(UUID accountId, LocalDateTime dateTime);
}
