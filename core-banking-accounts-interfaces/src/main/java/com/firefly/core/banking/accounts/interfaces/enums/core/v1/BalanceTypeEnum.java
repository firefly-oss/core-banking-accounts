package com.firefly.core.banking.accounts.interfaces.enums.core.v1;

/**
 * Defines the types of balances that can be tracked for an account.
 * Different balance types serve different purposes in accounting and operations.
 */
public enum BalanceTypeEnum {
    /**
     * The actual balance after all cleared transactions
     */
    CURRENT,
    
    /**
     * Balance available for use (current - pending - holds)
     */
    AVAILABLE,
    
    /**
     * Amount temporarily blocked/reserved
     */
    BLOCKED,
    
    /**
     * Amount staked in a staking protocol or validator
     * Used primarily for crypto accounts with staking capabilities
     */
    STAKED,
    
    /**
     * Amount locked for a specific time period or until certain conditions are met
     * Used for time-locked tokens, vesting schedules, etc.
     */
    LOCKED,
    
    /**
     * Amount pending blockchain confirmation
     * Used for tracking deposits/withdrawals that are in-flight on the blockchain
     */
    PENDING_CONFIRMATION
}
