package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShippingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Shipping getShippingSample1() {
        return new Shipping().id(1L).carrier("carrier1").trackingNumber("trackingNumber1");
    }

    public static Shipping getShippingSample2() {
        return new Shipping().id(2L).carrier("carrier2").trackingNumber("trackingNumber2");
    }

    public static Shipping getShippingRandomSampleGenerator() {
        return new Shipping()
            .id(longCount.incrementAndGet())
            .carrier(UUID.randomUUID().toString())
            .trackingNumber(UUID.randomUUID().toString());
    }
}
