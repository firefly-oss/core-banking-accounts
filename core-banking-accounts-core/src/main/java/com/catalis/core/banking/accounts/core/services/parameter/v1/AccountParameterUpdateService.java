package com.catalis.core.banking.accounts.core.services.parameter.v1;

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
public class AccountParameterUpdateService {

    @Autowired
    private AccountParameterRepository repository;

    @Autowired
    private AccountParameterMapper mapper;

    /**
     * Updates an existing account parameter with the specified details.
     *
     * @param accountParameterId the unique identifier of the account parameter to update
     * @param accountParameter the updated details of the account parameter
     * @return a Mono emitting the updated AccountParameterDTO upon success
     */
    public Mono<AccountParameterDTO> updateAccountParameter(Long accountParameterId, AccountParameterDTO accountParameter) {
        return repository.findById(accountParameterId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Account parameter not found with ID: " + accountParameterId)))
                .flatMap(existingParameter -> {
                    AccountParameter updatedEntity = mapper.toEntity(accountParameter);
                    updatedEntity.setAccountId(accountParameter.getAccountId());
                    updatedEntity.setParamType(accountParameter.getParamType());
                    updatedEntity.setParamValue(accountParameter.getParamValue());
                    updatedEntity.setParamUnit(accountParameter.getParamUnit());
                    updatedEntity.setEffectiveDate(accountParameter.getEffectiveDate());
                    updatedEntity.setExpiryDate(accountParameter.getExpiryDate());
                    updatedEntity.setDescription(accountParameter.getDescription());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to update account parameter with ID: " + accountParameterId, e)));
    }

}
