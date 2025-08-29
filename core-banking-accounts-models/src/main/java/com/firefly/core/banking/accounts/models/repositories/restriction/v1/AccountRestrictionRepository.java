package com.firefly.core.banking.accounts.models.repositories.restriction.v1;

import com.firefly.core.banking.accounts.models.entities.restriction.v1.AccountRestriction;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRestrictionRepository extends BaseRepository<AccountRestriction, Long> {
    
    /**
     * Find all restrictions for a specific account
     * @param accountId The account ID
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByAccountId(Long accountId);
    
    /**
     * Find all active restrictions for a specific account
     * @param accountId The account ID
     * @param isActive Whether the restriction is active
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByAccountIdAndIsActive(Long accountId, Boolean isActive);
    
    /**
     * Count active restrictions for a specific account
     * @param accountId The account ID
     * @param isActive Whether the restriction is active
     * @return Mono of Long representing the count
     */
    Mono<Long> countByAccountIdAndIsActive(Long accountId, Boolean isActive);
    
    /**
     * Find restrictions by reference number
     * @param referenceNumber The reference number
     * @return Flux of AccountRestriction
     */
    Flux<AccountRestriction> findByReferenceNumber(String referenceNumber);
}
