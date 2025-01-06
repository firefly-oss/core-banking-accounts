package com.catalis.core.banking.accounts.models.entities.status.v1;

import com.catalis.core.banking.accounts.interfaces.enums.models.status.v1.StatusCodeEnum;
import com.catalis.core.banking.accounts.models.entities.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * Tracks the history of status changes for an account.
 * Maintains an audit trail of all status transitions for regulatory and operational purposes.
 *
 * Business Rules:
 * - Every status change must be recorded
 * - Status periods cannot overlap
 * - End datetime must be after start datetime
 * - Latest status must match Account.accountStatus
 */

@Table("account_status_history")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusHistory extends BaseEntity {

    /**
     * Unique identifier for the status history record
     * Example: 1000001
     */
    @Id
    private Long accountStatusHistoryId;

    /**
     * Reference to the associated account
     * Example: 100001 (links to Account.accountId)
     */
    private Long accountId;

    /**
     * The status code for this period
     * Examples with typical reasons:
     * - OPEN: Normal active status
     * - SUSPENDED: Fraud investigation
     * - CLOSED: Customer request
     * - DORMANT: No activity for extended period
     */
    private StatusCodeEnum statusCode;

    /**
     * When this status became effective
     * Example: 2024-01-15T14:30:00.000Z
     */
    private LocalDateTime statusStartDatetime;

    /**
     * When this status was superseded by a new status
     * Null if this is the current status
     * Example: 2024-01-16T09:15:00.000Z
     */
    private LocalDateTime statusEndDatetime;

    /**
     * Optional explanation for the status change
     * Examples:
     * - "Customer requested account closure"
     * - "Automated dormancy after 12 months inactivity"
     * - "Fraud department investigation initiated"
     * - "Reactivated after identity verification"
     */
    private String reason;
}

