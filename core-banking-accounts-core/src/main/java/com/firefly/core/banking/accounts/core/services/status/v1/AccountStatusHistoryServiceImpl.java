package com.firefly.core.banking.accounts.core.services.status.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.accounts.core.mappers.status.v1.AccountStatusHistoryMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.status.v1.AccountStatusHistoryDTO;
import com.firefly.core.banking.accounts.models.entities.status.v1.AccountStatusHistory;
import com.firefly.core.banking.accounts.models.repositories.status.v1.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountStatusHistoryServiceImpl implements AccountStatusHistoryService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    @Autowired
    private AccountStatusHistoryMapper mapper;

    @Override
    public Mono<PaginationResponse<AccountStatusHistoryDTO>> listStatusHistory(Long accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<AccountStatusHistoryDTO> createStatusHistory(Long accountId, AccountStatusHistoryDTO historyDTO) {
        historyDTO.setAccountId(accountId);
        AccountStatusHistory entity = mapper.toEntity(historyDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatusHistoryDTO> getStatusHistory(Long accountId, Long historyId) {
        return repository.findById(historyId)
                .filter(record -> record.getAccountId().equals(accountId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatusHistoryDTO> updateStatusHistory(Long accountId, Long historyId, AccountStatusHistoryDTO historyDTO) {
        return repository.findById(historyId)
                .filter(record -> record.getAccountId().equals(accountId))
                .flatMap(existingRecord -> {
                    historyDTO.setAccountId(accountId);
                    historyDTO.setAccountStatusHistoryId(historyId);
                    AccountStatusHistory updatedEntity = mapper.toEntity(historyDTO);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteStatusHistory(Long accountId, Long historyId) {
        return repository.findById(historyId)
                .filter(record -> record.getAccountId().equals(accountId))
                .flatMap(repository::delete);
    }
}
