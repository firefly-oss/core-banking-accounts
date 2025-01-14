package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.models.entities.core.v1.Account;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = mapper.toEntity(accountDTO);
        return repository.save(account)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountDTO> getAccount(Long accountId) {
        return repository.findById(accountId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountDTO> updateAccount(Long accountId, AccountDTO accountDTO) {
        return repository.findById(accountId)
                .flatMap(existingAccount -> {
                    Account updatedAccount = mapper.toEntity(accountDTO);
                    updatedAccount.setAccountId(accountId);
                    return repository.save(updatedAccount);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAccount(Long accountId) {
        return repository.findById(accountId)
                .flatMap(repository::delete);
    }
}