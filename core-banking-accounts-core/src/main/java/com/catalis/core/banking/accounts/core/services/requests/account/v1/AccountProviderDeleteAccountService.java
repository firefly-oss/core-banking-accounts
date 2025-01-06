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
public class AccountProviderDeleteAccountService {

    @Autowired
    private AccountProvider accountProvider;

    @Autowired
    private AccountProviderGetService getService;

    public Mono<Void> deleteAccount(String externalAccountId) {
        return accountProvider.deleteAccount(externalAccountId)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to delete account", e)));
    }

    /**
     * Deletes an account by its ID and removes the corresponding active provider if one exists.
     * The method retrieves all account providers for the given account ID, identifies the active provider,
     * and deletes the account associated with the active provider's external reference.
     *
     * @param accountId the ID of the account to delete
     * @return a {@code Mono<Void>} signaling completion or an error
     */
    public Mono<Void> deleteAccount(Long accountId) {
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
                .flatMap(activeProvider -> Mono.defer(() -> deleteAccount(activeProvider.getExternalReference())))
                .then();
    }

    /**
     * Deletes an account by invoking the deletion process for the account's ID.
     *
     * @param account the {@link AccountDTO} object containing the account details, including ID, to be deleted
     * @return a {@code Mono<Void>} signaling completion of the account deletion process or an error if the operation fails
     */
    public Mono<Void> deleteAccount(AccountDTO account) {
        return deleteAccount(account.getAccountId());
    }

}
