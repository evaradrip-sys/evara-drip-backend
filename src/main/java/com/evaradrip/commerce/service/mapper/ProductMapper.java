package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Brand;
import com.evaradrip.commerce.domain.Category;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.BrandDTO;
import com.evaradrip.commerce.service.dto.CategoryDTO;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.dto.PromotionDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "promotions", source = "promotions", qualifiedByName = "promotionIdSet")
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandName")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    @Mapping(target = "wishlisteds", source = "wishlisteds", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "applicablePromotions", source = "applicablePromotions", qualifiedByName = "promotionIdSet")
    @Mapping(target = "featuredInCategories", source = "featuredInCategories", qualifiedByName = "categoryIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "removePromotions", ignore = true)
    @Mapping(target = "wishlisteds", ignore = true)
    @Mapping(target = "removeWishlisted", ignore = true)
    @Mapping(target = "applicablePromotions", ignore = true)
    @Mapping(target = "removeApplicablePromotions", ignore = true)
    @Mapping(target = "featuredInCategories", ignore = true)
    @Mapping(target = "removeFeaturedInCategories", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("promotionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PromotionDTO toDtoPromotionId(Promotion promotion);

    @Named("promotionIdSet")
    default Set<PromotionDTO> toDtoPromotionIdSet(Set<Promotion> promotion) {
        return promotion.stream().map(this::toDtoPromotionId).collect(Collectors.toSet());
    }

    @Named("brandName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BrandDTO toDtoBrandName(Brand brand);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
    }
}
