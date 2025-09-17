package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Wishlist;
import com.evaradrip.commerce.service.dto.WishlistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Wishlist} and its DTO {@link WishlistDTO}.
 */
@Mapper(componentModel = "spring")
public interface WishlistMapper extends EntityMapper<WishlistDTO, Wishlist> {}
