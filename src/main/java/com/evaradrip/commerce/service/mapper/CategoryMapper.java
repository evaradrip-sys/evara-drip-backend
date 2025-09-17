package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Category;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.service.dto.CategoryDTO;
import com.evaradrip.commerce.service.dto.ProductDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "featuredProducts", source = "featuredProducts", qualifiedByName = "productIdSet")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "categoryName")
    CategoryDTO toDto(Category s);

    @Mapping(target = "removeFeaturedProducts", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);
}
