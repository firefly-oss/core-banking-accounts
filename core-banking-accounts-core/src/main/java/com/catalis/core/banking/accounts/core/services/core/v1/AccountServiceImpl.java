package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.accounts.core.mappers.core.v1.AccountMapper;
import com.catalis.core.banking.accounts.core.services.space.v1.AccountSpaceService;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.catalis.core.banking.accounts.models.entities.core.v1.Account;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    @Autowired
    private AccountSpaceService accountSpaceService;

    @Override
    public Mono<PaginationResponse<AccountDTO>> filterAccounts(FilterRequest<AccountDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Account.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        Account account = mapper.toEntity(accountDTO);
        return repository.save(account)
                .flatMap(savedAccount -> {
                    // Create a default MAIN space for the account
                    AccountSpaceDTO mainSpace = new AccountSpaceDTO();
                    mainSpace.setAccountId(savedAccount.getAccountId());
                    mainSpace.setSpaceName("Main Account");
                    mainSpace.setSpaceType(AccountSpaceTypeEnum.MAIN);
                    mainSpace.setBalance(BigDecimal.ZERO); // Initial balance
                    mainSpace.setIsVisible(true);
                    mainSpace.setDescription("Primary account space");

                    return accountSpaceService.createAccountSpace(mainSpace)
                            .thenReturn(savedAccount);
                })
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
                    mapper.updateEntityFromDto(accountDTO, existingAccount);
                    existingAccount.setAccountId(accountId);
                    return repository.save(existingAccount);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAccount(Long accountId) {
        return repository.findById(accountId)
                .flatMap(repository::delete);
    }
}
