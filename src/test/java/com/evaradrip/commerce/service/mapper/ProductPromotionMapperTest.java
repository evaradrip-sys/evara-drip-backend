package com.evaradrip.commerce.service.mapper;

import static com.evaradrip.commerce.domain.ProductPromotionAsserts.*;
import static com.evaradrip.commerce.domain.ProductPromotionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductPromotionMapperTest {

    private ProductPromotionMapper productPromotionMapper;

    @BeforeEach
    void setUp() {
        productPromotionMapper = new ProductPromotionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductPromotionSample1();
        var actual = productPromotionMapper.toEntity(productPromotionMapper.toDto(expected));
        assertProductPromotionAllPropertiesEquals(expected, actual);
    }
}
