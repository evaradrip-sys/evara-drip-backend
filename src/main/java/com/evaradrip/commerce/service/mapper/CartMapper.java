package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Cart;
import com.evaradrip.commerce.domain.User;
import com.evaradrip.commerce.service.dto.CartDTO;
import com.evaradrip.commerce.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CartDTO toDto(Cart s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
