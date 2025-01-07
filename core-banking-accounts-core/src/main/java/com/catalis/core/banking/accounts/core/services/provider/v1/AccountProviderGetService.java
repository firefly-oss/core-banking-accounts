package com.catalis.core.banking.accounts.core.services.provider.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.provider.v1.AccountProviderMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class AccountProviderGetService {

    @Autowired
    private AccountProviderRepository repository;

    @Autowired
    private AccountProviderMapper mapper;

    /**
     * Retrieves a paginated list of account providers for a specific account ID.
     *
     * @param accountId         the account ID to fetch providers for
     * @param paginationRequest the pagination request containing page size and page number
     * @return a Mono emitting a PaginationResponse with AccountProviderDTOs
     */
    public Mono<PaginationResponse<AccountProviderDTO>> getAccountProviders(Long accountId,
                                                                            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    /**
     * Retrieves a paginated list of active providers based on provider name.
     *
     * @param providerName      the name of the provider to filter by
     * @param paginationRequest the pagination request containing page size and page number
     * @return a Mono emitting a PaginationResponse of active AccountProviderDTOs
     */
    public Mono<PaginationResponse<AccountProviderDTO>> getActiveProvidersByName(String providerName,
                                                                                 PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findActiveByProvider(providerName, pageable),
                () -> repository.countActiveByProvider(providerName)
        );
    }

}
