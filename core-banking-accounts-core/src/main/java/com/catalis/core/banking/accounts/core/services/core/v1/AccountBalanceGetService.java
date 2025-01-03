package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.core.v1.AccountBalanceMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AccountBalanceGetService {

    @Autowired
    private AccountBalanceRepository repository;

    @Autowired
    private AccountBalanceMapper mapper;

    /**
     * Counts the balance history records for a specific account within a given time range and balance type.
     *
     * @param accountId the ID of the account whose balance history is to be counted
     * @param balanceType the type of balance to filter the records (e.g., CURRENT, AVAILABLE, BLOCKED)
     * @param startDate the start date of the time range to filter the balance history
     * @param endDate the end date of the time range to filter the balance history
     * @return a Mono emitting the count of balance history records that match the criteria
     */
    public Mono<Long> countBalanceHistory(Long accountId, BalanceTypeEnum balanceType,
                                          LocalDateTime startDate, LocalDateTime endDate) {
        return repository.countBalanceHistory(accountId, balanceType, startDate, endDate)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to count balance history for Account ID: "
                        + accountId, e)));
    }

    /**
     * Retrieves a paginated list of account balances for a specified account.
     *
     * @param accountId the ID of the account whose balances are to be retrieved
     * @param paginationRequest the pagination request containing page size and offset information
     * @return a Mono emitting a PaginationResponse containing a list of AccountBalanceDTOs
     */
    public Mono<PaginationResponse<AccountBalanceDTO>> getAccountBalances(Long accountId,
                                                                          PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    /**
     * Retrieves the balance history for a specific account within a specified date range and balance type,
     * with support for paginated results.
     *
     * @param accountId the ID of the account whose balance history is to be retrieved
     * @param balanceType the type of balance to filter the results (e.g., CURRENT, AVAILABLE, BLOCKED)
     * @param startDate the starting date and time for the balance history query
     * @param endDate the ending date and time for the balance history query
     * @param paginationRequest the pagination request object containing page size and page number
     * @return a Mono emitting a paginated response containing a list of AccountBalanceDTOs matching the query criteria
     */
    public Mono<PaginationResponse<AccountBalanceDTO>> getBalanceHistory(Long accountId, BalanceTypeEnum balanceType,
                                                     LocalDateTime startDate, LocalDateTime endDate,
                                                                         PaginationRequest paginationRequest) {

        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findBalanceHistory(accountId, balanceType, startDate, endDate, pageable),
                () -> repository.countBalanceHistory(accountId, balanceType, startDate, endDate)
        );

    }

}