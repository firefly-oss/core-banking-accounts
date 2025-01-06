package com.catalis.core.banking.accounts.core.services.models.parameter.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.parameter.v1.AccountParameterMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.models.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.interfaces.enums.models.parameter.v1.ParamTypeEnum;
import com.catalis.core.banking.accounts.models.repositories.parameter.v1.AccountParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Service class for retrieving account parameter data with reactive support and pagination.
 */
@Service
@Transactional(readOnly = true)
public class AccountParameterGetService {

    @Autowired
    private AccountParameterRepository repository;

    @Autowired
    private AccountParameterMapper mapper;

    /**
     * Retrieves a specific account parameter by account ID with pagination.
     *
     * @param accountId         the account ID to search for parameters
     * @param paginationRequest the pagination request containing page size and page number
     * @return a Mono emitting a PaginationResponse with AccountParameterDTOs
     */
    public Mono<PaginationResponse<AccountParameterDTO>> getAccountParameters(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    /**
     * Counts the total number of account parameters for a specific account.
     *
     * @param accountId the account ID to count parameters for
     * @return a Mono emitting the total count of parameters
     */
    public Mono<Long> countAccountParameters(Long accountId) {
        return repository.countByAccountId(accountId)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to count parameters for Account ID: "
                        + accountId, e)));
    }

    /**
     * Retrieves the current valid parameter for a specific account and parameter type.
     *
     * @param accountId  the account ID to search for
     * @param paramType  the parameter type to filter
     * @return a Mono emitting an AccountParameterDTO if found
     */
    public Mono<AccountParameterDTO> getCurrentParameter(Long accountId, ParamTypeEnum paramType) {
        return repository.findCurrentParameter(accountId, paramType)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch current parameter for Account ID: "
                        + accountId + " and ParamType: " + paramType, e)));
    }
}