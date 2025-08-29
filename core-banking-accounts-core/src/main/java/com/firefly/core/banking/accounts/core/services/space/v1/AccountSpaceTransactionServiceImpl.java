package com.firefly.core.banking.accounts.core.services.space.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.accounts.core.mappers.space.v1.SpaceTransactionMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.space.v1.SpaceTransactionDTO;
import com.firefly.core.banking.accounts.models.entities.space.v1.SpaceTransaction;
import com.firefly.core.banking.accounts.models.repositories.space.v1.SpaceTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of the AccountSpaceTransactionService interface.
 */
@Service
@Transactional
public class AccountSpaceTransactionServiceImpl implements AccountSpaceTransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountSpaceTransactionServiceImpl.class);
    
    @Autowired
    private SpaceTransactionRepository repository;
    
    @Autowired
    private SpaceTransactionMapper mapper;
    
    @Autowired
    private AccountSpaceService accountSpaceService;
    
    @Override
    public Mono<SpaceTransactionDTO> recordTransaction(Long accountSpaceId, BigDecimal amount, String description, String referenceId) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (amount == null) {
            return Mono.error(new IllegalArgumentException("Transaction amount is required"));
        }
        
        // Determine transaction type
        String transactionType = amount.compareTo(BigDecimal.ZERO) > 0 ? "DEPOSIT" : "WITHDRAWAL";
        
        // Get the account space to update its balance and record the transaction
        return accountSpaceService.getAccountSpace(accountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account space not found with ID: " + accountSpaceId)))
                .flatMap(spaceDTO -> {
                    // Calculate new balance
                    BigDecimal currentBalance = spaceDTO.getBalance();
                    BigDecimal newBalance = currentBalance.add(amount);
                    
                    // Create transaction record
                    SpaceTransactionDTO transactionDTO = new SpaceTransactionDTO();
                    transactionDTO.setAccountSpaceId(accountSpaceId);
                    transactionDTO.setAmount(amount);
                    transactionDTO.setBalanceAfterTransaction(newBalance);
                    transactionDTO.setTransactionDateTime(LocalDateTime.now());
                    transactionDTO.setDescription(description);
                    transactionDTO.setReferenceId(referenceId);
                    transactionDTO.setTransactionType(transactionType);
                    transactionDTO.setSpaceName(spaceDTO.getSpaceName());
                    transactionDTO.setAccountId(spaceDTO.getAccountId());
                    
                    SpaceTransaction transaction = mapper.toEntity(transactionDTO);
                    
                    // Update account space balance
                    spaceDTO.setBalance(newBalance);
                    
                    // Save transaction and update space balance
                    return repository.save(transaction)
                            .flatMap(savedTransaction -> 
                                accountSpaceService.updateAccountSpace(accountSpaceId, spaceDTO)
                                    .thenReturn(savedTransaction)
                            )
                            .map(mapper::toDTO);
                });
    }
    
    @Override
    public Mono<PaginationResponse<SpaceTransactionDTO>> getTransactions(Long accountSpaceId, PaginationRequest paginationRequest) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceId(accountSpaceId, pageable),
                () -> repository.countByAccountSpaceId(accountSpaceId)
        );
    }
    
    @Override
    public Mono<PaginationResponse<SpaceTransactionDTO>> getTransactionsByDateRange(
            Long accountSpaceId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            PaginationRequest paginationRequest) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (startDate == null) {
            return Mono.error(new IllegalArgumentException("Start date is required"));
        }
        if (endDate == null) {
            return Mono.error(new IllegalArgumentException("End date is required"));
        }
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException("Start date must be before or equal to end date"));
        }
        
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountSpaceIdAndTransactionDateTimeBetween(
                        accountSpaceId, startDate, endDate, pageable),
                () -> repository.countByAccountSpaceIdAndTransactionDateTimeBetween(
                        accountSpaceId, startDate, endDate)
        );
    }
    
    @Override
    public Mono<BigDecimal> calculateTotalDeposits(Long accountSpaceId, LocalDateTime startDate, LocalDateTime endDate) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (startDate == null) {
            return Mono.error(new IllegalArgumentException("Start date is required"));
        }
        if (endDate == null) {
            return Mono.error(new IllegalArgumentException("End date is required"));
        }
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException("Start date must be before or equal to end date"));
        }
        
        return repository.calculateTotalDeposits(accountSpaceId, startDate, endDate);
    }
    
    @Override
    public Mono<BigDecimal> calculateTotalWithdrawals(Long accountSpaceId, LocalDateTime startDate, LocalDateTime endDate) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (startDate == null) {
            return Mono.error(new IllegalArgumentException("Start date is required"));
        }
        if (endDate == null) {
            return Mono.error(new IllegalArgumentException("End date is required"));
        }
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException("Start date must be before or equal to end date"));
        }
        
        return repository.calculateTotalWithdrawals(accountSpaceId, startDate, endDate);
    }
    
    @Override
    public Mono<BigDecimal> getBalanceAtDateTime(Long accountSpaceId, LocalDateTime dateTime) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required"));
        }
        if (dateTime == null) {
            return Mono.error(new IllegalArgumentException("Date time is required"));
        }
        
        return repository.findFirstByAccountSpaceIdAndTransactionDateTimeLessThanEqualOrderByTransactionDateTimeDesc(
                accountSpaceId, dateTime)
                .map(SpaceTransaction::getBalanceAfterTransaction)
                .switchIfEmpty(Mono.just(BigDecimal.ZERO));
    }
}
