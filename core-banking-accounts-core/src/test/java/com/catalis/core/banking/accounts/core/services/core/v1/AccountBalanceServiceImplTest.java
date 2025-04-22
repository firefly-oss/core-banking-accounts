package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountBalanceMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.catalis.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBalanceServiceImplTest {

    @Mock
    private AccountBalanceRepository repository;

    @Mock
    private AccountBalanceMapper mapper;

    @InjectMocks
    private AccountBalanceServiceImpl accountBalanceService;

    private AccountBalance testAccountBalance;
    private AccountBalanceDTO testAccountBalanceDTO;
    private final Long TEST_ACCOUNT_ID = 1L;
    private final Long TEST_BALANCE_ID = 100L;

    @BeforeEach
    void setUp() {
        // Setup test account balance entity
        testAccountBalance = new AccountBalance();
        testAccountBalance.setAccountBalanceId(TEST_BALANCE_ID);
        testAccountBalance.setAccountId(TEST_ACCOUNT_ID);
        testAccountBalance.setBalanceType(BalanceTypeEnum.CURRENT);
        testAccountBalance.setBalanceAmount(new BigDecimal("1000.0000"));
        testAccountBalance.setAsOfDatetime(LocalDateTime.now());

        // Setup test account balance DTO
        testAccountBalanceDTO = AccountBalanceDTO.builder()
                .accountBalanceId(TEST_BALANCE_ID)
                .accountId(TEST_ACCOUNT_ID)
                .balanceType(BalanceTypeEnum.CURRENT)
                .balanceAmount(new BigDecimal("1000.0000"))
                .asOfDatetime(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllBalances_ShouldReturnPaginatedBalances() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();

        // Create a mock PaginationResponse
        @SuppressWarnings("unchecked")
        PaginationResponse<AccountBalanceDTO> mockResponse = mock(PaginationResponse.class);

        // Mock the static PaginationUtils.paginateQuery method
        try (MockedStatic<PaginationUtils> mockedPaginationUtils = mockStatic(PaginationUtils.class)) {
            mockedPaginationUtils.when(() -> PaginationUtils.paginateQuery(
                    any(PaginationRequest.class),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(mockResponse));

            // Act & Assert
            StepVerifier.create(accountBalanceService.getAllBalances(TEST_ACCOUNT_ID, paginationRequest))
                    .expectNext(mockResponse)
                    .verifyComplete();

            // Verify the PaginationUtils.paginateQuery was called
            mockedPaginationUtils.verify(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            ));
        }
    }

    @Test
    void createBalance_ShouldReturnCreatedBalance() {
        // Arrange
        when(mapper.toEntity(any(AccountBalanceDTO.class))).thenReturn(testAccountBalance);
        when(repository.save(any(AccountBalance.class))).thenReturn(Mono.just(testAccountBalance));
        when(mapper.toDTO(any(AccountBalance.class))).thenReturn(testAccountBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.createBalance(TEST_ACCOUNT_ID, testAccountBalanceDTO))
                .expectNext(testAccountBalanceDTO)
                .verifyComplete();

        verify(mapper).toEntity(testAccountBalanceDTO);
        verify(repository).save(testAccountBalance);
        verify(mapper).toDTO(testAccountBalance);
    }

    @Test
    void getBalance_ShouldReturnBalance_WhenBalanceExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(testAccountBalance));
        when(mapper.toDTO(testAccountBalance)).thenReturn(testAccountBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.getBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .expectNext(testAccountBalanceDTO)
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verify(mapper).toDTO(testAccountBalance);
    }

    @Test
    void getBalance_ShouldReturnEmptyMono_WhenBalanceDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountBalanceService.getBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void getBalance_ShouldReturnEmptyMono_WhenBalanceExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountBalance balanceForDifferentAccount = new AccountBalance();
        balanceForDifferentAccount.setAccountBalanceId(TEST_BALANCE_ID);
        balanceForDifferentAccount.setAccountId(999L); // Different account ID

        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(balanceForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountBalanceService.getBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void updateBalance_ShouldReturnUpdatedBalance_WhenBalanceExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(testAccountBalance));
        when(mapper.toEntity(testAccountBalanceDTO)).thenReturn(testAccountBalance);
        when(repository.save(testAccountBalance)).thenReturn(Mono.just(testAccountBalance));
        when(mapper.toDTO(testAccountBalance)).thenReturn(testAccountBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.updateBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID, testAccountBalanceDTO))
                .expectNext(testAccountBalanceDTO)
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verify(mapper).toEntity(testAccountBalanceDTO);
        verify(repository).save(testAccountBalance);
        verify(mapper).toDTO(testAccountBalance);
    }

    @Test
    void updateBalance_ShouldReturnEmptyMono_WhenBalanceDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountBalanceService.updateBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID, testAccountBalanceDTO))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void updateBalance_ShouldReturnEmptyMono_WhenBalanceExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountBalance balanceForDifferentAccount = new AccountBalance();
        balanceForDifferentAccount.setAccountBalanceId(TEST_BALANCE_ID);
        balanceForDifferentAccount.setAccountId(999L); // Different account ID

        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(balanceForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountBalanceService.updateBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID, testAccountBalanceDTO))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void deleteBalance_ShouldDeleteBalance_WhenBalanceExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(testAccountBalance));
        when(repository.delete(testAccountBalance)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountBalanceService.deleteBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verify(repository).delete(testAccountBalance);
    }

    @Test
    void deleteBalance_ShouldReturnEmptyMono_WhenBalanceDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountBalanceService.deleteBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteBalance_ShouldReturnEmptyMono_WhenBalanceExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountBalance balanceForDifferentAccount = new AccountBalance();
        balanceForDifferentAccount.setAccountBalanceId(TEST_BALANCE_ID);
        balanceForDifferentAccount.setAccountId(999L); // Different account ID

        when(repository.findById(TEST_BALANCE_ID)).thenReturn(Mono.just(balanceForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountBalanceService.deleteBalance(TEST_ACCOUNT_ID, TEST_BALANCE_ID))
                .verifyComplete();

        verify(repository).findById(TEST_BALANCE_ID);
        verifyNoMoreInteractions(repository);
    }
}
