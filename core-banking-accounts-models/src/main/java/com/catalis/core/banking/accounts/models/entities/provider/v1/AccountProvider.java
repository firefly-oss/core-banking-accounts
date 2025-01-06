package com.catalis.core.banking.accounts.models.entities.provider.v1;

import com.catalis.core.banking.accounts.interfaces.enums.models.provider.v1.ProviderStatusEnum;
import com.catalis.core.banking.accounts.models.entities.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Manages the relationship between accounts and external Banking-as-a-Service (BaaS) providers.
 * Tracks the status and external references for accounts linked to third-party providers.
 *
 * Business Rules:
 * - Each account can have multiple providers
 * - External references must be unique per provider
 * - Status changes must be tracked for reconciliation
 */
@Table("account_provider")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountProvider extends BaseEntity {

    /**
     * Unique identifier for the provider record
     * Example: 1000001
     */
    @Id
    private Long accountProviderId;

    /**
     * Reference to the associated account
     * Example: 100001 (links to Account.accountId)
     */
    private Long accountId;

    /**
     * Name of the BaaS provider
     * Examples:
     * - "ClearBank"
     * - "Banking Circle"
     * - "Modulr"
     */
    private String providerName;

    /**
     * External reference/ID used by the provider
     * Examples:
     * - "CB-ACC-123456" (ClearBank reference)
     * - "BC-1234567890" (Banking Circle reference)
     * - "MOD-ACC-001" (Modulr reference)
     */
    private String externalReference;

    /**
     * Current status of the provider connection
     * Examples with context:
     * - ACTIVE: Fully operational
     * - INACTIVE: Temporarily disabled
     * - PENDING: Awaiting provider setup
     * - SUSPENDED: Provider issues
     */
    private ProviderStatusEnum status;
}
