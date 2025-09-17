package com.evaradrip.commerce.service.mapper;

import com.evaradrip.commerce.domain.Brand;
import com.evaradrip.commerce.service.dto.BrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {}
