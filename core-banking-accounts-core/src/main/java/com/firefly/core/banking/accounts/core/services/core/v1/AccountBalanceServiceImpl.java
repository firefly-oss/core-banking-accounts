package com.firefly.core.banking.accounts.core.services.core.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.accounts.core.mappers.core.v1.AccountBalanceMapper;
import com.firefly.core.banking.accounts.interfaces.dtos.core.v1.AccountBalanceDTO;
import com.firefly.core.banking.accounts.models.entities.core.v1.AccountBalance;
import com.firefly.core.banking.accounts.models.repositories.core.v1.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@Transactional
public class AccountBalanceServiceImpl implements AccountBalanceService {

    @Autowired
    private AccountBalanceRepository repository;

    @Autowired
    private AccountBalanceMapper mapper;

    @Override
    public Mono<PaginationResponse<AccountBalanceDTO>> getAllBalances(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<AccountBalanceDTO>> getGlobalBalances(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndAccountSpaceIdIsNull(accountId, pageable),
                () -> repository.countByAccountIdAndAccountSpaceIdIsNull(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<AccountBalanceDTO>> getSpaceBalances(UUID accountId, UUID accountSpaceId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndAccountSpaceId(accountId, accountSpaceId, pageable),
                () -> repository.countByAccountIdAndAccountSpaceId(accountId, accountSpaceId)
        );
    }

    @Override
    public Mono<AccountBalanceDTO> createBalance(UUID accountId, AccountBalanceDTO balanceDTO) {
        balanceDTO.setAccountId(accountId);
        AccountBalance accountBalance = mapper.toEntity(balanceDTO);
        return repository.save(accountBalance)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountBalanceDTO> getBalance(UUID accountId, UUID balanceId) {
        return repository.findById(balanceId)
                .filter(balance -> balance.getAccountId().equals(accountId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AccountBalanceDTO> updateBalance(UUID accountId, UUID balanceId, AccountBalanceDTO balanceDTO) {
        return repository.findById(balanceId)
                .filter(balance -> balance.getAccountId().equals(accountId))
                .flatMap(existingBalance -> {
                    balanceDTO.setAccountBalanceId(balanceId);
                    balanceDTO.setAccountId(accountId);
                    AccountBalance updatedBalance = mapper.toEntity(balanceDTO);
                    return repository.save(updatedBalance);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteBalance(UUID accountId, UUID balanceId) {
        return repository.findById(balanceId)
                .filter(balance -> balance.getAccountId().equals(accountId))
                .flatMap(repository::delete);
    }
}
