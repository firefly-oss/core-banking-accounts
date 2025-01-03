package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.core.mappers.core.v1.AccountMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountCreateService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    /**
     * Creates a new account by mapping the provided AccountDTO to an entity, saving it in the repository,
     * and mapping the saved entity back to a DTO.
     *
     * @param account the AccountDTO object representing the account details to be created
     * @return a Mono emitting the created AccountDTO object containing the saved account information
     */
    public Mono<AccountDTO> createAccount(AccountDTO account) {
        return Mono.just(account)
                .map(mapper::toEntity)
                .flatMap(entity -> repository.save(entity).map(savedEntity -> {
                    account.setAccountId(savedEntity.getAccountId());
                    return savedEntity;
                }))
                .map(mapper::toDTO);
    }

}