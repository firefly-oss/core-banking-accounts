package com.catalis.core.banking.accounts.core.services.restriction.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.restriction.v1.AccountRestrictionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRestrictionService {
    
    /**
     * Create a new account restriction
     * @param accountRestrictionDTO The account restriction DTO
     * @return Mono of AccountRestrictionDTO
     */
    Mono<AccountRestrictionDTO> createAccountRestriction(AccountRestrictionDTO accountRestrictionDTO);
    
    /**
     * Get an account restriction by ID
     * @param accountRestrictionId The account restriction ID
     * @return Mono of AccountRestrictionDTO
     */
    Mono<AccountRestrictionDTO> getAccountRestriction(Long accountRestrictionId);
    
    /**
     * Update an account restriction
     * @param accountRestrictionId The account restriction ID
     * @param accountRestrictionDTO The account restriction DTO
     * @return Mono of AccountRestrictionDTO
     */
    Mono<AccountRestrictionDTO> updateAccountRestriction(Long accountRestrictionId, AccountRestrictionDTO accountRestrictionDTO);
    
    /**
     * Delete an account restriction
     * @param accountRestrictionId The account restriction ID
     * @return Mono of Void
     */
    Mono<Void> deleteAccountRestriction(Long accountRestrictionId);
    
    /**
     * Get all account restrictions for an account
     * @param accountId The account ID
     * @return Flux of AccountRestrictionDTO
     */
    Flux<AccountRestrictionDTO> getAccountRestrictionsByAccountId(Long accountId);
    
    /**
     * Get all active account restrictions for an account
     * @param accountId The account ID
     * @return Flux of AccountRestrictionDTO
     */
    Flux<AccountRestrictionDTO> getActiveAccountRestrictionsByAccountId(Long accountId);
    
    /**
     * Remove a restriction (mark as inactive)
     * @param accountRestrictionId The account restriction ID
     * @param removedBy The user or system that removed the restriction
     * @return Mono of AccountRestrictionDTO
     */
    Mono<AccountRestrictionDTO> removeRestriction(Long accountRestrictionId, String removedBy);
    
    /**
     * List account restrictions with pagination and filtering
     * @param filterRequest The filter request
     * @return Mono of PaginationResponse containing AccountRestrictionDTO
     */
    Mono<PaginationResponse<AccountRestrictionDTO>> listAccountRestrictions(FilterRequest filterRequest);
}
