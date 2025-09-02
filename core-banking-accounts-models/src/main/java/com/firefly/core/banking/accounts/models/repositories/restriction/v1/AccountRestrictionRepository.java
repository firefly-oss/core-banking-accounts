package com.firefly.core.banking.accounts.models.repositories.restriction.v1;

import com.firefly.core.banking.accounts.models.entities.restriction.v1.AccountRestriction;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountRestrictionRepository extends BaseRepository<AccountRestriction, UUID> {
    
    /**
     * Find all restrictions for a specific account
     * @param accountId The account ID
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByAccountId(UUID accountId);
    
    /**
     * Find all active restrictions for a specific account
     * @param accountId The account ID
     * @param isActive Whether the restriction is active
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByAccountIdAndIsActive(UUID accountId, Boolean isActive);
    
    /**
     * Count active restrictions for a specific account
     * @param accountId The account ID
     * @param isActive Whether the restriction is active
     * @return Mono of Long representing the count
     */
    Mono<Long> countByAccountIdAndIsActive(UUID accountId, Boolean isActive);
    
    /**
     * Find restrictions by reference number
     * @param referenceNumber The reference number
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByReferenceNumber(String referenceNumber);
}
