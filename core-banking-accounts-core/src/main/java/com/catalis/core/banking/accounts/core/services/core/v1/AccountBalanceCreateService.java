package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountBalanceMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountBalanceCreateService {

    @Autowired
    private AccountBalanceRepository repository;
    
    @Autowired
    private AccountBalanceMapper mapper;

    /**
     * Creates a new account balance by mapping the provided DTO to an entity, saving it to the repository,
     * and mapping the saved entity back to a DTO.
     *
     * @param accountBalance the DTO representing the account balance to be created
     * @return a Mono emitting the created account balance DTO
     */
    public Mono<AccountBalanceDTO> createAccountBalance(AccountBalanceDTO accountBalance) {
        return Mono.just(accountBalance)
                .map(mapper::toEntity)
                .flatMap(entity -> repository.save(entity).map(savedEntity -> {
                    accountBalance.setAccountBalanceId(savedEntity.getAccountBalanceId());
                    return savedEntity;
                }))
                .map(mapper::toDTO);
    }

}
