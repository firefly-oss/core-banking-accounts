package com.catalis.core.banking.accounts.web.controllers.space.v1;

import com.catalis.core.banking.accounts.core.services.space.v1.AccountSpaceService;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.SpaceAnalyticsDTO;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountSpaceControllerTest {

    @Mock
    private AccountSpaceService service;

    @InjectMocks
    private AccountSpaceController controller;

    private static final Long ACCOUNT_ID = 1L;
    private static final Long SPACE_ID = 100L;

    private AccountSpaceDTO accountSpaceDTO;
    private SpaceAnalyticsDTO spaceAnalyticsDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        accountSpaceDTO = new AccountSpaceDTO();
        accountSpaceDTO.setAccountSpaceId(SPACE_ID);
        accountSpaceDTO.setAccountId(ACCOUNT_ID);
        accountSpaceDTO.setSpaceName("Test Space");
        accountSpaceDTO.setSpaceType(AccountSpaceTypeEnum.SAVINGS);
        accountSpaceDTO.setBalance(BigDecimal.valueOf(1000));
        accountSpaceDTO.setIsFrozen(false);

        spaceAnalyticsDTO = new SpaceAnalyticsDTO();
        spaceAnalyticsDTO.setAccountSpaceId(SPACE_ID);
        spaceAnalyticsDTO.setAccountId(ACCOUNT_ID);
        spaceAnalyticsDTO.setSpaceName("Test Space");
        spaceAnalyticsDTO.setSpaceType(AccountSpaceTypeEnum.SAVINGS);
        spaceAnalyticsDTO.setOpeningBalance(BigDecimal.valueOf(800));
        spaceAnalyticsDTO.setClosingBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void freezeAccountSpace_Success() {
        // Arrange
        AccountSpaceDTO frozenSpaceDTO = new AccountSpaceDTO();
        frozenSpaceDTO.setAccountSpaceId(SPACE_ID);
        frozenSpaceDTO.setIsFrozen(true);
        frozenSpaceDTO.setFrozenDateTime(LocalDateTime.now());

        when(service.freezeAccountSpace(SPACE_ID)).thenReturn(Mono.just(frozenSpaceDTO));

        // Act & Assert
        StepVerifier.create(controller.freezeAccountSpace(SPACE_ID))
                .expectNextMatches(response -> 
                        response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        response.getBody().getIsFrozen() &&
                        response.getBody().getFrozenDateTime() != null
                )
                .verifyComplete();
    }

    @Test
    void freezeAccountSpace_Error() {
        // Arrange
        when(service.freezeAccountSpace(SPACE_ID)).thenReturn(Mono.error(new IllegalStateException("Already frozen")));

        // Act & Assert
        StepVerifier.create(controller.freezeAccountSpace(SPACE_ID))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    void unfreezeAccountSpace_Success() {
        // Arrange
        AccountSpaceDTO unfrozenSpaceDTO = new AccountSpaceDTO();
        unfrozenSpaceDTO.setAccountSpaceId(SPACE_ID);
        unfrozenSpaceDTO.setIsFrozen(false);
        unfrozenSpaceDTO.setUnfrozenDateTime(LocalDateTime.now());

        when(service.unfreezeAccountSpace(SPACE_ID)).thenReturn(Mono.just(unfrozenSpaceDTO));

        // Act & Assert
        StepVerifier.create(controller.unfreezeAccountSpace(SPACE_ID))
                .expectNextMatches(response -> 
                        response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        !response.getBody().getIsFrozen() &&
                        response.getBody().getUnfrozenDateTime() != null
                )
                .verifyComplete();
    }

    @Test
    void unfreezeAccountSpace_Error() {
        // Arrange
        when(service.unfreezeAccountSpace(SPACE_ID)).thenReturn(Mono.error(new IllegalStateException("Not frozen")));

        // Act & Assert
        StepVerifier.create(controller.unfreezeAccountSpace(SPACE_ID))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    void updateAccountSpaceBalance_Success() {
        // Arrange
        BigDecimal newBalance = BigDecimal.valueOf(2000);
        String reason = "Administrative adjustment";
        
        AccountSpaceDTO updatedSpaceDTO = new AccountSpaceDTO();
        updatedSpaceDTO.setAccountSpaceId(SPACE_ID);
        updatedSpaceDTO.setBalance(newBalance);
        updatedSpaceDTO.setLastBalanceUpdateReason(reason);
        updatedSpaceDTO.setLastBalanceUpdateDateTime(LocalDateTime.now());

        when(service.updateAccountSpaceBalance(eq(SPACE_ID), eq(newBalance), eq(reason)))
                .thenReturn(Mono.just(updatedSpaceDTO));

        // Act & Assert
        StepVerifier.create(controller.updateAccountSpaceBalance(SPACE_ID, newBalance, reason))
                .expectNextMatches(response -> 
                        response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        response.getBody().getBalance().equals(newBalance) &&
                        response.getBody().getLastBalanceUpdateReason().equals(reason)
                )
                .verifyComplete();
    }

    @Test
    void updateAccountSpaceBalance_Error() {
        // Arrange
        BigDecimal newBalance = BigDecimal.valueOf(-100);
        String reason = "Administrative adjustment";
        
        when(service.updateAccountSpaceBalance(eq(SPACE_ID), eq(newBalance), eq(reason)))
                .thenReturn(Mono.error(new IllegalArgumentException("Negative balance")));

        // Act & Assert
        StepVerifier.create(controller.updateAccountSpaceBalance(SPACE_ID, newBalance, reason))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    void getSpaceAnalytics_Success() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        
        when(service.getSpaceAnalytics(eq(SPACE_ID), eq(startDate), eq(endDate)))
                .thenReturn(Mono.just(spaceAnalyticsDTO));

        // Act & Assert
        StepVerifier.create(controller.getSpaceAnalytics(SPACE_ID, startDate, endDate))
                .expectNextMatches(response -> 
                        response.getStatusCode() == HttpStatus.OK &&
                        response.getBody() != null &&
                        response.getBody().getAccountSpaceId().equals(SPACE_ID) &&
                        response.getBody().getOpeningBalance().equals(BigDecimal.valueOf(800)) &&
                        response.getBody().getClosingBalance().equals(BigDecimal.valueOf(1000))
                )
                .verifyComplete();
    }

    @Test
    void getSpaceAnalytics_Error() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().minusDays(1); // Invalid range
        
        when(service.getSpaceAnalytics(eq(SPACE_ID), eq(startDate), eq(endDate)))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid date range")));

        // Act & Assert
        StepVerifier.create(controller.getSpaceAnalytics(SPACE_ID, startDate, endDate))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }
}
