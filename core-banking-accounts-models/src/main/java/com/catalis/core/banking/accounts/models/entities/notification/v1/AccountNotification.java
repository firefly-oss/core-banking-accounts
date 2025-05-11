package com.catalis.core.banking.accounts.models.entities.notification.v1;

import com.catalis.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import com.catalis.core.banking.accounts.models.entities.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a notification or alert for an account.
 * Notifications inform customers about various account events and conditions.
 *
 * Business Rules:
 * - Each account can have multiple notifications
 * - Notifications can be triggered by various events
 * - Notifications can be delivered through various channels
 * - Notifications can be read or unread
 * - Notifications can expire after a certain period
 */
@Table("account_notification")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountNotification extends BaseEntity {

    /**
     * Unique identifier for the notification record
     * Example: 1000001
     */
    @Id
    private Long accountNotificationId;

    /**
     * Reference to the associated account
     * Example: 100001 (links to Account.accountId)
     */
    private Long accountId;

    /**
     * Type of notification
     * Examples:
     * - LOW_BALANCE: Balance below threshold
     * - LARGE_WITHDRAWAL: Unusually large withdrawal
     * - OVERDRAFT: Account overdrawn
     */
    private NotificationTypeEnum notificationType;

    /**
     * Title or subject of the notification
     * Examples:
     * - "Low Balance Alert"
     * - "Large Withdrawal Detected"
     * - "Overdraft Notice"
     */
    private String title;

    /**
     * Detailed message of the notification
     * Examples:
     * - "Your account balance is below $100"
     * - "A withdrawal of $5,000 was made from your account"
     * - "Your account is overdrawn by $50"
     */
    private String message;

    /**
     * When the notification was created
     * Example: 2024-01-15T14:30:00.000Z
     */
    private LocalDateTime creationDateTime;

    /**
     * When the notification will expire
     * Null if it doesn't expire
     * Example: 2024-02-15T23:59:59.999Z
     */
    private LocalDateTime expiryDateTime;

    /**
     * Whether the notification has been read
     * True if read, false if unread
     */
    private Boolean isRead;

    /**
     * When the notification was read
     * Null if not yet read
     * Example: 2024-01-16T08:45:00.000Z
     */
    private LocalDateTime readDateTime;

    /**
     * Priority level of the notification
     * Examples:
     * - 1: High priority
     * - 2: Medium priority
     * - 3: Low priority
     */
    private Integer priority;

    /**
     * Channels through which the notification was delivered
     * Examples:
     * - "EMAIL,SMS,PUSH" (delivered via email, SMS, and push notification)
     * - "EMAIL" (delivered via email only)
     * - "APP" (delivered in-app only)
     */
    private String deliveryChannels;

    /**
     * Reference to the event that triggered the notification
     * Examples:
     * - "TXN-123456" (transaction reference)
     * - "STATUS-CHANGE-789" (status change reference)
     * - "PARAM-UPDATE-321" (parameter update reference)
     */
    private String eventReference;

    /**
     * Optional amount related to the notification
     * Null if not applicable
     * Example: 50.0000 (Overdraft amount)
     */
    private BigDecimal relatedAmount;

    /**
     * Optional action URL for the notification
     * Null if no action is required
     * Example: "https://banking.example.com/accounts/100001/overdraft"
     */
    private String actionUrl;

    /**
     * Optional action text for the notification
     * Null if no action is required
     * Example: "View Overdraft Details"
     */
    private String actionText;
}
