package com.firefly.core.banking.accounts.core.services.core.v1;

import com.firefly.core.banking.accounts.core.mappers.core.v1.AccountMapper;
import com.firefly.core.banking.accounts.core.services.space.v1.AccountSpaceService;
import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.firefly.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.AccountStatusEnum;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.AccountTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.firefly.core.banking.accounts.models.entities.core.v1.Account;
import com.firefly.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountSpaceService accountSpaceService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private AccountDTO testAccountDTO;
    private final UUID TEST_ACCOUNT_ID = 1L;

    @BeforeEach
    void setUp() {
        // Setup test account entity
        testAccount = new Account();
        testAccount.setAccountId(TEST_ACCOUNT_ID);
        testAccount.setContractId(100L);
        testAccount.setAccountNumber("TEST-ACCOUNT-001");
        testAccount.setAccountType(AccountTypeEnum.CHECKING);
        testAccount.setCurrency("USD");
        testAccount.setOpenDate(LocalDate.now());
        testAccount.setAccountStatus(AccountStatusEnum.OPEN);
        testAccount.setBranchId(200L);
        testAccount.setDescription("Test Account");

        // Setup test account DTO
        testAccountDTO = AccountDTO.builder()
                .accountId(TEST_ACCOUNT_ID)
                .contractId(100L)
                .accountNumber("TEST-ACCOUNT-001")
                .accountType(AccountTypeEnum.CHECKING)
                .currency("USD")
                .openDate(LocalDate.now())
                .accountStatus(AccountStatusEnum.OPEN)
                .branchId(200L)
                .description("Test Account")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    void createAccount_ShouldCreateMainSpaceAndReturnCreatedAccount() {
        // Arrange
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(testAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(testAccountDTO);
        when(accountSpaceService.createAccountSpace(any(AccountSpaceDTO.class))).thenReturn(Mono.just(
                AccountSpaceDTO.builder()
                        .accountSpaceId(1L)
                        .accountId(TEST_ACCOUNT_ID)
                        .spaceName("Main Account")
                        .spaceType(AccountSpaceTypeEnum.MAIN)
                        .build()
        ));

        // Act & Assert
        StepVerifier.create(accountService.createAccount(testAccountDTO))
                .expectNext(testAccountDTO)
                .verifyComplete();

        verify(accountMapper).toEntity(testAccountDTO);
        verify(accountRepository).save(testAccount);

        // Verify that createAccountSpace was called with a DTO containing the correct values
        verify(accountSpaceService).createAccountSpace(argThat(spaceDTO ->
                spaceDTO.getAccountId().equals(TEST_ACCOUNT_ID) &&
                spaceDTO.getSpaceType() == AccountSpaceTypeEnum.MAIN &&
                "Main Account".equals(spaceDTO.getSpaceName())
        ));

        verify(accountMapper).toDTO(testAccount);
    }

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.just(testAccount));
        when(accountMapper.toDTO(testAccount)).thenReturn(testAccountDTO);

        // Act & Assert
        StepVerifier.create(accountService.getAccount(TEST_ACCOUNT_ID))
                .expectNext(testAccountDTO)
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        verify(accountMapper).toDTO(testAccount);
    }

    @Test
    void getAccount_ShouldReturnEmptyMono_WhenAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountService.getAccount(TEST_ACCOUNT_ID))
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        verifyNoMoreInteractions(accountMapper);
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount_WhenAccountExists() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.just(testAccount));
       // when(accountMapper.updateEntityFromDto(testAccountDTO, testAccount)).thenReturn(testAccount);
        when(accountRepository.save(testAccount)).thenReturn(Mono.just(testAccount));
        when(accountMapper.toDTO(testAccount)).thenReturn(testAccountDTO);

        // Act & Assert
        StepVerifier.create(accountService.updateAccount(TEST_ACCOUNT_ID, testAccountDTO))
                .expectNext(testAccountDTO)
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        //verify(accountMapper).toEntity(testAccountDTO);
        verify(accountRepository).save(testAccount);
        verify(accountMapper).toDTO(testAccount);
    }

    @Test
    void updateAccount_ShouldReturnEmptyMono_WhenAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountService.updateAccount(TEST_ACCOUNT_ID, testAccountDTO))
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        verifyNoMoreInteractions(accountMapper, accountRepository);
    }

    @Test
    void deleteAccount_ShouldDeleteAccount_WhenAccountExists() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.just(testAccount));
        when(accountRepository.delete(testAccount)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountService.deleteAccount(TEST_ACCOUNT_ID))
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        verify(accountRepository).delete(testAccount);
    }

    @Test
    void deleteAccount_ShouldReturnEmptyMono_WhenAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountService.deleteAccount(TEST_ACCOUNT_ID))
                .verifyComplete();

        verify(accountRepository).findById(TEST_ACCOUNT_ID);
        verifyNoMoreInteractions(accountRepository);
    }
    
    @Test
    void createAccount_ShouldCreateCryptoWalletAccount() {
        // Arrange
        AccountDTO cryptoAccountDTO = AccountDTO.builder()
                .contractId(100L)
                .accountNumber("CRYPTO-001")
                .accountType(AccountTypeEnum.CRYPTO_WALLET)
                .currency("USD")
                .openDate(LocalDate.now())
                .accountStatus(AccountStatusEnum.OPEN)
                .branchId(200L)
                .description("Bitcoin Wallet")
                .walletAddress("0x742d35Cc6634C0532925a3b844Bc454e4438f44e")
                .blockchainNetwork("Ethereum")
                .isCustodial(true)
                .build();
                
        Account cryptoAccount = new Account();
        cryptoAccount.setAccountId(2L);
        cryptoAccount.setContractId(100L);
        cryptoAccount.setAccountNumber("CRYPTO-001");
        cryptoAccount.setAccountType(AccountTypeEnum.CRYPTO_WALLET);
        cryptoAccount.setCurrency("USD");
        cryptoAccount.setOpenDate(LocalDate.now());
        cryptoAccount.setAccountStatus(AccountStatusEnum.OPEN);
        cryptoAccount.setBranchId(200L);
        cryptoAccount.setDescription("Bitcoin Wallet");
        cryptoAccount.setWalletAddress("0x742d35Cc6634C0532925a3b844Bc454e4438f44e");
        cryptoAccount.setBlockchainNetwork("Ethereum");
        cryptoAccount.setIsCustodial(true);
        
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(cryptoAccount);
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(cryptoAccount));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(cryptoAccountDTO);
        when(accountSpaceService.createAccountSpace(any(AccountSpaceDTO.class))).thenReturn(Mono.just(
                AccountSpaceDTO.builder()
                        .accountSpaceId(2L)
                        .accountId(2L)
                        .spaceName("Main Account")
                        .spaceType(AccountSpaceTypeEnum.MAIN)
                        .build()
        ));

        // Act & Assert
        StepVerifier.create(accountService.createAccount(cryptoAccountDTO))
                .expectNext(cryptoAccountDTO)
                .verifyComplete();

        verify(accountMapper).toEntity(cryptoAccountDTO);
        verify(accountRepository).save(cryptoAccount);
        
        // Verify that createAccountSpace was called with a DTO containing the correct values
        verify(accountSpaceService).createAccountSpace(argThat(spaceDTO ->
                spaceDTO.getAccountId().equals(2L) &&
                spaceDTO.getSpaceType() == AccountSpaceTypeEnum.MAIN &&
                "Main Account".equals(spaceDTO.getSpaceName())
        ));
        
        verify(accountMapper).toDTO(cryptoAccount);
    }
    
    @Test
    void createAccount_ShouldCreateTokenizedAssetAccount() {
        // Arrange
        AccountDTO tokenizedAssetDTO = AccountDTO.builder()
                .contractId(100L)
                .accountNumber("TOKEN-001")
                .accountType(AccountTypeEnum.TOKENIZED_ASSET)
                .currency("USD")
                .openDate(LocalDate.now())
                .accountStatus(AccountStatusEnum.OPEN)
                .branchId(200L)
                .description("USDT Token")
                .walletAddress("0x742d35Cc6634C0532925a3b844Bc454e4438f44e")
                .blockchainNetwork("Ethereum")
                .tokenContractAddress("0xdAC17F958D2ee523a2206206994597C13D831ec7")
                .tokenStandard("ERC-20")
                .isCustodial(true)
                .build();
                
        Account tokenizedAsset = new Account();
        tokenizedAsset.setAccountId(3L);
        tokenizedAsset.setContractId(100L);
        tokenizedAsset.setAccountNumber("TOKEN-001");
        tokenizedAsset.setAccountType(AccountTypeEnum.TOKENIZED_ASSET);
        tokenizedAsset.setCurrency("USD");
        tokenizedAsset.setOpenDate(LocalDate.now());
        tokenizedAsset.setAccountStatus(AccountStatusEnum.OPEN);
        tokenizedAsset.setBranchId(200L);
        tokenizedAsset.setDescription("USDT Token");
        tokenizedAsset.setWalletAddress("0x742d35Cc6634C0532925a3b844Bc454e4438f44e");
        tokenizedAsset.setBlockchainNetwork("Ethereum");
        tokenizedAsset.setTokenContractAddress("0xdAC17F958D2ee523a2206206994597C13D831ec7");
        tokenizedAsset.setTokenStandard("ERC-20");
        tokenizedAsset.setIsCustodial(true);
        
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(tokenizedAsset);
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(tokenizedAsset));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(tokenizedAssetDTO);
        when(accountSpaceService.createAccountSpace(any(AccountSpaceDTO.class))).thenReturn(Mono.just(
                AccountSpaceDTO.builder()
                        .accountSpaceId(3L)
                        .accountId(3L)
                        .spaceName("Main Account")
                        .spaceType(AccountSpaceTypeEnum.MAIN)
                        .build()
        ));

        // Act & Assert
        StepVerifier.create(accountService.createAccount(tokenizedAssetDTO))
                .expectNext(tokenizedAssetDTO)
                .verifyComplete();

        verify(accountMapper).toEntity(tokenizedAssetDTO);
        verify(accountRepository).save(tokenizedAsset);
        
        // Verify that createAccountSpace was called with a DTO containing the correct values
        verify(accountSpaceService).createAccountSpace(argThat(spaceDTO ->
                spaceDTO.getAccountId().equals(3L) &&
                spaceDTO.getSpaceType() == AccountSpaceTypeEnum.MAIN &&
                "Main Account".equals(spaceDTO.getSpaceName())
        ));
        
        verify(accountMapper).toDTO(tokenizedAsset);
    }
}
