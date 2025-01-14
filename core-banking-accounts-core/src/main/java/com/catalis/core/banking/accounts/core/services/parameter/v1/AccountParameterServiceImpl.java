package com.catalis.core.banking.accounts.core.services.parameter.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.parameter.v1.AccountParameterMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.models.entities.parameter.v1.AccountParameter;
import com.catalis.core.banking.accounts.models.repositories.parameter.v1.AccountParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountParameterServiceImpl implements AccountParameterService {

    @Autowired
    private AccountParameterRepository repository;

    @Autowired
    private AccountParameterMapper mapper;

    @Override
    public Mono<PaginationResponse<AccountParameterDTO>> listParameters(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<AccountParameterDTO> createParameter(Long accountId, AccountParameterDTO parameterDTO) {
        parameterDTO.setAccountId(accountId);
        AccountParameter accountParameter = mapper.toEntity(parameterDTO);
        return repository.save(accountParameter)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountParameterDTO> getParameter(Long accountId, Long paramId) {
        return repository.findById(paramId)
                .filter(param -> param.getAccountId().equals(accountId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountParameterDTO> updateParameter(Long accountId, Long paramId, AccountParameterDTO parameterDTO) {
        return repository.findById(paramId)
                .filter(param -> param.getAccountId().equals(accountId))
                .flatMap(existingParam -> {
                    parameterDTO.setAccountId(accountId);
                    parameterDTO.setAccountParameterId(paramId);
                    AccountParameter updatedParam = mapper.toEntity(parameterDTO);
                    return repository.save(updatedParam);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteParameter(Long accountId, Long paramId) {
        return repository.findById(paramId)
                .filter(param -> param.getAccountId().equals(accountId))
                .flatMap(repository::delete);
    }
}
