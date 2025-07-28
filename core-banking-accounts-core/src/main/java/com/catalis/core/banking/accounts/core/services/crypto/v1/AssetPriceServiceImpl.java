package com.catalis.core.banking.accounts.core.services.crypto.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.crypto.v1.AssetPriceMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.crypto.v1.AssetPriceDTO;
import com.catalis.core.banking.accounts.models.entities.crypto.v1.AssetPrice;
import com.catalis.core.banking.accounts.models.repositories.crypto.v1.AssetPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of the AssetPriceService interface.
 * Provides business logic for managing asset prices.
 */
@Service
@Transactional
public class AssetPriceServiceImpl implements AssetPriceService {

    @Autowired
    private AssetPriceRepository repository;

    @Autowired
    private AssetPriceMapper mapper;

    @Override
    public Mono<PaginationResponse<AssetPriceDTO>> getAllAssetPrices(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllAssetPrices(pageable),
                repository::countAllAssetPrices
        );
    }

    @Override
    public Mono<AssetPriceDTO> getAssetPrice(Long assetPriceId) {
        return repository.findById(assetPriceId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetPriceDTO> createAssetPrice(AssetPriceDTO assetPriceDTO) {
        AssetPrice assetPrice = mapper.toEntity(assetPriceDTO);
        return repository.save(assetPrice)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetPriceDTO> updateAssetPrice(Long assetPriceId, AssetPriceDTO assetPriceDTO) {
        return repository.findById(assetPriceId)
                .flatMap(existingAssetPrice -> {
                    assetPriceDTO.setAssetPriceId(assetPriceId);
                    AssetPrice updatedAssetPrice = mapper.toEntity(assetPriceDTO);
                    return repository.save(updatedAssetPrice);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAssetPrice(Long assetPriceId) {
        return repository.findById(assetPriceId)
                .flatMap(repository::delete);
    }

    @Override
    public Mono<PaginationResponse<AssetPriceDTO>> getAssetPricesBySymbol(String assetSymbol, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAssetSymbol(assetSymbol, pageable),
                () -> repository.countByAssetSymbol(assetSymbol)
        );
    }

    @Override
    public Mono<PaginationResponse<AssetPriceDTO>> getAssetPricesBySymbolAndCurrency(
            String assetSymbol, String quoteCurrency, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAssetSymbolAndQuoteCurrency(assetSymbol, quoteCurrency, pageable),
                () -> repository.countByAssetSymbolAndQuoteCurrency(assetSymbol, quoteCurrency)
        );
    }

    @Override
    public Mono<AssetPriceDTO> getLatestAssetPrice(String assetSymbol, String quoteCurrency) {
        return repository.findLatestPrice(assetSymbol, quoteCurrency)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<AssetPriceDTO>> getHistoricalAssetPrices(
            String assetSymbol, LocalDateTime startDate, LocalDateTime endDate, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAssetSymbolAndAsOfDatetimeBetween(assetSymbol, startDate, endDate, pageable),
                () -> repository.countByAssetSymbolAndAsOfDatetimeBetween(assetSymbol, startDate, endDate)
        );
    }

    @Override
    public Mono<PaginationResponse<AssetPriceDTO>> getHistoricalAssetPricesByCurrency(
            String assetSymbol, String quoteCurrency, LocalDateTime startDate, LocalDateTime endDate, 
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAssetSymbolAndQuoteCurrencyAndAsOfDatetimeBetween(
                        assetSymbol, quoteCurrency, startDate, endDate, pageable),
                () -> repository.countByAssetSymbolAndQuoteCurrencyAndAsOfDatetimeBetween(
                        assetSymbol, quoteCurrency, startDate, endDate)
        );
    }

    @Override
    public Mono<Void> deleteAssetPricesBySymbol(String assetSymbol) {
        return repository.deleteByAssetSymbol(assetSymbol);
    }

    @Override
    public Mono<Void> deleteAssetPricesBySymbolAndCurrency(String assetSymbol, String quoteCurrency) {
        return repository.deleteByAssetSymbolAndQuoteCurrency(assetSymbol, quoteCurrency);
    }

    @Override
    public Mono<Void> deleteAssetPricesOlderThan(LocalDateTime date) {
        return repository.deleteByAsOfDatetimeBefore(date);
    }
}