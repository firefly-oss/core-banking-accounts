package com.firefly.core.banking.accounts.models.entities.space.v1;

import com.firefly.core.banking.accounts.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a transaction that affects the balance of an account space.
 * This entity tracks all financial movements within a space.
 *
 * Business Rules:
 * - Each transaction must be linked to an account space
 * - Transactions can be deposits (positive amount) or withdrawals (negative amount)
 * - Each transaction must update the balance of the space
 * - Transactions must be timestamped
 * - Transactions can have a reference ID for cross-system tracking
 */
@Table("space_transaction")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaceTransaction extends BaseEntity {

    /**
     * Unique identifier for the transaction
     * Example: 1000001
     */
    @Id
    private Long spaceTransactionId;

    /**
     * Reference to the associated account space
     * Example: 100001 (links to AccountSpace.accountSpaceId)
     */
    private Long accountSpaceId;

    /**
     * Transaction amount
     * Positive for deposits, negative for withdrawals
     * Examples:
     * - 100.0000 (deposit of 100)
     * - -50.0000 (withdrawal of 50)
     */
    private BigDecimal amount;

    /**
     * Balance after this transaction was applied
     * Example: 1250.0000
     */
    private BigDecimal balanceAfterTransaction;

    /**
     * When the transaction occurred
     * Example: 2024-01-15T14:30:00.000Z
     */
    private LocalDateTime transactionDateTime;

    /**
     * Description of the transaction
     * Examples:
     * - "Deposit from main account"
     * - "Withdrawal to vacation fund"
     * - "Automatic transfer"
     */
    private String description;

    /**
     * Optional reference ID for cross-system tracking
     * Examples:
     * - "TXN-123456" (transaction ID from another system)
     * - "TRANSFER-789" (transfer ID)
     */
    private String referenceId;

    /**
     * Type of transaction
     * Examples:
     * - "DEPOSIT"
     * - "WITHDRAWAL"
     * - "TRANSFER_IN"
     * - "TRANSFER_OUT"
     * - "INTEREST"
     * - "FEE"
     */
    private String transactionType;
}
