package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Coupon;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.CouponDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coupon} and its DTO {@link CouponDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouponMapper extends EntityMapper<CouponDTO, Coupon> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userProfileIdSet")
    CouponDTO toDto(Coupon s);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    Coupon toEntity(CouponDTO couponDTO);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
    }
}
