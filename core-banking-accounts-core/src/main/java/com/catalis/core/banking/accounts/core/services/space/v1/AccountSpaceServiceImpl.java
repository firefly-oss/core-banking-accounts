package com.catalis.core.banking.accounts.core.services.space.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.mappers.models.space.v1.AccountSpaceMapper;
import com.catalis.core.banking.accounts.core.services.core.v1.AccountBalanceService;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.TransferFrequencyEnum;
import com.catalis.core.banking.accounts.models.entities.space.v1.AccountSpace;
import com.catalis.core.banking.accounts.models.repositories.space.v1.AccountSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AccountSpaceServiceImpl implements AccountSpaceService {

    private static final Logger logger = LoggerFactory.getLogger(AccountSpaceServiceImpl.class);
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    // Error messages
    private static final String ERROR_SPACE_NOT_FOUND = "Account space not found with ID: %d";
    private static final String ERROR_INSUFFICIENT_FUNDS = "Insufficient funds in source space. Available: %s, Requested: %s";
    private static final String ERROR_NEGATIVE_AMOUNT = "Transfer amount must be positive, got: %s";
    private static final String ERROR_DIFFERENT_ACCOUNTS = "Cannot transfer between spaces of different accounts";
    private static final String ERROR_MAIN_SPACE_DELETION = "Cannot delete the main account space";
    private static final String ERROR_INVALID_TRANSFER_CONFIG = "Frequency and amount are required and amount must be positive when enabling automatic transfers";
    private static final String ERROR_SOURCE_SPACE_INVALID = "Source space must belong to the same account";
    private static final String ERROR_INVALID_DATE_RANGE = "Invalid date range: start date must be before end date";
    private static final String ERROR_MONTHS_POSITIVE = "Months must be positive, got: %d";

    @Autowired
    private AccountSpaceRepository repository;

    @Autowired
    private AccountSpaceMapper mapper;

    @Autowired
    private AccountBalanceService accountBalanceService;

    @Override
    public Mono<PaginationResponse<AccountSpaceDTO>> filterAccountSpaces(FilterRequest<AccountSpaceDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        AccountSpace.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    private static final String ERROR_ACCOUNT_ID_REQUIRED = "Account ID is required for creating a space";
    private static final String ERROR_SPACE_NAME_REQUIRED = "Space name is required";
    private static final String ERROR_SPACE_TYPE_REQUIRED = "Space type is required";
    private static final String ERROR_NEGATIVE_BALANCE = "Balance cannot be negative";
    private static final String ERROR_NEGATIVE_TARGET = "Target amount cannot be negative";
    private static final String ERROR_TARGET_DATE_PAST = "Target date cannot be in the past";

    @Override
    public Mono<AccountSpaceDTO> createAccountSpace(AccountSpaceDTO accountSpaceDTO) {
        // Validate required fields
        if (accountSpaceDTO.getAccountId() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_ACCOUNT_ID_REQUIRED));
        }

        if (accountSpaceDTO.getSpaceName() == null || accountSpaceDTO.getSpaceName().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(ERROR_SPACE_NAME_REQUIRED));
        }

        if (accountSpaceDTO.getSpaceType() == null) {
            return Mono.error(new IllegalArgumentException(ERROR_SPACE_TYPE_REQUIRED));
        }

        // Ensure new spaces have a non-null balance
        if (accountSpaceDTO.getBalance() == null) {
            accountSpaceDTO.setBalance(BigDecimal.ZERO);
        } else if (accountSpaceDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new IllegalArgumentException(ERROR_NEGATIVE_BALANCE));
        }

        // Validate target amount if present
        if (accountSpaceDTO.getTargetAmount() != null &&
            accountSpaceDTO.getTargetAmount().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new IllegalArgumentException(ERROR_NEGATIVE_TARGET));
        }

        // Validate target date if present
        if (accountSpaceDTO.getTargetDate() != null &&
            accountSpaceDTO.getTargetDate().isBefore(LocalDateTime.now())) {
            return Mono.error(new IllegalArgumentException(ERROR_TARGET_DATE_PAST));
        }

        // Ensure isVisible is set if null
        if (accountSpaceDTO.getIsVisible() == null) {
            accountSpaceDTO.setIsVisible(true);
        }

        AccountSpace accountSpace = mapper.toEntity(accountSpaceDTO);
        return repository.save(accountSpace)
                .map(mapper::toDTO);
    }

    private static final String ERROR_SPACE_ID_REQUIRED_GET = "Account space ID is required";
    private static final String ERROR_SPACE_NOT_FOUND_GET = "Account space not found with ID: %d";

    @Override
    public Mono<AccountSpaceDTO> getAccountSpace(Long accountSpaceId) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_SPACE_ID_REQUIRED_GET));
        }

        return repository.findById(accountSpaceId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.empty());
    }

    private static final String ERROR_SPACE_NOT_FOUND_UPDATE = "Account space not found for update with ID: %d";
    private static final String ERROR_CANNOT_CHANGE_TYPE = "Cannot change the type of an existing space";

    @Override
    public Mono<AccountSpaceDTO> updateAccountSpace(Long accountSpaceId, AccountSpaceDTO accountSpaceDTO) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Account space ID is required for update"));
        }

        return repository.findById(accountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format(ERROR_SPACE_NOT_FOUND_UPDATE, accountSpaceId))))
                .flatMap(existingSpace -> {
                    // Validate space name if provided
                    if (accountSpaceDTO.getSpaceName() != null && accountSpaceDTO.getSpaceName().trim().isEmpty()) {
                        return Mono.error(new IllegalArgumentException(ERROR_SPACE_NAME_REQUIRED));
                    }

                    // Validate space type - don't allow changing the type
                    if (accountSpaceDTO.getSpaceType() != null &&
                        existingSpace.getSpaceType() != accountSpaceDTO.getSpaceType()) {
                        return Mono.error(new IllegalArgumentException(ERROR_CANNOT_CHANGE_TYPE));
                    }

                    // Validate balance if provided
                    if (accountSpaceDTO.getBalance() != null &&
                        accountSpaceDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new IllegalArgumentException(ERROR_NEGATIVE_BALANCE));
                    }

                    // Validate target amount if provided
                    if (accountSpaceDTO.getTargetAmount() != null &&
                        accountSpaceDTO.getTargetAmount().compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new IllegalArgumentException(ERROR_NEGATIVE_TARGET));
                    }

                    // Validate target date if provided
                    if (accountSpaceDTO.getTargetDate() != null &&
                        accountSpaceDTO.getTargetDate().isBefore(LocalDateTime.now())) {
                        return Mono.error(new IllegalArgumentException(ERROR_TARGET_DATE_PAST));
                    }

                    AccountSpace updatedSpace = mapper.toEntity(accountSpaceDTO);
                    updatedSpace.setAccountSpaceId(accountSpaceId);
                    // Preserve the accountId from the existing space
                    updatedSpace.setAccountId(existingSpace.getAccountId());

                    // Preserve the space type from the existing space
                    updatedSpace.setSpaceType(existingSpace.getSpaceType());

                    return repository.save(updatedSpace);
                })
                .map(mapper::toDTO);
    }

    private static final String ERROR_SPACE_ID_REQUIRED = "Account space ID is required for deletion";
    private static final String ERROR_SPACE_NOT_FOUND_DELETE = "Account space not found for deletion with ID: %d";
    private static final String ERROR_NON_ZERO_BALANCE = "Cannot delete space with non-zero balance. Transfer funds first.";

    @Override
    public Mono<Void> deleteAccountSpace(Long accountSpaceId) {
        if (accountSpaceId == null) {
            return Mono.error(new IllegalArgumentException(ERROR_SPACE_ID_REQUIRED));
        }

        return repository.findById(accountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_SPACE_NOT_FOUND_DELETE, accountSpaceId))))
                .flatMap(space -> {
                    // Don't allow deletion of MAIN spaces
                    if (space.getSpaceType() == AccountSpaceTypeEnum.MAIN) {
                        return Mono.error(new IllegalStateException(ERROR_MAIN_SPACE_DELETION));
                    }

                    // Don't allow deletion of spaces with non-zero balance
                    if (space.getBalance() != null && space.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                        return Mono.error(new IllegalStateException(ERROR_NON_ZERO_BALANCE));
                    }

                    return repository.delete(space);
                });
    }

    @Override
    public Flux<AccountSpaceDTO> getAccountSpacesByAccountId(Long accountId) {
        return repository.findByAccountId(accountId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<AccountSpaceDTO>> getAccountSpacesByAccountId(Long accountId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return repository.countByAccountId(accountId)
                .flatMap(total ->
                    repository.findByAccountId(accountId, pageRequest)
                        .map(mapper::toDTO)
                        .collectList()
                        .map(items -> new PaginationResponse<>(
                            items,
                            page,
                            size,
                            total.intValue()
                        ))
                );
    }

    private static final String ERROR_SPACE_NOT_FOUND_TRANSFER = "Account space not found for transfer with ID: %d";
    private static final String ERROR_SAME_SPACE = "Cannot transfer funds to the same space";

    @Override
    public Mono<Boolean> transferBetweenSpaces(Long fromAccountSpaceId, Long toAccountSpaceId, BigDecimal amount) {
        // Validate IDs
        if (fromAccountSpaceId == null || toAccountSpaceId == null) {
            return Mono.error(new IllegalArgumentException("Source and destination space IDs are required"));
        }

        // Validate not same space
        if (fromAccountSpaceId.equals(toAccountSpaceId)) {
            return Mono.error(new IllegalArgumentException(ERROR_SAME_SPACE));
        }

        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException(String.format(ERROR_NEGATIVE_AMOUNT, amount)));
        }

        // Check that spaces exist and belong to the same account
        Mono<AccountSpace> fromSpaceMono = repository.findById(fromAccountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_SPACE_NOT_FOUND_TRANSFER, fromAccountSpaceId))));

        Mono<AccountSpace> toSpaceMono = repository.findById(toAccountSpaceId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        String.format(ERROR_SPACE_NOT_FOUND_TRANSFER, toAccountSpaceId))));

        return Mono.zip(fromSpaceMono, toSpaceMono)
            .flatMap(tuple -> {
                AccountSpace fromSpace = tuple.getT1();
                AccountSpace toSpace = tuple.getT2();

                // Verify spaces belong to the same account
                if (!fromSpace.getAccountId().equals(toSpace.getAccountId())) {
                    return Mono.error(new IllegalArgumentException(ERROR_DIFFERENT_ACCOUNTS));
                }

                // Verify sufficient funds
                if (fromSpace.getBalance().compareTo(amount) < 0) {
                    return Mono.error(new IllegalArgumentException(
                            String.format(ERROR_INSUFFICIENT_FUNDS,
                                    fromSpace.getBalance().toString(), amount.toString())));
                }

                // Update balances
                BigDecimal fromSpaceNewBalance = fromSpace.getBalance().subtract(amount);
                BigDecimal toSpaceNewBalance = toSpace.getBalance().add(amount);
                fromSpace.setBalance(fromSpaceNewBalance);
                toSpace.setBalance(toSpaceNewBalance);

                // Create balance history records
                LocalDateTime now = LocalDateTime.now();
                Long accountId = fromSpace.getAccountId(); // Both spaces have the same accountId

                // Create balance record for source space
                AccountBalanceDTO fromSpaceBalanceDTO = AccountBalanceDTO.builder()
                        .accountId(accountId)
                        .accountSpaceId(fromAccountSpaceId)
                        .balanceType(BalanceTypeEnum.CURRENT)
                        .balanceAmount(fromSpaceNewBalance)
                        .asOfDatetime(now)
                        .build();

                // Create balance record for destination space
                AccountBalanceDTO toSpaceBalanceDTO = AccountBalanceDTO.builder()
                        .accountId(accountId)
                        .accountSpaceId(toAccountSpaceId)
                        .balanceType(BalanceTypeEnum.CURRENT)
                        .balanceAmount(toSpaceNewBalance)
                        .asOfDatetime(now)
                        .build();

                // Save spaces and balance records
                return Mono.when(
                    repository.save(fromSpace),
                    repository.save(toSpace),
                    accountBalanceService.createBalance(accountId, fromSpaceBalanceDTO),
                    accountBalanceService.createBalance(accountId, toSpaceBalanceDTO)
                ).thenReturn(true);
            });
    }

    // ===== Goal Tracking Methods =====

    @Override
    public Mono<AccountSpaceDTO> calculateGoalProgress(Long accountSpaceId) {
        return repository.findById(accountSpaceId)
                .flatMap(space -> {
                    // If there's no target amount, we can't calculate progress
                    if (space.getTargetAmount() == null) {
                        return Mono.just(mapper.toDTO(space));
                    }

                    AccountSpaceDTO dto = mapper.toDTO(space);

                    // Calculate progress percentage
                    BigDecimal progressPercentage = space.getBalance()
                            .multiply(ONE_HUNDRED)
                            .divide(space.getTargetAmount(), MATH_CONTEXT);
                    dto.setGoalProgressPercentage(progressPercentage);

                    // Calculate remaining amount
                    BigDecimal remaining = space.getTargetAmount().subtract(space.getBalance());
                    if (remaining.compareTo(BigDecimal.ZERO) < 0) {
                        remaining = BigDecimal.ZERO;
                    }
                    dto.setRemainingToTarget(remaining);

                    // Determine if goal is completed
                    boolean isCompleted = space.getBalance().compareTo(space.getTargetAmount()) >= 0;
                    dto.setIsGoalCompleted(isCompleted);

                    // Calculate estimated completion date if we have a target date
                    if (space.getTargetDate() != null && !isCompleted) {
                        // If balance is zero, we can't estimate
                        if (space.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                            return Mono.just(dto);
                        }

                        // Calculate average daily growth based on current balance and creation date
                        LocalDateTime now = LocalDateTime.now();
                        long daysSinceCreation = ChronoUnit.DAYS.between(space.getDateCreated(), now);
                        if (daysSinceCreation <= 0) {
                            daysSinceCreation = 1; // Avoid division by zero
                        }

                        BigDecimal dailyGrowth = space.getBalance().divide(new BigDecimal(daysSinceCreation), MATH_CONTEXT);

                        // If no growth, we can't estimate
                        if (dailyGrowth.compareTo(BigDecimal.ZERO) <= 0) {
                            return Mono.just(dto);
                        }

                        // Calculate days needed to reach target
                        BigDecimal daysNeeded = remaining.divide(dailyGrowth, MATH_CONTEXT);

                        // Calculate estimated completion date
                        LocalDateTime estimatedDate = now.plusDays(daysNeeded.longValue());
                        dto.setEstimatedCompletionDate(estimatedDate);
                    }

                    return Mono.just(dto);
                });
    }

    @Override
    public Flux<AccountSpaceDTO> getSpacesWithGoals(Long accountId) {
        return repository.findByAccountIdAndTargetAmountIsNotNull(accountId)
                .flatMap(space -> Mono.just(space)
                        .flatMap(s -> calculateGoalProgress(s.getAccountSpaceId())));
    }

    @Override
    public Flux<AccountSpaceDTO> getSpacesWithUpcomingTargetDates(Long accountId, int daysThreshold) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdDate = now.plusDays(daysThreshold);

        return repository.findByAccountIdAndTargetDateGreaterThanEqual(accountId, now)
                .filter(space -> space.getTargetDate().isBefore(thresholdDate))
                .flatMap(space -> Mono.just(space)
                        .flatMap(s -> calculateGoalProgress(s.getAccountSpaceId())));
    }

    // ===== Automatic Transfer Methods =====

    @Override
    public Mono<AccountSpaceDTO> configureAutomaticTransfers(
            Long accountSpaceId,
            Boolean enabled,
            TransferFrequencyEnum frequency,
            BigDecimal amount,
            Long sourceSpaceId
    ) {
        return repository.findById(accountSpaceId)
                .flatMap(space -> {
                    // Validate parameters
                    if (enabled && (frequency == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)) {
                        return Mono.error(new IllegalArgumentException(
                                "Frequency and amount are required and amount must be positive when enabling automatic transfers"));
                    }

                    // If source space is provided, verify it exists and belongs to the same account
                    Mono<Boolean> sourceSpaceCheck = sourceSpaceId == null ?
                            Mono.just(true) :
                            repository.findById(sourceSpaceId)
                                    .map(sourceSpace -> sourceSpace.getAccountId().equals(space.getAccountId()))
                                    .defaultIfEmpty(false);

                    return sourceSpaceCheck.flatMap(validSource -> {
                        if (!validSource) {
                            return Mono.error(new IllegalArgumentException(
                                    "Source space must belong to the same account"));
                        }

                        // Update space with automatic transfer configuration
                        space.setEnableAutomaticTransfers(enabled);
                        if (enabled) {
                            space.setTransferFrequency(frequency);
                            space.setTransferAmount(amount);
                            space.setSourceSpaceId(sourceSpaceId);
                        } else {
                            // Clear configuration if disabling
                            space.setTransferFrequency(null);
                            space.setTransferAmount(null);
                            space.setSourceSpaceId(null);
                        }

                        return repository.save(space).map(mapper::toDTO);
                    });
                });
    }

    @Override
    public Mono<Integer> executeAutomaticTransfers(Long accountId) {
        // Find all spaces with automatic transfers enabled for this account
        return repository.findByAccountId(accountId)
                .filter(space -> Boolean.TRUE.equals(space.getEnableAutomaticTransfers()))
                .flatMap(space -> {
                    // Skip if missing required configuration
                    if (space.getTransferFrequency() == null ||
                        space.getTransferAmount() == null ||
                        space.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        logger.warn("Skipping automatic transfer for space {} due to invalid configuration",
                                space.getAccountSpaceId());
                        return Mono.empty();
                    }

                    // Determine source space ID
                    Long sourceSpaceId = space.getSourceSpaceId();
                    if (sourceSpaceId == null) {
                        // Find MAIN space if source not specified
                        return repository.findByAccountIdAndSpaceType(accountId, AccountSpaceTypeEnum.MAIN)
                                .next()
                                .flatMap(mainSpace ->
                                    transferBetweenSpaces(mainSpace.getAccountSpaceId(), space.getAccountSpaceId(),
                                            space.getTransferAmount())
                                    .thenReturn(1)
                                    .onErrorResume(e -> {
                                        logger.error("Error executing automatic transfer from MAIN to {}: {}",
                                                space.getAccountSpaceId(), e.getMessage());
                                        return Mono.just(0);
                                    })
                                );
                    } else {
                        // Use specified source space
                        return transferBetweenSpaces(sourceSpaceId, space.getAccountSpaceId(),
                                space.getTransferAmount())
                                .thenReturn(1)
                                .onErrorResume(e -> {
                                    logger.error("Error executing automatic transfer from {} to {}: {}",
                                            sourceSpaceId, space.getAccountSpaceId(), e.getMessage());
                                    return Mono.just(0);
                                });
                    }
                })
                .reduce(0, Integer::sum);
    }

    @Override
    public Mono<Map<Long, BigDecimal>> simulateFutureBalances(Long accountId, int months) {
        if (months <= 0) {
            return Mono.error(new IllegalArgumentException("Months must be positive"));
        }

        // Get all spaces for the account
        return repository.findByAccountId(accountId)
                .collectList()
                .flatMap(spaces -> {
                    Map<Long, BigDecimal> projectedBalances = new HashMap<>();

                    // Initialize with current balances
                    spaces.forEach(space ->
                        projectedBalances.put(space.getAccountSpaceId(), space.getBalance())
                    );

                    // Get spaces with automatic transfers
                    Map<Long, AccountSpace> spacesWithTransfers = new HashMap<>();
                    Map<Long, AccountSpace> allSpacesMap = new HashMap<>();

                    spaces.forEach(space -> {
                        allSpacesMap.put(space.getAccountSpaceId(), space);
                        if (Boolean.TRUE.equals(space.getEnableAutomaticTransfers()) &&
                            space.getTransferFrequency() != null &&
                            space.getTransferAmount() != null &&
                            space.getTransferAmount().compareTo(BigDecimal.ZERO) > 0) {
                            spacesWithTransfers.put(space.getAccountSpaceId(), space);
                        }
                    });

                    // Find MAIN space
                    AccountSpace mainSpace = spaces.stream()
                            .filter(s -> s.getSpaceType() == AccountSpaceTypeEnum.MAIN)
                            .findFirst()
                            .orElse(null);

                    // Simulate transfers for each month
                    for (int month = 0; month < months; month++) {
                        // Process each space with automatic transfers
                        for (AccountSpace space : spacesWithTransfers.values()) {
                            // Skip if no transfer frequency or amount
                            if (space.getTransferFrequency() == null || space.getTransferAmount() == null) {
                                continue;
                            }

                            // Determine if transfer should occur this month based on frequency
                            boolean shouldTransfer = false;
                            switch (space.getTransferFrequency()) {
                                case DAILY:
                                    shouldTransfer = true;
                                    break;
                                case WEEKLY:
                                    shouldTransfer = true;
                                    break;
                                case MONTHLY:
                                    shouldTransfer = true;
                                    break;
                                case QUARTERLY:
                                    shouldTransfer = month % 3 == 0;
                                    break;
                                case ANNUALLY:
                                    shouldTransfer = month % 12 == 0;
                                    break;
                            }

                            if (!shouldTransfer) {
                                continue;
                            }

                            // Calculate transfer amount based on frequency
                            BigDecimal transferAmount = space.getTransferAmount();
                            if (space.getTransferFrequency() == TransferFrequencyEnum.DAILY) {
                                transferAmount = transferAmount.multiply(new BigDecimal(30)); // Approximate days in month
                            } else if (space.getTransferFrequency() == TransferFrequencyEnum.WEEKLY) {
                                transferAmount = transferAmount.multiply(new BigDecimal(4)); // Approximate weeks in month
                            }

                            // Determine source space
                            Long sourceSpaceId = space.getSourceSpaceId();
                            AccountSpace sourceSpace = sourceSpaceId != null ?
                                    allSpacesMap.get(sourceSpaceId) : mainSpace;

                            if (sourceSpace == null) {
                                continue; // Skip if source space not found
                            }

                            // Get current projected balances
                            BigDecimal sourceBalance = projectedBalances.get(sourceSpace.getAccountSpaceId());
                            BigDecimal targetBalance = projectedBalances.get(space.getAccountSpaceId());

                            // Skip if insufficient funds
                            if (sourceBalance.compareTo(transferAmount) < 0) {
                                continue;
                            }

                            // Update projected balances
                            projectedBalances.put(sourceSpace.getAccountSpaceId(), sourceBalance.subtract(transferAmount));
                            projectedBalances.put(space.getAccountSpaceId(), targetBalance.add(transferAmount));
                        }
                    }

                    return Mono.just(projectedBalances);
                });
    }

    // ===== Analytics Methods =====

    @Override
    public Mono<Map<Long, BigDecimal>> calculateBalanceDistribution(Long accountId) {
        return repository.calculateTotalBalance(accountId)
                .flatMap(totalBalance -> {
                    if (totalBalance == null || totalBalance.compareTo(BigDecimal.ZERO) <= 0) {
                        return Mono.just(new HashMap<Long, BigDecimal>());
                    }

                    return repository.findByAccountId(accountId)
                            .collectList()
                            .map(spaces -> {
                                Map<Long, BigDecimal> distribution = new HashMap<>();

                                spaces.forEach(space -> {
                                    BigDecimal percentage = space.getBalance()
                                            .multiply(ONE_HUNDRED)
                                            .divide(totalBalance, MATH_CONTEXT);
                                    distribution.put(space.getAccountSpaceId(), percentage);
                                });

                                return distribution;
                            });
                });
    }

    @Override
    public Mono<Map<Long, BigDecimal>> calculateGrowthRates(
            Long accountId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        // For a real implementation, we would need historical balance data
        // This is a simplified version that assumes linear growth from creation date
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException("Invalid date range"));
        }

        return repository.findByAccountId(accountId)
                .collectList()
                .map(spaces -> {
                    Map<Long, BigDecimal> growthRates = new HashMap<>();

                    spaces.forEach(space -> {
                        // Skip spaces created after the start date
                        if (space.getDateCreated().isAfter(startDate)) {
                            growthRates.put(space.getAccountSpaceId(), BigDecimal.ZERO);
                            return;
                        }

                        // Calculate days in the period
                        long daysInPeriod = ChronoUnit.DAYS.between(startDate, endDate);
                        if (daysInPeriod <= 0) {
                            daysInPeriod = 1; // Avoid division by zero
                        }

                        // Calculate daily growth rate (simplified)
                        BigDecimal dailyRate = space.getBalance()
                                .divide(new BigDecimal(daysInPeriod), MATH_CONTEXT)
                                .multiply(ONE_HUNDRED)
                                .divide(space.getBalance().max(BigDecimal.ONE), MATH_CONTEXT);

                        growthRates.put(space.getAccountSpaceId(), dailyRate);
                    });

                    return growthRates;
                });
    }

    @Override
    public Flux<AccountSpaceDTO> getSpacesByType(Long accountId, AccountSpaceTypeEnum spaceType) {
        return repository.findByAccountIdAndSpaceType(accountId, spaceType)
                .map(mapper::toDTO);
    }
}
