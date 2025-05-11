package com.catalis.core.banking.accounts.core.services.notification.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.interfaces.dtos.notification.v1.AccountNotificationDTO;
import com.catalis.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Mono<AccountNotificationDTO> getAccountNotification(Long accountNotificationId);
    
    /**
     * Update an account notification
     * @param accountNotificationId The account notification ID
     * @param accountNotificationDTO The account notification DTO
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> updateAccountNotification(Long accountNotificationId, AccountNotificationDTO accountNotificationDTO);
    
    /**
     * Delete an account notification
     * @param accountNotificationId The account notification ID
     * @return Mono of Void
     */
    Mono<Void> deleteAccountNotification(Long accountNotificationId);
    
    /**
     * Get all account notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getAccountNotificationsByAccountId(Long accountId);
    
    /**
     * Get unread account notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getUnreadAccountNotifications(Long accountId);
    
    /**
     * Get account notifications by type for an account
     * @param accountId The account ID
     * @param notificationType The notification type
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getAccountNotificationsByType(Long accountId, NotificationTypeEnum notificationType);
    
    /**
     * Mark an account notification as read
     * @param accountNotificationId The account notification ID
     * @return Mono of AccountNotificationDTO
     */
    Mono<AccountNotificationDTO> markNotificationAsRead(Long accountNotificationId);
    
    /**
     * Mark all notifications as read for an account
     * @param accountId The account ID
     * @return Mono of Long representing the number of notifications marked as read
     */
    Mono<Long> markAllNotificationsAsRead(Long accountId);
    
    /**
     * Get active (non-expired) notifications for an account
     * @param accountId The account ID
     * @return Flux of AccountNotificationDTO
     */
    Flux<AccountNotificationDTO> getActiveNotifications(Long accountId);
    
    /**
     * List account notifications with pagination and filtering
     * @param filterRequest The filter request
     * @return Mono of PaginationResponse containing AccountNotificationDTO
     */
    Mono<PaginationResponse<AccountNotificationDTO>> listAccountNotifications(FilterRequest filterRequest);
}
