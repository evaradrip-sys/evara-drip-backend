package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.UserAddressDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAddress} and its DTO {@link UserAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAddressMapper extends EntityMapper<UserAddressDTO, UserAddress> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    UserAddressDTO toDto(UserAddress s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
