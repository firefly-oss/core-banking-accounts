package com.catalis.core.banking.accounts.core.services.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.mappers.models.statement.v1.AccountStatementMapper;
import com.catalis.core.banking.accounts.core.services.space.v1.AccountSpaceService;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.statement.v1.AccountStatementDTO;
import com.catalis.core.banking.accounts.models.entities.statement.v1.AccountStatement;
import com.catalis.core.banking.accounts.models.repositories.statement.v1.AccountStatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountStatementServiceImpl implements AccountStatementService {

    private static final Logger logger = LoggerFactory.getLogger(AccountStatementServiceImpl.class);

    private static final String ERROR_STATEMENT_ID_REQUIRED = "Account statement ID is required";
    private static final String ERROR_STATEMENT_NOT_FOUND = "Account statement not found with ID: %d";
    private static final String ERROR_ACCOUNT_ID_REQUIRED = "Account ID is required";
    private static final String ERROR_STATEMENT_NUMBER_REQUIRED = "Statement number is required";
    private static final String ERROR_PERIOD_START_DATE_REQUIRED = "Period start date is required";
    private static final String ERROR_PERIOD_END_DATE_REQUIRED = "Period end date is required";
    private static final String ERROR_OPENING_BALANCE_REQUIRED = "Opening balance is required";
    private static final String ERROR_CLOSING_BALANCE_REQUIRED = "Closing balance is required";
    private static final String ERROR_GENERATION_DATE_REQUIRED = "Generation date is required";
    private static final String ERROR_DELIVERY_METHOD_REQUIRED = "Delivery method is required";
    private static final String ERROR_STATEMENT_ALREADY_VIEWED = "Statement is already viewed";
    private static final String ERROR_INVALID_DATE_RANGE = "Invalid date range: start date must be before or equal to end date";
    private static final String ERROR_STATEMENT_NUMBER_EXISTS = "Statement number already exists: %s";

    @Autowired
    private AccountStatementRepository repository;

    @Autowired
    private AccountStatementMapper mapper;

    @Autowired
    private AccountSpaceService accountSpaceService;

    @Override
    public Mono<AccountStatementDTO> createAccountStatement(AccountStatementDTO accountStatementDTO) {
        // Validate required fields
        if (accountStatementDTO.getAccountId() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }
        if (accountStatementDTO.getStatementNumber() == null || accountStatementDTO.getStatementNumber().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_NUMBER_REQUIRED));
        }
        if (accountStatementDTO.getPeriodStartDate() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_START_DATE_REQUIRED));
        }
        if (accountStatementDTO.getPeriodEndDate() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_END_DATE_REQUIRED));
        }
        if (accountStatementDTO.getOpeningBalance() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_OPENING_BALANCE_REQUIRED));
        }
        if (accountStatementDTO.getClosingBalance() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_CLOSING_BALANCE_REQUIRED));
        }
        if (accountStatementDTO.getGenerationDateTime() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_GENERATION_DATE_REQUIRED));
        }
        if (accountStatementDTO.getDeliveryMethod() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_DELIVERY_METHOD_REQUIRED));
        }

        // Validate date range
        if (accountStatementDTO.getPeriodStartDate().isAfter(accountStatementDTO.getPeriodEndDate())) {
            return Mono.error(new IllegalArgumentException(ERROR_INVALID_DATE_RANGE));
        }

        // Check if statement number already exists
        return repository.findByStatementNumber(accountStatementDTO.getStatementNumber())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException(
                                String.format(ERROR_STATEMENT_NUMBER_EXISTS, accountStatementDTO.getStatementNumber())));
                    }

                    // Set default values if not provided
                    if (accountStatementDTO.getTotalDeposits() == null) {
                        accountStatementDTO.setTotalDeposits(BigDecimal.ZERO);
                    }
                    if (accountStatementDTO.getTotalWithdrawals() == null) {
                        accountStatementDTO.setTotalWithdrawals(BigDecimal.ZERO);
                    }
                    if (accountStatementDTO.getTotalFees() == null) {
                        accountStatementDTO.setTotalFees(BigDecimal.ZERO);
                    }
                    if (accountStatementDTO.getTotalInterest() == null) {
                        accountStatementDTO.setTotalInterest(BigDecimal.ZERO);
                    }
                    if (accountStatementDTO.getIsViewed() == null) {
                        accountStatementDTO.setIsViewed(false);
                    }

                    AccountStatement accountStatement = mapper.toEntity(accountStatementDTO);
                    return repository.save(accountStatement)
                            .map(mapper::toDTO);
                });
    }

    @Override
    public Mono<AccountStatementDTO> getAccountStatement(Long accountStatementId) {
        if (accountStatementId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_ID_REQUIRED));
        }

        return repository.findById(accountStatementId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_STATEMENT_NOT_FOUND, accountStatementId))));
    }

    @Override
    public Mono<AccountStatementDTO> updateAccountStatement(Long accountStatementId, AccountStatementDTO accountStatementDTO) {
        if (accountStatementId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_ID_REQUIRED));
        }

        return repository.findById(accountStatementId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_STATEMENT_NOT_FOUND, accountStatementId))))
                .flatMap(existingStatement -> {
                    // Update fields
                    if (accountStatementDTO.getStatementNumber() != null) {
                        existingStatement.setStatementNumber(accountStatementDTO.getStatementNumber());
                    }
                    if (accountStatementDTO.getPeriodStartDate() != null) {
                        existingStatement.setPeriodStartDate(accountStatementDTO.getPeriodStartDate());
                    }
                    if (accountStatementDTO.getPeriodEndDate() != null) {
                        existingStatement.setPeriodEndDate(accountStatementDTO.getPeriodEndDate());
                    }
                    if (accountStatementDTO.getOpeningBalance() != null) {
                        existingStatement.setOpeningBalance(accountStatementDTO.getOpeningBalance());
                    }
                    if (accountStatementDTO.getClosingBalance() != null) {
                        existingStatement.setClosingBalance(accountStatementDTO.getClosingBalance());
                    }
                    if (accountStatementDTO.getTotalDeposits() != null) {
                        existingStatement.setTotalDeposits(accountStatementDTO.getTotalDeposits());
                    }
                    if (accountStatementDTO.getTotalWithdrawals() != null) {
                        existingStatement.setTotalWithdrawals(accountStatementDTO.getTotalWithdrawals());
                    }
                    if (accountStatementDTO.getTotalFees() != null) {
                        existingStatement.setTotalFees(accountStatementDTO.getTotalFees());
                    }
                    if (accountStatementDTO.getTotalInterest() != null) {
                        existingStatement.setTotalInterest(accountStatementDTO.getTotalInterest());
                    }
                    if (accountStatementDTO.getGenerationDateTime() != null) {
                        existingStatement.setGenerationDateTime(accountStatementDTO.getGenerationDateTime());
                    }
                    if (accountStatementDTO.getDeliveryMethod() != null) {
                        existingStatement.setDeliveryMethod(accountStatementDTO.getDeliveryMethod());
                    }
                    if (accountStatementDTO.getDeliveryDateTime() != null) {
                        existingStatement.setDeliveryDateTime(accountStatementDTO.getDeliveryDateTime());
                    }
                    if (accountStatementDTO.getDocumentUrl() != null) {
                        existingStatement.setDocumentUrl(accountStatementDTO.getDocumentUrl());
                    }
                    if (accountStatementDTO.getIsViewed() != null) {
                        existingStatement.setIsViewed(accountStatementDTO.getIsViewed());
                    }
                    if (accountStatementDTO.getFirstViewedDateTime() != null) {
                        existingStatement.setFirstViewedDateTime(accountStatementDTO.getFirstViewedDateTime());
                    }

                    return repository.save(existingStatement);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAccountStatement(Long accountStatementId) {
        if (accountStatementId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_ID_REQUIRED));
        }

        return repository.findById(accountStatementId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_STATEMENT_NOT_FOUND, accountStatementId))))
                .flatMap(statement -> repository.delete(statement));
    }

    @Override
    public Flux<AccountStatementDTO> getAccountStatementsByAccountId(Long accountId) {
        if (accountId == null) {
            return Flux.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }

        return repository.findByAccountId(accountId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<AccountStatementDTO> getAccountStatementsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate) {
        if (accountId == null) {
            return Flux.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }
        if (startDate == null) {
            return Flux.error(new IllegalArgumentException(ERROR_PERIOD_START_DATE_REQUIRED));
        }
        if (endDate == null) {
            return Flux.error(new IllegalArgumentException(ERROR_PERIOD_END_DATE_REQUIRED));
        }
        if (startDate.isAfter(endDate)) {
            return Flux.error(new IllegalArgumentException(ERROR_INVALID_DATE_RANGE));
        }

        return repository.findByAccountIdAndPeriodEndDateBetween(accountId, startDate, endDate)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatementDTO> getAccountStatementByNumber(String statementNumber) {
        if (statementNumber == null || statementNumber.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_NUMBER_REQUIRED));
        }

        return repository.findByStatementNumber(statementNumber)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<AccountStatementDTO> markStatementAsViewed(Long accountStatementId) {
        if (accountStatementId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_STATEMENT_ID_REQUIRED));
        }

        return repository.findById(accountStatementId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_STATEMENT_NOT_FOUND, accountStatementId))))
                .flatMap(statement -> {
                    if (statement.getIsViewed()) {
                        return Mono.error(new IllegalStateException(ERROR_STATEMENT_ALREADY_VIEWED));
                    }

                    statement.setIsViewed(true);
                    statement.setFirstViewedDateTime(LocalDateTime.now());

                    return repository.save(statement);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Flux<AccountStatementDTO> getUnviewedStatements(Long accountId) {
        if (accountId == null) {
            return Flux.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }

        return repository.findByAccountIdAndIsViewed(accountId, false)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatementDTO> generateStatement(Long accountId, LocalDate startDate, LocalDate endDate) {
        // This would typically involve complex business logic to:
        // 1. Retrieve all transactions for the account within the date range
        // 2. Calculate opening and closing balances
        // 3. Calculate totals for deposits, withdrawals, fees, and interest
        // 4. Generate a statement number
        // 5. Create and save the statement record
        // 6. Generate the statement document (PDF, etc.)
        // 7. Update the statement record with the document URL

        // For now, we'll implement a simplified version that just creates a statement record
        if (accountId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }
        if (startDate == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_START_DATE_REQUIRED));
        }
        if (endDate == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_END_DATE_REQUIRED));
        }
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException(ERROR_INVALID_DATE_RANGE));
        }

        // Generate a statement number
        String statementNumber = String.format("STMT-%s-%d",
                endDate.toString().replace("-", ""),
                accountId);

        // Create a new statement DTO
        AccountStatementDTO statementDTO = new AccountStatementDTO();
        statementDTO.setAccountId(accountId);
        statementDTO.setStatementNumber(statementNumber);
        statementDTO.setPeriodStartDate(startDate);
        statementDTO.setPeriodEndDate(endDate);
        statementDTO.setOpeningBalance(BigDecimal.ZERO); // Placeholder
        statementDTO.setClosingBalance(BigDecimal.ZERO); // Placeholder
        statementDTO.setTotalDeposits(BigDecimal.ZERO); // Placeholder
        statementDTO.setTotalWithdrawals(BigDecimal.ZERO); // Placeholder
        statementDTO.setTotalFees(BigDecimal.ZERO); // Placeholder
        statementDTO.setTotalInterest(BigDecimal.ZERO); // Placeholder
        statementDTO.setGenerationDateTime(LocalDateTime.now());
        statementDTO.setDeliveryMethod(com.catalis.core.banking.accounts.interfaces.enums.statement.v1.StatementDeliveryMethodEnum.ONLINE);
        statementDTO.setIsViewed(false);

        // Create the statement
        return createAccountStatement(statementDTO);
    }

    @Override
    public Mono<PaginationResponse<AccountStatementDTO>> listAccountStatements(FilterRequest filterRequest) {
        return FilterUtils
                .createFilter(
                        AccountStatement.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<AccountStatementDTO> generateSpaceStatement(Long accountId, Long accountSpaceId, LocalDate startDate, LocalDate endDate) {
        // Validate inputs
        if (accountId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (startDate == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_START_DATE_REQUIRED));
        }
        if (endDate == null) {
            return Mono.error(new IllegalArgumentException(ERROR_PERIOD_END_DATE_REQUIRED));
        }
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException(ERROR_INVALID_DATE_RANGE));
        }

        // First, verify the account space exists and belongs to the account
        return accountSpaceService.getAccountSpace(accountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account space not found with ID: " + accountSpaceId)))
                .flatMap(spaceDTO -> {
                    if (!spaceDTO.getAccountId().equals(accountId)) {
                        return Mono.error(new IllegalArgumentException("Account space does not belong to the specified account"));
                    }

                    // Generate a statement number specific to the space
                    String statementNumber = String.format("SPACE-STMT-%s-%d-%d",
                            endDate.toString().replace("-", ""),
                            accountId,
                            accountSpaceId);

                    // Create a new statement DTO
                    AccountStatementDTO statementDTO = new AccountStatementDTO();
                    statementDTO.setAccountId(accountId);
                    statementDTO.setStatementNumber(statementNumber);
                    statementDTO.setPeriodStartDate(startDate);
                    statementDTO.setPeriodEndDate(endDate);
                    statementDTO.setOpeningBalance(BigDecimal.ZERO); // Would be calculated from historical data
                    statementDTO.setClosingBalance(spaceDTO.getBalance()); // Current balance of the space
                    statementDTO.setTotalDeposits(BigDecimal.ZERO); // Would be calculated from transaction history
                    statementDTO.setTotalWithdrawals(BigDecimal.ZERO); // Would be calculated from transaction history
                    statementDTO.setTotalFees(BigDecimal.ZERO); // Would be calculated from transaction history
                    statementDTO.setTotalInterest(BigDecimal.ZERO); // Would be calculated from transaction history
                    statementDTO.setGenerationDateTime(LocalDateTime.now());
                    statementDTO.setDeliveryMethod(com.catalis.core.banking.accounts.interfaces.enums.statement.v1.StatementDeliveryMethodEnum.ONLINE);
                    statementDTO.setIsViewed(false);

                    // Add space-specific metadata
                    statementDTO.setMetadata(String.format("{\"accountSpaceId\":%d,\"spaceName\":\"%s\",\"spaceType\":\"%s\"}",
                            spaceDTO.getAccountSpaceId(),
                            spaceDTO.getSpaceName(),
                            spaceDTO.getSpaceType().name()));

                    // Create the statement
                    return createAccountStatement(statementDTO);
                });
    }

    @Override
    public Flux<AccountStatementDTO> getStatementsByAccountSpace(Long accountSpaceId) {
        if (accountSpaceId == null) {
            return Flux.error(new IllegalArgumentException("Account space ID is required"));
        }

        // Find statements that contain the account space ID in their metadata
        return repository.findAll()
                .filter(statement -> {
                    String metadata = statement.getMetadata();
                    if (metadata == null || metadata.isEmpty()) {
                        return false;
                    }
                    return metadata.contains(String.format("\"accountSpaceId\":%d", accountSpaceId));
                })
                .map(mapper::toDTO);
    }
}
