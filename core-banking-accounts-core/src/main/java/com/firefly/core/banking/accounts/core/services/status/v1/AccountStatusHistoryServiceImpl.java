/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
import java.util.UUID;

@Service
@Transactional
public class AccountStatusHistoryServiceImpl implements AccountStatusHistoryService {

    @Autowired
    private AccountStatusHistoryRepository repository;

    @Autowired
    private AccountStatusHistoryMapper mapper;

    @Override
    public Mono<PaginationResponse<AccountStatusHistoryDTO>> listStatusHistory(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<AccountStatusHistoryDTO> createStatusHistory(UUID accountId, AccountStatusHistoryDTO historyDTO) {
        historyDTO.setAccountId(accountId);
        AccountStatusHistory entity = mapper.toEntity(historyDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatusHistoryDTO> getStatusHistory(UUID accountId, UUID historyId) {
        return repository.findById(historyId)
                .filter(record -> record.getAccountId().equals(accountId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountStatusHistoryDTO> updateStatusHistory(UUID accountId, UUID historyId, AccountStatusHistoryDTO historyDTO) {
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
    public Mono<Void> deleteStatusHistory(UUID accountId, UUID historyId) {
        return repository.findById(historyId)
                .filter(record -> record.getAccountId().equals(accountId))
                .flatMap(repository::delete);
    }
}
