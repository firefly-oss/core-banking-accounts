package com.catalis.core.banking.accounts.core.services.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.core.banking.accounts.core.mappers.models.statement.v1.AccountStatementMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import com.catalis.core.banking.accounts.interfaces.enums.statement.v1.StatementDeliveryMethodEnum;
import com.catalis.core.banking.accounts.models.entities.statement.v1.AccountStatement;
import com.catalis.core.banking.accounts.models.repositories.statement.v1.AccountStatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountStatementServiceImplTest {

    @Mock
    private AccountStatementRepository repository;

    @Mock
    private AccountStatementMapper mapper;

    @InjectMocks
    private AccountStatementServiceImpl service;

    private AccountStatement accountStatement;
    private AccountStatementDTO accountStatementDTO;
    private final Long STATEMENT_ID = 1L;
    private final Long ACCOUNT_ID = 100L;
    private final String STATEMENT_NUMBER = "STMT-20240101-100";

    @BeforeEach
    void setUp() {
        // Setup test data
        LocalDate periodStartDate = LocalDate.of(2024, 1, 1);
        LocalDate periodEndDate = LocalDate.of(2024, 1, 31);

        accountStatement = new AccountStatement();
        accountStatement.setAccountStatementId(STATEMENT_ID);
        accountStatement.setAccountId(ACCOUNT_ID);
        accountStatement.setStatementNumber(STATEMENT_NUMBER);
        accountStatement.setPeriodStartDate(periodStartDate);
        accountStatement.setPeriodEndDate(periodEndDate);
        accountStatement.setOpeningBalance(BigDecimal.valueOf(1000));
        accountStatement.setClosingBalance(BigDecimal.valueOf(1200));
        accountStatement.setTotalDeposits(BigDecimal.valueOf(500));
        accountStatement.setTotalWithdrawals(BigDecimal.valueOf(300));
        accountStatement.setTotalFees(BigDecimal.valueOf(10));
        accountStatement.setTotalInterest(BigDecimal.valueOf(10));
        accountStatement.setGenerationDateTime(LocalDateTime.now());
        accountStatement.setDeliveryMethod(StatementDeliveryMethodEnum.ONLINE);
        accountStatement.setIsViewed(false);

        accountStatementDTO = new AccountStatementDTO();
        accountStatementDTO.setAccountStatementId(STATEMENT_ID);
        accountStatementDTO.setAccountId(ACCOUNT_ID);
        accountStatementDTO.setStatementNumber(STATEMENT_NUMBER);
        accountStatementDTO.setPeriodStartDate(periodStartDate);
        accountStatementDTO.setPeriodEndDate(periodEndDate);
        accountStatementDTO.setOpeningBalance(BigDecimal.valueOf(1000));
        accountStatementDTO.setClosingBalance(BigDecimal.valueOf(1200));
        accountStatementDTO.setTotalDeposits(BigDecimal.valueOf(500));
        accountStatementDTO.setTotalWithdrawals(BigDecimal.valueOf(300));
        accountStatementDTO.setTotalFees(BigDecimal.valueOf(10));
        accountStatementDTO.setTotalInterest(BigDecimal.valueOf(10));
        accountStatementDTO.setGenerationDateTime(LocalDateTime.now());
        accountStatementDTO.setDeliveryMethod(StatementDeliveryMethodEnum.ONLINE);
        accountStatementDTO.setIsViewed(false);
    }

    @Test
    void createAccountStatement_Success() {
        // Arrange
        when(repository.findByStatementNumber(STATEMENT_NUMBER)).thenReturn(Mono.empty());
        when(mapper.toEntity(any(AccountStatementDTO.class))).thenReturn(accountStatement);
        when(repository.save(any(AccountStatement.class))).thenReturn(Mono.just(accountStatement));
        when(mapper.toDTO(any(AccountStatement.class))).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.createAccountStatement(accountStatementDTO))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByStatementNumber(STATEMENT_NUMBER);
        verify(repository).save(any(AccountStatement.class));
    }

    @Test
    void createAccountStatement_DuplicateStatementNumber() {
        // Arrange
        when(repository.findByStatementNumber(STATEMENT_NUMBER)).thenReturn(Mono.just(accountStatement));

        // Act & Assert
        StepVerifier.create(service.createAccountStatement(accountStatementDTO))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository).findByStatementNumber(STATEMENT_NUMBER);
        verify(repository, never()).save(any(AccountStatement.class));
    }

    @Test
    void getAccountStatement_Success() {
        // Arrange
        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.just(accountStatement));
        when(mapper.toDTO(accountStatement)).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountStatement(STATEMENT_ID))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findById(STATEMENT_ID);
    }

    @Test
    void getAccountStatement_NotFound() {
        // Arrange
        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getAccountStatement(STATEMENT_ID))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository).findById(STATEMENT_ID);
    }

    @Test
    void updateAccountStatement_Success() {
        // Arrange
        AccountStatement updatedStatement = new AccountStatement();
        updatedStatement.setAccountStatementId(STATEMENT_ID);
        updatedStatement.setDocumentUrl("https://example.com/statements/1.pdf");

        AccountStatementDTO updatedDTO = new AccountStatementDTO();
        updatedDTO.setAccountStatementId(STATEMENT_ID);
        updatedDTO.setDocumentUrl("https://example.com/statements/1.pdf");

        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.just(accountStatement));
        when(repository.save(any(AccountStatement.class))).thenReturn(Mono.just(updatedStatement));
        when(mapper.toDTO(updatedStatement)).thenReturn(updatedDTO);

        // Act & Assert
        StepVerifier.create(service.updateAccountStatement(STATEMENT_ID, updatedDTO))
                .expectNext(updatedDTO)
                .verifyComplete();

        verify(repository).findById(STATEMENT_ID);
        verify(repository).save(any(AccountStatement.class));
    }

    @Test
    void deleteAccountStatement_Success() {
        // Arrange
        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.just(accountStatement));
        when(repository.delete(accountStatement)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAccountStatement(STATEMENT_ID))
                .verifyComplete();

        verify(repository).findById(STATEMENT_ID);
        verify(repository).delete(accountStatement);
    }

    @Test
    void getAccountStatementsByAccountId_Success() {
        // Arrange
        when(repository.findByAccountId(ACCOUNT_ID)).thenReturn(Flux.just(accountStatement));
        when(mapper.toDTO(accountStatement)).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountStatementsByAccountId(ACCOUNT_ID))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByAccountId(ACCOUNT_ID);
    }

    @Test
    void getAccountStatementsByDateRange_Success() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        when(repository.findByAccountIdAndPeriodEndDateBetween(ACCOUNT_ID, startDate, endDate))
                .thenReturn(Flux.just(accountStatement));
        when(mapper.toDTO(accountStatement)).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountStatementsByDateRange(ACCOUNT_ID, startDate, endDate))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByAccountIdAndPeriodEndDateBetween(ACCOUNT_ID, startDate, endDate);
    }

    @Test
    void getAccountStatementByNumber_Success() {
        // Arrange
        when(repository.findByStatementNumber(STATEMENT_NUMBER)).thenReturn(Mono.just(accountStatement));
        when(mapper.toDTO(accountStatement)).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.getAccountStatementByNumber(STATEMENT_NUMBER))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByStatementNumber(STATEMENT_NUMBER);
    }

    @Test
    void markStatementAsViewed_Success() {
        // Arrange
        AccountStatement viewedStatement = new AccountStatement();
        viewedStatement.setAccountStatementId(STATEMENT_ID);
        viewedStatement.setIsViewed(true);
        // Don't use matchers in object initialization
        LocalDateTime viewedTime = LocalDateTime.now();
        viewedStatement.setFirstViewedDateTime(viewedTime);

        AccountStatementDTO viewedDTO = new AccountStatementDTO();
        viewedDTO.setAccountStatementId(STATEMENT_ID);
        viewedDTO.setIsViewed(true);
        viewedDTO.setFirstViewedDateTime(viewedTime);

        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.just(accountStatement));
        when(repository.save(any(AccountStatement.class))).thenReturn(Mono.just(viewedStatement));
        when(mapper.toDTO(viewedStatement)).thenReturn(viewedDTO);

        // Act & Assert
        StepVerifier.create(service.markStatementAsViewed(STATEMENT_ID))
                .expectNext(viewedDTO)
                .verifyComplete();

        verify(repository).findById(STATEMENT_ID);
        verify(repository).save(any(AccountStatement.class));
    }

    @Test
    void markStatementAsViewed_AlreadyViewed() {
        // Arrange
        accountStatement.setIsViewed(true);

        when(repository.findById(STATEMENT_ID)).thenReturn(Mono.just(accountStatement));

        // Act & Assert
        StepVerifier.create(service.markStatementAsViewed(STATEMENT_ID))
                .expectError(IllegalStateException.class)
                .verify();

        verify(repository).findById(STATEMENT_ID);
        verify(repository, never()).save(any(AccountStatement.class));
    }

    @Test
    void getUnviewedStatements_Success() {
        // Arrange
        when(repository.findByAccountIdAndIsViewed(ACCOUNT_ID, false)).thenReturn(Flux.just(accountStatement));
        when(mapper.toDTO(accountStatement)).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.getUnviewedStatements(ACCOUNT_ID))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByAccountIdAndIsViewed(ACCOUNT_ID, false);
    }

    @Test
    void generateStatement_Success() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        // This is a simplified test as the actual implementation would involve more complex logic
        when(repository.findByStatementNumber(any())).thenReturn(Mono.empty());
        when(mapper.toEntity(any(AccountStatementDTO.class))).thenReturn(accountStatement);
        when(repository.save(any(AccountStatement.class))).thenReturn(Mono.just(accountStatement));
        when(mapper.toDTO(any(AccountStatement.class))).thenReturn(accountStatementDTO);

        // Act & Assert
        StepVerifier.create(service.generateStatement(ACCOUNT_ID, startDate, endDate))
                .expectNext(accountStatementDTO)
                .verifyComplete();

        verify(repository).findByStatementNumber(any());
        verify(repository).save(any(AccountStatement.class));
    }
}
