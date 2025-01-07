package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountUpdateService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    /**
     * Updates an account in the repository based on the provided account ID and account data.
     *
     * @param accountId the ID of the account to be updated
     * @param account the data to update the account with
     * @return a {@code Mono} that emits the updated account as a {@code AccountDTO}
     *         or an error if the account does not exist or the update process fails
     */
    public Mono<AccountDTO> updateAccount(Long accountId, AccountDTO account) {
        return repository.findById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("Account with ID " + accountId + " not found")))
                .flatMap(existingAccount -> {
                    existingAccount.setContractId(account.getContractId());
                    existingAccount.setAccountNumber(account.getAccountNumber());
                    existingAccount.setAccountType(account.getAccountType());
                    existingAccount.setCurrency(account.getCurrency());
                    existingAccount.setOpenDate(account.getOpenDate());
                    existingAccount.setCloseDate(account.getCloseDate());
                    existingAccount.setAccountStatus(account.getAccountStatus());
                    existingAccount.setBranchId(account.getBranchId());
                    existingAccount.setDescription(account.getDescription());
                    return repository.save(existingAccount);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to update account with ID: " + accountId, e)));
    }

}
