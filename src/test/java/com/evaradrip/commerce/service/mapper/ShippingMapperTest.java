package com.evaradrip.commerce.service.mapper;

import static com.evaradrip.commerce.domain.ShippingAsserts.*;
import static com.evaradrip.commerce.domain.ShippingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingMapperTest {

    private ShippingMapper shippingMapper;

    @BeforeEach
    void setUp() {
        shippingMapper = new ShippingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShippingSample1();
        var actual = shippingMapper.toEntity(shippingMapper.toDto(expected));
        assertShippingAllPropertiesEquals(expected, actual);
    }
}
