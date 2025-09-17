package com.evaradrip.commerce.service.mapper;

import static com.evaradrip.commerce.domain.UserAddressAsserts.*;
import static com.evaradrip.commerce.domain.UserAddressTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAddressMapperTest {

    private UserAddressMapper userAddressMapper;

    @BeforeEach
    void setUp() {
        userAddressMapper = new UserAddressMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAddressSample1();
        var actual = userAddressMapper.toEntity(userAddressMapper.toDto(expected));
        assertUserAddressAllPropertiesEquals(expected, actual);
    }
}
