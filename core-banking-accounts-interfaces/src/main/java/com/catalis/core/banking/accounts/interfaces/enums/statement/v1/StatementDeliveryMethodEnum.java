package com.catalis.core.banking.accounts.interfaces.enums.statement.v1;

/**
 * Defines the methods by which account statements are delivered to customers.
 * Customers may choose different delivery methods based on preferences.
 */
public enum StatementDeliveryMethodEnum {
    /**
     * Statements delivered by postal mail
     */
    MAIL,
    
    /**
     * Statements delivered by email
     */
    EMAIL,
    
    /**
     * Statements available online through digital banking
     */
    ONLINE,
    
    /**
     * Statements delivered both by mail and available online
     */
    MAIL_AND_ONLINE,
    
    /**
     * Statements delivered both by email and available online
     */
    EMAIL_AND_ONLINE,
    
    /**
     * Statements delivered by all methods (mail, email, online)
     */
    ALL_METHODS,
    
    /**
     * Statements held at branch for pickup
     */
    BRANCH_PICKUP,
    
    /**
     * No statements delivered (paperless, view only online)
     */
    NONE
}
