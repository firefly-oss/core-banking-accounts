package com.catalis.core.banking.accounts.interfaces.enums.regulatory.v1;

/**
 * Defines the tax reporting status of an account.
 * This affects how the account is reported to tax authorities.
 */
public enum TaxReportingStatusEnum {
    /**
     * Account is reportable for tax purposes
     */
    REPORTABLE,
    
    /**
     * Account is exempt from tax reporting
     */
    EXEMPT,
    
    /**
     * Account is subject to backup withholding
     */
    BACKUP_WITHHOLDING,
    
    /**
     * Account is subject to FATCA reporting
     */
    FATCA_REPORTABLE,
    
    /**
     * Account is subject to CRS reporting
     */
    CRS_REPORTABLE,
    
    /**
     * Account is subject to both FATCA and CRS reporting
     */
    FATCA_CRS_REPORTABLE,
    
    /**
     * Account is pending tax documentation
     */
    PENDING_DOCUMENTATION,
    
    /**
     * Account has incomplete tax information
     */
    INCOMPLETE_INFORMATION,
    
    /**
     * Account is not subject to tax reporting
     */
    NON_REPORTABLE
}
