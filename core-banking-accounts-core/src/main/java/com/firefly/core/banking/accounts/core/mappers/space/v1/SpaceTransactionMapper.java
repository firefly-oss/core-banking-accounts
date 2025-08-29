package com.firefly.core.banking.accounts.core.mappers.space.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.space.v1.SpaceTransactionDTO;
import com.firefly.core.banking.accounts.models.entities.space.v1.SpaceTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for converting between SpaceTransaction entity and SpaceTransactionDTO.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SpaceTransactionMapper {
    
    /**
     * Convert entity to DTO
     * @param entity the entity to convert
     * @return the DTO
     */
    SpaceTransactionDTO toDTO(SpaceTransaction entity);
    
    /**
     * Convert DTO to entity
     * @param dto the DTO to convert
     * @return the entity
     */
    SpaceTransaction toEntity(SpaceTransactionDTO dto);
    
    /**
     * Update entity from DTO
     * @param dto the source DTO
     * @param entity the target entity
     */
    void updateEntityFromDTO(SpaceTransactionDTO dto, @MappingTarget SpaceTransaction entity);
}
