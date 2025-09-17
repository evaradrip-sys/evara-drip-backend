package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Inventory;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.service.dto.InventoryDTO;
import com.evaradrip.commerce.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    InventoryDTO toDto(Inventory s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
