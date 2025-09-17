package com.evaradrip.commerce.service.mapper;

import static com.evaradrip.commerce.domain.PromotionAsserts.*;
import static com.evaradrip.commerce.domain.PromotionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromotionMapperTest {

    private PromotionMapper promotionMapper;

    @BeforeEach
    void setUp() {
        promotionMapper = new PromotionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPromotionSample1();
        var actual = promotionMapper.toEntity(promotionMapper.toDto(expected));
        assertPromotionAllPropertiesEquals(expected, actual);
    }
}
