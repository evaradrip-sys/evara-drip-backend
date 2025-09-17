package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Review;
import com.evaradrip.commerce.domain.User;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.dto.ReviewDTO;
import com.evaradrip.commerce.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    ReviewDTO toDto(Review s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
