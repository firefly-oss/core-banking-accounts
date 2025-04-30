package com.catalis.core.banking.accounts.interfaces.enums.space.v1;

/**
 * Enum representing the frequency of automatic transfers between account spaces.
 */
public enum TransferFrequencyEnum {
    /**
     * Transfer occurs once per day
     */
    DAILY,
    
    /**
     * Transfer occurs once per week
     */
    WEEKLY,
    
    /**
     * Transfer occurs once per month
     */
    MONTHLY,
    
    /**
     * Transfer occurs once per quarter (every 3 months)
     */
    QUARTERLY,
    
    /**
     * Transfer occurs once per year
     */
    ANNUALLY
}