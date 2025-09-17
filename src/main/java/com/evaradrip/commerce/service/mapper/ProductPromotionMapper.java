package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.ProductPromotion;
import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductPromotion} and its DTO {@link ProductPromotionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductPromotionMapper extends EntityMapper<ProductPromotionDTO, ProductPromotion> {}
