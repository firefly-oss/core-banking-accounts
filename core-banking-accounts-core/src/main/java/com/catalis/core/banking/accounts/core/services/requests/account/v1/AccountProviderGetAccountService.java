package com.catalis.core.banking.accounts.core.services.requests.account.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.core.banking.accounts.core.services.models.provider.v1.AccountProviderGetService;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.enums.models.provider.v1.ProviderStatusEnum;
import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountProviderGetAccountService {

    @Autowired
    private AccountProvider accountProvider;

    @Autowired
    private AccountProviderGetService getService;

    public Mono<AccountDTO> getAccount(String externalReferenceNumber) {
        return accountProvider.getAccount(externalReferenceNumber)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to get account", e)));
    }

    /**
     * Retrieves an account associated with the given account ID. The method fetches all account providers
     * for the specified account ID, filters to find the active provider, and retrieves the account information
     * based on the active provider's external reference*/
    public Mono<AccountDTO> getAccount(Long accountId) {
        return getService.getAccountProviders(
                        accountId,
                        PaginationRequest.builder()
                                .pageSize(100)
                                .pageNumber(0)
                                .build()
                )
                .flatMapMany(response -> Flux.fromIterable(response.getContent()))
                .filter(provider -> provider.getStatus() == ProviderStatusEnum.ACTIVE)
                .next()
                .flatMap(activeProvider -> getAccount(activeProvider.getExternalReference()));
    }

    /**
     * Retrieves an account associated with the given account's account ID.
     *
     * @param account the {@link AccountDTO} object containing details of the account
     * @return a {@link Mono} emitting the retrieved {@link AccountDTO} object, or an error if retrieval fails
     */
    public Mono<AccountDTO> getAccount(AccountDTO account) {
        return getAccount(account.getAccountId());
    }

}
