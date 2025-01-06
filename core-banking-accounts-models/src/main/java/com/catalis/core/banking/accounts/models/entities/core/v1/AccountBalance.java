package com.catalis.core.banking.accounts.models.entities.core.v1;

import com.catalis.core.banking.accounts.interfaces.enums.models.core.v1.BalanceTypeEnum;
import com.catalis.core.banking.accounts.models.entities.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the balance information for an account.
 * Tracks different types of balances (current, available, blocked) for each account.
 *
 * Business Rules:
 * - Each account can have multiple balance types
 * - Balance amounts must be stored with 4 decimal places
 * - Historical balance records should be maintained
 * - Balance updates must be timestamped
 */
@Table("account_balance")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance extends BaseEntity {

    /**
     * Unique identifier for the balance record
     * Example: 1000001
     */
    @Id
    private Long accountBalanceId;

    /**
     * Reference to the associated account
     * Example: 100001 (links to Account.accountId)
     */
    private Long accountId;

    /**
     * Type of balance being recorded
     * CURRENT: Actual balance after all cleared transactions
     * AVAILABLE: Balance available for use (current - pending - holds)
     * BLOCKED: Amount temporarily blocked/reserved
     */
    private BalanceTypeEnum balanceType;

    /**
     * The actual balance amount
     * Stored with 4 decimal places for high precision
     * Examples:
     * - 1000.0000 (One thousand)
     * - -50.5000 (Negative fifty and fifty cents)
     * - 0.0001 (Smallest unit)
     */
    private BigDecimal balanceAmount;

    /**
     * Timestamp when this balance was recorded
     * Used for:
     * - Historical tracking
     * - Reconciliation
     * - Reporting
     * Example: 2024-01-15T14:30:00.000Z
     */
    private LocalDateTime asOfDatetime;
}
