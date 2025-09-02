package com.firefly.core.banking.accounts.interfaces.dtos.notification.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountNotificationDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID accountNotificationId;

    @FilterableId
    private UUID accountId;

    private NotificationTypeEnum notificationType;
    private String title;
    private String message;
    private LocalDateTime creationDateTime;
    private LocalDateTime expiryDateTime;
    private Boolean isRead;
    private LocalDateTime readDateTime;
    private Integer priority;
    private String deliveryChannels;
    private String eventReference;
    private BigDecimal relatedAmount;
    private String actionUrl;
    private String actionText;
}
