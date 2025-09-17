package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Coupon;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.User;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.CouponDTO;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.dto.UserDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "wishlists", source = "wishlists", qualifiedByName = "productIdSet")
    @Mapping(target = "coupons", source = "coupons", qualifiedByName = "couponIdSet")
    UserProfileDTO toDto(UserProfile s);

    @Mapping(target = "removeWishlist", ignore = true)
    @Mapping(target = "removeCoupons", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }

    @Named("couponId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CouponDTO toDtoCouponId(Coupon coupon);

    @Named("couponIdSet")
    default Set<CouponDTO> toDtoCouponIdSet(Set<Coupon> coupon) {
        return coupon.stream().map(this::toDtoCouponId).collect(Collectors.toSet());
    }
}
