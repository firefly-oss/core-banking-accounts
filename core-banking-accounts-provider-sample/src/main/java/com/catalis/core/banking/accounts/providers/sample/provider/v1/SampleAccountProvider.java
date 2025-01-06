package com.catalis.core.banking.accounts.providers.sample.provider.v1;

import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component

public class SampleAccountProvider implements AccountProvider {
    @Override
    public Mono<AccountDTO> getAccount(String externalReference) {
        return null;
    }

    @Override
    public Mono<AccountDTO> createAccount(AccountDTO accountDTO) {
        return null;
    }

    @Override
    public Mono<AccountDTO> updateAccount(String externalReference, AccountDTO accountDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteAccount(String externalReference) {
        return null;
    }

    @Override
    public Flux<AccountBalanceDTO> getAccountBalance(String externalReference) {
        return null;
    }

    @Override
    public Flux<AccountStatusHistoryDTO> getAccountStatusHistory(String externalReference) {
        return null;
    }
}
