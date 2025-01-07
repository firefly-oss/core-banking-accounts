package com.catalis.core.banking.accounts.core.services.core.v1;

import com.catalis.core.banking.accounts.core.mappers.models.core.v1.AccountBalanceMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.catalis.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountBalanceUpdateService {

    @Autowired
    private AccountBalanceRepository repository;

    @Autowired
    private AccountBalanceMapper mapper;

    /**
     * Updates the account balance details for a specified account balance ID.
     * If the account balance ID is not found, an error is returned.
     *
     * @param accountBalanceId The unique identifier of the account balance to update.
     * @param accountBalance The updated account balance details encapsulated in an AccountBalanceDTO object.
     *                       Includes updated values such as account ID, balance type, balance amount,
     *                       and timestamp.
     * @return A Mono of AccountBalanceDTO containing the updated account balance details after the
     *         changes have been persisted.
     */
    public Mono<AccountBalanceDTO> updateAccountBalance(Long accountBalanceId, AccountBalanceDTO accountBalance) {
        return repository.findById(accountBalanceId)
                .switchIfEmpty(Mono.error(new RuntimeException("AccountBalance not found for ID: " + accountBalanceId)))
                .flatMap(existingAccountBalance -> {
                    AccountBalance entityToUpdate = mapper.toEntity(accountBalance);
                    entityToUpdate.setAccountId(accountBalance.getAccountId());
                    entityToUpdate.setBalanceType(accountBalance.getBalanceType());
                    entityToUpdate.setBalanceAmount(accountBalance.getBalanceAmount());
                    entityToUpdate.setAsOfDatetime(accountBalance.getAsOfDatetime());
                    return repository.save(entityToUpdate);
                })
                .map(mapper::toDTO);
    }
    
}
