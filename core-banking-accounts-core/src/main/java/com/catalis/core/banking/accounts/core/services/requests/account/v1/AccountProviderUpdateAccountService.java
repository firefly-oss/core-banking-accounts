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
public class AccountProviderUpdateAccountService {

    @Autowired
    private AccountProvider accountProvider;

    @Autowired
    private AccountProviderGetService getService;

    /**
     * Updates an existing account identified by its external reference number.
     *
     * @param externalReferenceNumber the unique external reference number identifying the account
     * @param account the {@link AccountDTO} object containing the updated account details
     * @return a {@link Mono} emitting the updated {@link AccountDTO} object, or an error if the update process fails
     */
    public Mono<AccountDTO> updateAccount(String externalReferenceNumber, AccountDTO account) {
        return accountProvider.updateAccount(externalReferenceNumber, account)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to get account", e)));
    }

    /**
     * Updates the account details for a given account ID. The method retrieves all account providers
     * associated with the specified account ID, filters through them to find the active provider,
     * and updates the account details using the external reference obtained from the active provider.
     *
     * @param accountId the unique identifier of the account to be updated
     * @param account   the {@link AccountDTO} object containing updated account details
     * @return a {@link Mono} emitting the updated {@link AccountDTO} object, or an error if the update fails
     */
    public Mono<AccountDTO> updateAccount(Long accountId, AccountDTO account) {
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
                .flatMap(activeProvider -> updateAccount(activeProvider.getExternalReference(), account));
    }


    /**
     * Updates the account information for the given {@link AccountDTO}.
     *
     * @param account the {@link AccountDTO} object containing the updated account details
     * @return a {@link Mono} emitting the updated {@link AccountDTO}, or an error if the update process fails
     */
    public Mono<AccountDTO> updateAccount(AccountDTO account) {
        return updateAccount(account.getAccountId(), account);
    }

}
