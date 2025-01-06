package com.catalis.core.banking.accounts.core.services.models.status.v1;

import com.catalis.core.banking.accounts.core.mappers.models.status.v1.AccountStatusHistoryMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.models.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.models.repositories.status.v1.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountStatusHistoryUpdateService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    @Autowired
    private AccountStatusHistoryMapper mapper;

    /**
     * Updates an existing AccountStatusHistory entity with the provided details.
     *
     * @param accountStatusHistoryId the ID of the AccountStatusHistory to be updated
     * @param request                the DTO containing the new details for the AccountStatusHistory
     * @return a Mono emitting the updated AccountStatusHistoryDTO if successful
     */
    public Mono<AccountStatusHistoryDTO> updateAccountStatusHistory(Long accountStatusHistoryId,
                                                                    AccountStatusHistoryDTO request) {
        return repository.findById(accountStatusHistoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("AccountStatusHistory not found for ID: " + accountStatusHistoryId)))
                .flatMap(existingEntity -> {
                    existingEntity.setAccountId(request.getAccountId());
                    existingEntity.setStatusCode(request.getStatusCode());
                    existingEntity.setStatusStartDatetime(request.getStatusStartDatetime());
                    existingEntity.setStatusEndDatetime(request.getStatusEndDatetime());
                    existingEntity.setReason(request.getReason());
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to update AccountStatusHistory for ID: "
                        + accountStatusHistoryId, e)));
    }

}
