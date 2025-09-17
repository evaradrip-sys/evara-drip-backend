package com.evaradrip.commerce.service.mapper;

import static com.evaradrip.commerce.domain.ProductImageAsserts.*;
import static com.evaradrip.commerce.domain.ProductImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductImageMapperTest {

    private ProductImageMapper productImageMapper;

    @BeforeEach
    void setUp() {
        productImageMapper = new ProductImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductImageSample1();
        var actual = productImageMapper.toEntity(productImageMapper.toDto(expected));
        assertProductImageAllPropertiesEquals(expected, actual);
    }
}
