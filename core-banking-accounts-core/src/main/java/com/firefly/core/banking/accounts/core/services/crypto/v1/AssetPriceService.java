package com.firefly.core.banking.accounts.core.services.crypto.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.accounts.interfaces.dtos.crypto.v1.AssetPriceDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service interface for managing asset prices.
 * Provides methods for CRUD operations and business logic related to asset prices.
 */
public interface AssetPriceService {

    /**
     * Get all asset prices with pagination.
     *
     * @param paginationRequest pagination parameters
     * @return a paginated response of asset price DTOs
     */
    Mono<PaginationResponse<AssetPriceDTO>> getAllAssetPrices(PaginationRequest paginationRequest);

    /**
     * Get an asset price by ID.
     *
     * @param assetPriceId the ID of the asset price
     * @return a Mono of AssetPriceDTO
     */
    Mono<AssetPriceDTO> getAssetPrice(Long assetPriceId);

    /**
     * Create a new asset price.
     *
     * @param assetPriceDTO the asset price DTO to create
     * @return a Mono of the created AssetPriceDTO
     */
    Mono<AssetPriceDTO> createAssetPrice(AssetPriceDTO assetPriceDTO);

    /**
     * Update an existing asset price.
     *
     * @param assetPriceId the ID of the asset price to update
     * @param assetPriceDTO the updated asset price DTO
     * @return a Mono of the updated AssetPriceDTO
     */
    Mono<AssetPriceDTO> updateAssetPrice(Long assetPriceId, AssetPriceDTO assetPriceDTO);

    /**
     * Delete an asset price by ID.
     *
     * @param assetPriceId the ID of the asset price to delete
     * @return a Mono of Void
     */
    Mono<Void> deleteAssetPrice(Long assetPriceId);

    /**
     * Get all asset prices for a specific asset symbol.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param paginationRequest pagination parameters
     * @return a paginated response of asset price DTOs
     */
    Mono<PaginationResponse<AssetPriceDTO>> getAssetPricesBySymbol(String assetSymbol, PaginationRequest paginationRequest);

    /**
     * Get all asset prices for a specific asset symbol and quote currency.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param quoteCurrency the currency in which the asset is priced (e.g., "USD", "EUR")
     * @param paginationRequest pagination parameters
     * @return a paginated response of asset price DTOs
     */
    Mono<PaginationResponse<AssetPriceDTO>> getAssetPricesBySymbolAndCurrency(
            String assetSymbol, String quoteCurrency, PaginationRequest paginationRequest);

    /**
     * Get the latest asset price for a specific asset symbol and quote currency.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param quoteCurrency the currency in which the asset is priced (e.g., "USD", "EUR")
     * @return a Mono of AssetPriceDTO
     */
    Mono<AssetPriceDTO> getLatestAssetPrice(String assetSymbol, String quoteCurrency);

    /**
     * Get historical asset prices for a specific asset symbol within a date range.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param paginationRequest pagination parameters
     * @return a paginated response of asset price DTOs
     */
    Mono<PaginationResponse<AssetPriceDTO>> getHistoricalAssetPrices(
            String assetSymbol, LocalDateTime startDate, LocalDateTime endDate, PaginationRequest paginationRequest);

    /**
     * Get historical asset prices for a specific asset symbol and quote currency within a date range.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param quoteCurrency the currency in which the asset is priced (e.g., "USD", "EUR")
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param paginationRequest pagination parameters
     * @return a paginated response of asset price DTOs
     */
    Mono<PaginationResponse<AssetPriceDTO>> getHistoricalAssetPricesByCurrency(
            String assetSymbol, String quoteCurrency, LocalDateTime startDate, LocalDateTime endDate, 
            PaginationRequest paginationRequest);

    /**
     * Delete all asset prices for a specific asset symbol.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @return a Mono of Void
     */
    Mono<Void> deleteAssetPricesBySymbol(String assetSymbol);

    /**
     * Delete all asset prices for a specific asset symbol and quote currency.
     *
     * @param assetSymbol the symbol of the asset (e.g., "BTC", "ETH")
     * @param quoteCurrency the currency in which the asset is priced (e.g., "USD", "EUR")
     * @return a Mono of Void
     */
    Mono<Void> deleteAssetPricesBySymbolAndCurrency(String assetSymbol, String quoteCurrency);

    /**
     * Delete all asset prices older than a specific date.
     *
     * @param date the cutoff date
     * @return a Mono of Void
     */
    Mono<Void> deleteAssetPricesOlderThan(LocalDateTime date);
}