package com.firefly.core.banking.accounts.core.mappers.crypto.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.crypto.v1.AssetPriceDTO;
import com.firefly.core.banking.accounts.models.entities.crypto.v1.AssetPrice;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between AssetPrice entities and AssetPriceDTO objects.
 */
@Component
public class AssetPriceMapper {

    /**
     * Converts an AssetPrice entity to an AssetPriceDTO.
     *
     * @param assetPrice the entity to convert
     * @return the converted DTO
     */
    public AssetPriceDTO toDTO(AssetPrice assetPrice) {
        if (assetPrice == null) {
            return null;
        }

        return AssetPriceDTO.builder()
                .assetPriceId(assetPrice.getAssetPriceId())
                .assetSymbol(assetPrice.getAssetSymbol())
                .quoteCurrency(assetPrice.getQuoteCurrency())
                .price(assetPrice.getPrice())
                .asOfDatetime(assetPrice.getAsOfDatetime())
                .priceSource(assetPrice.getPriceSource())
                .dateCreated(assetPrice.getDateCreated())
                .dateUpdated(assetPrice.getDateUpdated())
                .build();
    }

    /**
     * Converts an AssetPriceDTO to an AssetPrice entity.
     *
     * @param assetPriceDTO the DTO to convert
     * @return the converted entity
     */
    public AssetPrice toEntity(AssetPriceDTO assetPriceDTO) {
        if (assetPriceDTO == null) {
            return null;
        }

        AssetPrice assetPrice = new AssetPrice();
        assetPrice.setAssetPriceId(assetPriceDTO.getAssetPriceId());
        assetPrice.setAssetSymbol(assetPriceDTO.getAssetSymbol());
        assetPrice.setQuoteCurrency(assetPriceDTO.getQuoteCurrency());
        assetPrice.setPrice(assetPriceDTO.getPrice());
        assetPrice.setAsOfDatetime(assetPriceDTO.getAsOfDatetime());
        assetPrice.setPriceSource(assetPriceDTO.getPriceSource());
        
        return assetPrice;
    }
}