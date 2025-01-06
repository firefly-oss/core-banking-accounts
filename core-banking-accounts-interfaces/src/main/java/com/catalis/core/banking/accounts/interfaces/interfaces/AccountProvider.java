package com.catalis.core.banking.accounts.interfaces.interfaces;

import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountProvider {

    Mono<AccountDTO> getAccount(String externalReference);
    Mono<AccountDTO> createAccount(AccountDTO accountDTO);
    Mono<AccountDTO> updateAccount(String externalReference, AccountDTO accountDTO);
    Mono<Void> deleteAccount(String externalReference);

    Flux<AccountBalanceDTO> getAccountBalance(String externalReference);
    Flux<AccountStatusHistoryDTO> getAccountStatusHistory(String externalReference);

}
