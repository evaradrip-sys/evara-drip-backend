package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAddress getUserAddressSample1() {
        return new UserAddress()
            .id(1L)
            .fullName("fullName1")
            .phoneNumber("phoneNumber1")
            .streetAddress("streetAddress1")
            .streetAddress2("streetAddress21")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .country("country1")
            .landmark("landmark1");
    }

    public static UserAddress getUserAddressSample2() {
        return new UserAddress()
            .id(2L)
            .fullName("fullName2")
            .phoneNumber("phoneNumber2")
            .streetAddress("streetAddress2")
            .streetAddress2("streetAddress22")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .country("country2")
            .landmark("landmark2");
    }

    public static UserAddress getUserAddressRandomSampleGenerator() {
        return new UserAddress()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .streetAddress(UUID.randomUUID().toString())
            .streetAddress2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .landmark(UUID.randomUUID().toString());
    }
}
