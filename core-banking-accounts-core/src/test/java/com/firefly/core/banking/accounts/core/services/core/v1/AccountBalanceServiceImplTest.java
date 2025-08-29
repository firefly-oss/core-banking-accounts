package com.firefly.core.banking.accounts.core.services.core.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.accounts.core.mappers.core.v1.AccountBalanceMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.firefly.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.firefly.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    void createBalance_ShouldCreateStakedCryptoBalance() {
        // Arrange
        AccountBalance stakedBalance = new AccountBalance();
        stakedBalance.setAccountBalanceId(200L);
        stakedBalance.setAccountId(TEST_ACCOUNT_ID);
        stakedBalance.setBalanceType(BalanceTypeEnum.STAKED);
        stakedBalance.setBalanceAmount(new BigDecimal("0.5"));
        stakedBalance.setAsOfDatetime(LocalDateTime.now());
        stakedBalance.setAssetSymbol("ETH");
        stakedBalance.setAssetDecimals("18");
        stakedBalance.setTransactionHash("0x742d35Cc6634C0532925a3b844Bc454e4438f44e123456789abcdef0123456789");
        stakedBalance.setConfirmations(12);
        
        AccountBalanceDTO stakedBalanceDTO = AccountBalanceDTO.builder()
                .accountBalanceId(200L)
                .accountId(TEST_ACCOUNT_ID)
                .balanceType(BalanceTypeEnum.STAKED)
                .balanceAmount(new BigDecimal("0.5"))
                .asOfDatetime(LocalDateTime.now())
                .assetSymbol("ETH")
                .assetDecimals("18")
                .transactionHash("0x742d35Cc6634C0532925a3b844Bc454e4438f44e123456789abcdef0123456789")
                .confirmations(12)
                .build();
        
        when(mapper.toEntity(any(AccountBalanceDTO.class))).thenReturn(stakedBalance);
        when(repository.save(any(AccountBalance.class))).thenReturn(Mono.just(stakedBalance));
        when(mapper.toDTO(any(AccountBalance.class))).thenReturn(stakedBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.createBalance(TEST_ACCOUNT_ID, stakedBalanceDTO))
                .expectNext(stakedBalanceDTO)
                .verifyComplete();

        verify(mapper).toEntity(stakedBalanceDTO);
        verify(repository).save(stakedBalance);
        verify(mapper).toDTO(stakedBalance);
    }
    
    @Test
    void createBalance_ShouldCreateLockedCryptoBalance() {
        // Arrange
        AccountBalance lockedBalance = new AccountBalance();
        lockedBalance.setAccountBalanceId(300L);
        lockedBalance.setAccountId(TEST_ACCOUNT_ID);
        lockedBalance.setBalanceType(BalanceTypeEnum.LOCKED);
        lockedBalance.setBalanceAmount(new BigDecimal("100"));
        lockedBalance.setAsOfDatetime(LocalDateTime.now());
        lockedBalance.setAssetSymbol("USDC");
        lockedBalance.setAssetDecimals("6");
        lockedBalance.setTransactionHash("0x842d35Cc6634C0532925a3b844Bc454e4438f44e123456789abcdef0123456789");
        lockedBalance.setConfirmations(15);
        
        AccountBalanceDTO lockedBalanceDTO = AccountBalanceDTO.builder()
                .accountBalanceId(300L)
                .accountId(TEST_ACCOUNT_ID)
                .balanceType(BalanceTypeEnum.LOCKED)
                .balanceAmount(new BigDecimal("100"))
                .asOfDatetime(LocalDateTime.now())
                .assetSymbol("USDC")
                .assetDecimals("6")
                .transactionHash("0x842d35Cc6634C0532925a3b844Bc454e4438f44e123456789abcdef0123456789")
                .confirmations(15)
                .build();
        
        when(mapper.toEntity(any(AccountBalanceDTO.class))).thenReturn(lockedBalance);
        when(repository.save(any(AccountBalance.class))).thenReturn(Mono.just(lockedBalance));
        when(mapper.toDTO(any(AccountBalance.class))).thenReturn(lockedBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.createBalance(TEST_ACCOUNT_ID, lockedBalanceDTO))
                .expectNext(lockedBalanceDTO)
                .verifyComplete();

        verify(mapper).toEntity(lockedBalanceDTO);
        verify(repository).save(lockedBalance);
        verify(mapper).toDTO(lockedBalance);
    }
    
    @Test
    void createBalance_ShouldCreatePendingConfirmationCryptoBalance() {
        // Arrange
        AccountBalance pendingBalance = new AccountBalance();
        pendingBalance.setAccountBalanceId(400L);
        pendingBalance.setAccountId(TEST_ACCOUNT_ID);
        pendingBalance.setBalanceType(BalanceTypeEnum.PENDING_CONFIRMATION);
        pendingBalance.setBalanceAmount(new BigDecimal("0.01"));
        pendingBalance.setAsOfDatetime(LocalDateTime.now());
        pendingBalance.setAssetSymbol("BTC");
        pendingBalance.setAssetDecimals("8");
        pendingBalance.setTransactionHash("3a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u");
        pendingBalance.setConfirmations(2);
        
        AccountBalanceDTO pendingBalanceDTO = AccountBalanceDTO.builder()
                .accountBalanceId(400L)
                .accountId(TEST_ACCOUNT_ID)
                .balanceType(BalanceTypeEnum.PENDING_CONFIRMATION)
                .balanceAmount(new BigDecimal("0.01"))
                .asOfDatetime(LocalDateTime.now())
                .assetSymbol("BTC")
                .assetDecimals("8")
                .transactionHash("3a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u")
                .confirmations(2)
                .build();
        
        when(mapper.toEntity(any(AccountBalanceDTO.class))).thenReturn(pendingBalance);
        when(repository.save(any(AccountBalance.class))).thenReturn(Mono.just(pendingBalance));
        when(mapper.toDTO(any(AccountBalance.class))).thenReturn(pendingBalanceDTO);

        // Act & Assert
        StepVerifier.create(accountBalanceService.createBalance(TEST_ACCOUNT_ID, pendingBalanceDTO))
                .expectNext(pendingBalanceDTO)
                .verifyComplete();

        verify(mapper).toEntity(pendingBalanceDTO);
        verify(repository).save(pendingBalance);
        verify(mapper).toDTO(pendingBalance);
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
