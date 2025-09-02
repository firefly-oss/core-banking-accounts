package com.firefly.core.banking.accounts.core.services.restriction.v1;

import com.firefly.core.banking.accounts.core.mappers.restriction.v1.AccountRestrictionMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.restriction.v1.AccountRestrictionDTO;
import com.firefly.core.banking.accounts.interfaces.enums.restriction.v1.RestrictionTypeEnum;
import com.firefly.core.banking.accounts.models.entities.restriction.v1.AccountRestriction;
import com.firefly.core.banking.accounts.models.repositories.restriction.v1.AccountRestrictionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountRestrictionServiceImplTest {

    @Mock
    private AccountRestrictionRepository repository;

    @Mock
    private AccountRestrictionMapper mapper;

    @InjectMocks
    private AccountRestrictionServiceImpl service;

    private AccountRestriction accountRestriction;
    private AccountRestrictionDTO accountRestrictionDTO;
    private final UUID RESTRICTION_ID = 1L;
    private final UUID ACCOUNT_ID = 100L;

    @BeforeEach
    void setUp() {
        // Setup test data
        accountRestriction = new AccountRestriction();
        accountRestriction.setAccountRestrictionId(RESTRICTION_ID);
        accountRestriction.setAccountId(ACCOUNT_ID);
        accountRestriction.setRestrictionType(RestrictionTypeEnum.WITHDRAWAL_HOLD);
        accountRestriction.setStartDateTime(LocalDateTime.now());
        accountRestriction.setReferenceNumber("REF-001");
        accountRestriction.setReason("Test reason");
        accountRestriction.setAppliedBy("test-user");
        accountRestriction.setIsActive(true);

        accountRestrictionDTO = new AccountRestrictionDTO();
        accountRestrictionDTO.setAccountRestrictionId(RESTRICTION_ID);
        accountRestrictionDTO.setAccountId(ACCOUNT_ID);
        accountRestrictionDTO.setRestrictionType(RestrictionTypeEnum.WITHDRAWAL_HOLD);
        accountRestrictionDTO.setStartDateTime(LocalDateTime.now());
        accountRestrictionDTO.setReferenceNumber("REF-001");
        accountRestrictionDTO.setReason("Test reason");
        accountRestrictionDTO.setAppliedBy("test-user");
        accountRestrictionDTO.setIsActive(true);
    }

    @Test
    void createAccountRestriction_Success() {
        // Arrange
        when(mapper.toEntity(any(AccountRestrictionDTO.class))).thenReturn(accountRestriction);
        when(repository.save(any(AccountRestriction.class))).thenReturn(Mono.just(accountRestriction));
        when(mapper.toDTO(any(AccountRestriction.class))).thenReturn(accountRestrictionDTO);

        // Act & Assert
        StepVerifier.create(service.createAccountRestriction(accountRestrictionDTO))
                .expectNext(accountRestrictionDTO)
                .verifyComplete();

        verify(repository).save(any(AccountRestriction.class));
    }

    @Test
    void createAccountRestriction_MissingAccountId() {
        // Arrange
        accountRestrictionDTO.setAccountId(null);

        // Act & Assert
        StepVerifier.create(service.createAccountRestriction(accountRestrictionDTO))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).save(any(AccountRestriction.class));
    }

    @Test
    void getAccountRestriction_Success() {
        // Arrange
        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.just(accountRestriction));
        when(mapper.toDTO(accountRestriction)).thenReturn(accountRestrictionDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountRestriction(RESTRICTION_ID))
                .expectNext(accountRestrictionDTO)
                .verifyComplete();

        verify(repository).findById(RESTRICTION_ID);
    }

    @Test
    void getAccountRestriction_NotFound() {
        // Arrange
        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getAccountRestriction(RESTRICTION_ID))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository).findById(RESTRICTION_ID);
    }

    @Test
    void updateAccountRestriction_Success() {
        // Arrange
        AccountRestriction updatedRestriction = new AccountRestriction();
        updatedRestriction.setAccountRestrictionId(RESTRICTION_ID);
        updatedRestriction.setReason("Updated reason");

        AccountRestrictionDTO updatedDTO = new AccountRestrictionDTO();
        updatedDTO.setAccountRestrictionId(RESTRICTION_ID);
        updatedDTO.setReason("Updated reason");

        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.just(accountRestriction));
        when(repository.save(any(AccountRestriction.class))).thenReturn(Mono.just(updatedRestriction));
        when(mapper.toDTO(updatedRestriction)).thenReturn(updatedDTO);

        // Act & Assert
        StepVerifier.create(service.updateAccountRestriction(RESTRICTION_ID, updatedDTO))
                .expectNext(updatedDTO)
                .verifyComplete();

        verify(repository).findById(RESTRICTION_ID);
        verify(repository).save(any(AccountRestriction.class));
    }

    @Test
    void deleteAccountRestriction_Success() {
        // Arrange
        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.just(accountRestriction));
        when(repository.delete(accountRestriction)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAccountRestriction(RESTRICTION_ID))
                .verifyComplete();

        verify(repository).findById(RESTRICTION_ID);
        verify(repository).delete(accountRestriction);
    }

    @Test
    void getAccountRestrictionsByAccountId_Success() {
        // Arrange
        when(repository.findByAccountId(ACCOUNT_ID)).thenReturn(Flux.just(accountRestriction));
        when(mapper.toDTO(accountRestriction)).thenReturn(accountRestrictionDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountRestrictionsByAccountId(ACCOUNT_ID))
                .expectNext(accountRestrictionDTO)
                .verifyComplete();

        verify(repository).findByAccountId(ACCOUNT_ID);
    }

    @Test
    void getActiveAccountRestrictionsByAccountId_Success() {
        // Arrange
        when(repository.findByAccountIdAndIsActive(ACCOUNT_ID, true)).thenReturn(Flux.just(accountRestriction));
        when(mapper.toDTO(accountRestriction)).thenReturn(accountRestrictionDTO);

        // Act & Assert
        StepVerifier.create(service.getActiveAccountRestrictionsByAccountId(ACCOUNT_ID))
                .expectNext(accountRestrictionDTO)
                .verifyComplete();

        verify(repository).findByAccountIdAndIsActive(ACCOUNT_ID, true);
    }

    @Test
    void removeRestriction_Success() {
        // Arrange
        String removedBy = "admin-user";

        AccountRestriction removedRestriction = new AccountRestriction();
        removedRestriction.setAccountRestrictionId(RESTRICTION_ID);
        removedRestriction.setIsActive(false);
        removedRestriction.setRemovedBy(removedBy);
        // Don't use matchers in object initialization
        LocalDateTime endTime = LocalDateTime.now();
        removedRestriction.setEndDateTime(endTime);

        AccountRestrictionDTO removedDTO = new AccountRestrictionDTO();
        removedDTO.setAccountRestrictionId(RESTRICTION_ID);
        removedDTO.setIsActive(false);
        removedDTO.setRemovedBy(removedBy);
        removedDTO.setEndDateTime(endTime);

        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.just(accountRestriction));
        when(repository.save(any(AccountRestriction.class))).thenReturn(Mono.just(removedRestriction));
        when(mapper.toDTO(removedRestriction)).thenReturn(removedDTO);

        // Act & Assert
        StepVerifier.create(service.removeRestriction(RESTRICTION_ID, removedBy))
                .expectNext(removedDTO)
                .verifyComplete();

        verify(repository).findById(RESTRICTION_ID);
        verify(repository).save(any(AccountRestriction.class));
    }

    @Test
    void removeRestriction_AlreadyRemoved() {
        // Arrange
        String removedBy = "admin-user";
        accountRestriction.setIsActive(false);

        when(repository.findById(RESTRICTION_ID)).thenReturn(Mono.just(accountRestriction));

        // Act & Assert
        StepVerifier.create(service.removeRestriction(RESTRICTION_ID, removedBy))
                .expectError(IllegalStateException.class)
                .verify();

        verify(repository).findById(RESTRICTION_ID);
        verify(repository, never()).save(any(AccountRestriction.class));
    }

    // TODO: Fix this test
    // @Test
    void listAccountRestrictions_Success() {
        // This test is disabled until we can properly mock the FilterUtils class
    }
}
