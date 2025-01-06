package com.catalis.core.banking.accounts.core.services.requests.balance.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.core.banking.accounts.core.services.models.provider.v1.AccountProviderGetService;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.enums.models.provider.v1.ProviderStatusEnum;
import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountProviderGetBalanceService {

    @Autowired
    private AccountProvider accountProvider;

    @Autowired
    private AccountProviderGetService getService;

    /**
     * Fetches the account balance for a given external reference number.
     *
     * @param externalReferenceNumber the external reference number of the account
     * @return a Flux emitting instances of AccountBalanceDTO containing the account balance details
     */
    public Flux<AccountBalanceDTO> getAccountBalance(String externalReferenceNumber) {
        return accountProvider.getAccountBalance(externalReferenceNumber)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to get account balances", e)));
    }

    /**
     * Retrieves the balance information for the provided account ID.
     *
     * This method fetches and processes active account providers for the given account ID,
     * retrieves their balance data, and returns the account balance details.
     *
     * @param accountId the unique identifier of the account for which the balance is to be retrieved
     * @return a Flux containing {@link AccountBalanceDTO} objects representing the account balance details
     */
    public Flux<AccountBalanceDTO> getAccountBalance(Long accountId) {
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
                .flatMapMany(activeProvider -> getAccountBalance(activeProvider.getExternalReference()))
                .onErrorResume(e -> Flux.error(new RuntimeException("Failed to get account balances", e)));
    }

    /**
     * Retrieves the account balance information for the given account.
     *
     * @param account the AccountDTO object containing the account details
     * @return a Flux emitting AccountBalanceDTO objects representing the balance details of the account
     */
    public Flux<AccountBalanceDTO> getAccountBalance(AccountDTO account) {
        return getAccountBalance(account.getAccountId());
    }

}