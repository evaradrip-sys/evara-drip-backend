package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.service.dto.OrderDTO;
import com.evaradrip.commerce.service.dto.UserAddressDTO;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "shippingAddress", source = "shippingAddress", qualifiedByName = "userAddressId")
    @Mapping(target = "billingAddress", source = "billingAddress", qualifiedByName = "userAddressId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    OrderDTO toDto(Order s);

    @Named("userAddressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAddressDTO toDtoUserAddressId(UserAddress userAddress);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
