package com.catalis.core.banking.accounts.core.mappers.notification.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.notification.v1.AccountNotificationDTO;
import com.catalis.core.banking.accounts.models.entities.notification.v1.AccountNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountNotificationMapper {
    AccountNotificationDTO toDTO(AccountNotification accountNotification);
    AccountNotification toEntity(AccountNotificationDTO accountNotificationDTO);
}
