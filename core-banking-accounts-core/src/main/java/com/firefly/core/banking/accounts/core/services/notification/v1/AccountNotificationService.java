package com.firefly.core.banking.accounts.core.services.notification.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.notification.v1.AccountNotificationDTO;
import com.firefly.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface AccountNotificationService {
    
    /**
     * Create a new account notification
     * @param accountNotificationDTO The account notification DTO
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> createAccountNotification(AccountNotificationDTO accountNotificationDTO);
    
    /**
     * Get an account notification by ID
     * @param accountNotificationId The account notification ID
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> getAccountNotification(UUID accountNotificationId);
    
    /**
     * Update an account notification
     * @param accountNotificationId The account notification ID
     * @param accountNotificationDTO The account notification DTO
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> updateAccountNotification(UUID accountNotificationId, AccountNotificationDTO accountNotificationDTO);
    
    /**
     * Delete an account notification
     * @param accountNotificationId The account notification ID
     * @return Mono of Void
     */
    Mono<Void> deleteAccountNotification(UUID accountNotificationId);
    
    /**
     * Get all account notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getAccountNotificationsByAccountId(UUID accountId);
    
    /**
     * Get unread account notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getUnreadAccountNotifications(UUID accountId);
    
    /**
     * Get account notifications by type for an account
     * @param accountId The account ID
     * @param notificationType The notification type
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getAccountNotificationsByType(UUID accountId, NotificationTypeEnum notificationType);
    
    /**
     * Mark an account notification as read
     * @param accountNotificationId The account notification ID
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> markNotificationAsRead(UUID accountNotificationId);
    
    /**
     * Mark all notifications as read for an account
     * @param accountId The account ID
     * @return Mono of Long representing the number of notifications marked as read
     */
    Mono<Long> markAllNotificationsAsRead(UUID accountId);
    
    /**
     * Get active (non-expired) notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getActiveNotifications(UUID accountId);
    
    /**
     * List account notifications with pagination and filtering
     * @param filterRequest The filter request
     * @return Mono of PaginationResponse containing AccountNotificationDTO
     */
    Mono<PaginationResponse<AccountNotificationDTO>> listAccountNotifications(FilterRequest filterRequest);
}
