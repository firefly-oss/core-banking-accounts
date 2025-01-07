package com.catalis.core.banking.accounts.core.services.status.v1;

import com.catalis.core.banking.accounts.core.mappers.models.status.v1.AccountStatusHistoryMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import com.catalis.core.banking.accounts.models.repositories.status.v1.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountStatusHistoryCreateService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    @Autowired
    private AccountStatusHistoryMapper mapper;

    /**
     * Creates a new account status history record.
     * This method maps the provided DTO to an entity, persists the entity in the repository,
     * and then maps the saved entity back to a DTO with additional generated ID information.
     *
     * @param request the {@link AccountStatusHistoryDTO} containing the details of the account status history to be created
     * @return a {@link Mono} emitting the created {@link AccountStatusHistoryDTO}; includes the generated unique ID
     */
    public Mono<AccountStatusHistoryDTO> createAccountStatusHistory(AccountStatusHistoryDTO request) {
        return Mono.just(request)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(savedEntity -> {
                    AccountStatusHistoryDTO response = mapper.toDTO(savedEntity);
                    response.setAccountStatusHistoryId(savedEntity.getAccountStatusHistoryId());
                    return response;
                });
    }

}
