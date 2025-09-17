package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.dto.PromotionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promotion} and its DTO {@link PromotionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromotionMapper extends EntityMapper<PromotionDTO, Promotion> {
    @Mapping(target = "applicableProducts", source = "applicableProducts", qualifiedByName = "productIdSet")
    @Mapping(target = "products", source = "products", qualifiedByName = "productIdSet")
    PromotionDTO toDto(Promotion s);

    @Mapping(target = "removeApplicableProducts", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProducts", ignore = true)
    Promotion toEntity(PromotionDTO promotionDTO);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }
}
