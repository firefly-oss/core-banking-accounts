package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Service class for retrieving account data with reactive support and pagination.
 */
@Service
@Transactional(readOnly = true)
public class AccountGetService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    /**
     * Retrieves account information by its unique ID.
     *
     * @param accountId the unique identifier of the account to be retrieved
     * @return a Mono emitting the AccountDTO if the account is found or an error if retrieval fails
     */
    public Mono<AccountDTO> getAccountById(Long accountId) {
        return repository.findById(accountId)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch account for Account ID: "
                        + accountId, e)));
    }

    /**
     * Retrieves account information based on account number.
     *
     * @param accountNumber the account number to search for
     * @return a Mono emitting the AccountDTO if found
     */
    public Mono<AccountDTO> getAccountByNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch account for Account Number: "
                        + accountNumber, e)));
    }

    /**
     * Retrieves account information by contract ID.
     *
     * @param contractId the contract ID linked to the account
     * @return a Mono emitting the AccountDTO if found
     */
    public Mono<AccountDTO> getAccountByContractId(Long contractId) {
        return repository.findByContractId(contractId)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch account for Contract ID: "
                        + contractId, e)));
    }

    /**
     * Retrieves a paginated list of accounts associated with a specific branch.
     *
     * @param branchId          the branch ID for accounts to be fetched
     * @param paginationRequest the pagination request containing page size and number settings
     * @return a Mono emitting a PaginationResponse containing a list of AccountDTOs
     */
    public Mono<PaginationResponse<AccountDTO>> getAccountsByBranchId(Long branchId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByBranchId(branchId, pageable),
                () -> repository.countByBranchId(branchId)
        );
    }

    /**
     * Counts the total number of accounts associated with a specific branch.
     *
     * @param branchId the branch ID to count accounts for
     * @return a Mono emitting the total count of accounts
     */
    public Mono<Long> countAccountsByBranchId(Long branchId) {
        return repository.countByBranchId(branchId)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to count accounts for Branch ID: "
                        + branchId, e)));
    }
}