package com.catalis.core.banking.accounts.interfaces.enums.statement.v1;

/**
 * Defines the frequency at which account statements are generated.
 * Different account types may have different default statement frequencies.
 */
public enum StatementFrequencyEnum {
    /**
     * Statements generated daily
     */
    DAILY,
    
    /**
     * Statements generated weekly
     */
    WEEKLY,
    
    /**
     * Statements generated bi-weekly (every two weeks)
     */
    BI_WEEKLY,
    
    /**
     * Statements generated monthly
     */
    MONTHLY,
    
    /**
     * Statements generated quarterly (every three months)
     */
    QUARTERLY,
    
    /**
     * Statements generated semi-annually (twice per year)
     */
    SEMI_ANNUALLY,
    
    /**
     * Statements generated annually (once per year)
     */
    ANNUALLY,
    
    /**
     * Statements generated on demand only
     */
    ON_DEMAND,
    
    /**
     * No statements generated
     */
    NONE
}
