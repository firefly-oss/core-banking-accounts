package com.catalis.core.banking.accounts.core.services.parameter.v1;

import com.catalis.core.banking.accounts.core.mappers.models.parameter.v1.AccountParameterMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.parameter.v1.AccountParameterDTO;
import com.catalis.core.banking.accounts.models.repositories.parameter.v1.AccountParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountParameterCreateService {

    @Autowired
    private AccountParameterRepository repository;

    @Autowired
    private AccountParameterMapper mapper;

    /**
     * Creates a new account parameter by converting the provided DTO to an entity,
     * saving the entity to the repository, and then converting the saved entity back to a DTO.
     *
     * @param accountParameter the {@code AccountParameterDTO} containing the details of
     *        the account parameter to be created
     * @return a {@code Mono<AccountParameterDTO>} containing the created account parameter
     *         details, including the generated ID
     */
    public Mono<AccountParameterDTO> createAccountParameter(AccountParameterDTO accountParameter) {
        return Mono.just(accountParameter)
                .map(mapper::toEntity)
                .flatMap(entity -> repository.save(entity).map(savedEntity -> {
                    accountParameter.setAccountParameterId(savedEntity.getAccountParameterId());
                    return savedEntity;
                }))
                .map(mapper::toDTO);
    }

}
