package com.firefly.core.banking.accounts.models.repositories.notification.v1;

import com.firefly.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import com.firefly.core.banking.accounts.models.entities.notification.v1.AccountNotification;
import com.firefly.core.banking.accounts.models.repositories.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountNotificationRepository extends BaseRepository<AccountNotification, UUID> {
    
    /**
     * Find all notifications for a specific account
     * @param accountId The account ID
     * @return Flux of AccountNotification
     */
    Flux<AccountNotification> findByAccountId(UUID accountId);
    
    /**
     * Find unread notifications for a specific account
     * @param accountId The account ID
     * @param isRead Whether the notification has been read
     * @return Flux of AccountNotification
     */
    Flux<AccountNotification> findByAccountIdAndIsRead(UUID accountId, Boolean isRead);
    
    /**
     * Find notifications by type for a specific account
     * @param accountId The account ID
     * @param notificationType The notification type
     * @return Flux of AccountNotification
     */
    Flux<AccountNotification> findByAccountIdAndNotificationType(UUID accountId, NotificationTypeEnum notificationType);
    
    /**
     * Find notifications by priority for a specific account
     * @param accountId The account ID
     * @param priority The priority level
     * @return Flux of AccountNotification
     */
    Flux<AccountNotification> findByAccountIdAndPriority(UUID accountId, Integer priority);
    
    /**
     * Count unread notifications for a specific account
     * @param accountId The account ID
     * @param isRead Whether the notification has been read
     * @return Mono of Long representing the count
     */
    Mono<Long> countByAccountIdAndIsRead(UUID accountId, Boolean isRead);
    
    /**
     * Find notifications that have not expired
     * @param accountId The account ID
     * @param currentDateTime The current date and time
     * @return Flux of AccountNotification
     */
    Flux<AccountNotification> findByAccountIdAndExpiryDateTimeIsNullOrExpiryDateTimeGreaterThan(
            UUID accountId, LocalDateTime currentDateTime);
}
