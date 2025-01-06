package com.catalis.core.banking.accounts.core.services.requests.status.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.core.banking.accounts.core.services.models.provider.v1.AccountProviderGetService;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountBalanceDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.interfaces.enums.models.provider.v1.ProviderStatusEnum;
import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountProviderGetStatusService {

    @Autowired
    private AccountProvider accountProvider;

    @Autowired
    private AccountProviderGetService getService;

    /**
     * Retrieves the account status history for the given external reference number.
     *
     * @param externalReferenceNumber the external reference number identifying the account
     * @return a Flux emitting AccountStatusHistoryDTO objects representing the account's status history
     */
    public Flux<AccountStatusHistoryDTO> getAccountStatusHistory(String externalReferenceNumber) {
        return accountProvider.getAccountStatusHistory(externalReferenceNumber)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to get account statuses", e)));
    }

    /**
     * Retrieves the status history of an account based on the provided account ID.
     *
     * This method filters through the account's providers to find the first active provider,
     * then fetches the account status history by using the external reference of the active provider.
     * If no active provider is found or an error occurs during the process, an error is emitted.
     *
     * @param accountId the unique identifier of the account for which status history is to be retrieved
     * @return a Flux stream emitting {@link AccountStatusHistoryDTO} objects representing the status history
     * of the account, or an error if the operation fails
     */
    public Flux<AccountStatusHistoryDTO> getAccountStatusHistory(Long accountId) {
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
                .flatMapMany(activeProvider -> getAccountStatusHistory(activeProvider.getExternalReference()))
                .onErrorResume(e -> Flux.error(new RuntimeException("Failed to get account statuses", e)));
    }


    /**
     * Retrieves the account status history for a given account.
     *
     * @param account the account for which the status history is to be fetched
     * @return a Flux that emits AccountStatusHistoryDTO objects representing the account's status history
     */
    public Flux<AccountStatusHistoryDTO> getAccountStatusHistory(AccountDTO account) {
        return getAccountStatusHistory(account.getAccountId());
    }

}
