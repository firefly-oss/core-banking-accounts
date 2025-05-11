package com.catalis.core.banking.accounts.interfaces.enums.core.v1;

/**
 * Defines the types of accounts supported by the system.
 * Each type has different characteristics, rules, and parameters.
 */
public enum AccountTypeEnum {
    /**
     * Standard checking account for everyday transactions
     */
    CHECKING,
    
    /**
     * Interest-bearing account for saving money
     */
    SAVINGS,
    
    /**
     * Account for fixed-term deposits with higher interest rates
     */
    TERM_DEPOSIT,
    
    /**
     * Credit account for loans
     */
    LOAN,
    
    /**
     * Account for credit card transactions
     */
    CREDIT_CARD,
    
    /**
     * Investment account for securities
     */
    INVESTMENT,
    
    /**
     * Account for mortgage loans
     */
    MORTGAGE,
    
    /**
     * Business checking account
     */
    BUSINESS_CHECKING,
    
    /**
     * Business savings account
     */
    BUSINESS_SAVINGS,
    
    /**
     * Account for foreign currency
     */
    FOREIGN_CURRENCY,
    
    /**
     * Account with special features for high-value customers
     */
    PREMIUM,
    
    /**
     * Account for youth or students
     */
    YOUTH,
    
    /**
     * Account for senior citizens
     */
    SENIOR,
    
    /**
     * Account for pension payments
     */
    PENSION,
    
    /**
     * Account for government benefits
     */
    GOVERNMENT_BENEFITS
}