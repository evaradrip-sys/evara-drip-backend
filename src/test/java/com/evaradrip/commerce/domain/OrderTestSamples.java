package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order()
            .id(1L)
            .orderNumber("orderNumber1")
            .shippingMethod("shippingMethod1")
            .trackingNumber("trackingNumber1")
            .cancelReason("cancelReason1")
            .returnReason("returnReason1");
    }

    public static Order getOrderSample2() {
        return new Order()
            .id(2L)
            .orderNumber("orderNumber2")
            .shippingMethod("shippingMethod2")
            .trackingNumber("trackingNumber2")
            .cancelReason("cancelReason2")
            .returnReason("returnReason2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(longCount.incrementAndGet())
            .orderNumber(UUID.randomUUID().toString())
            .shippingMethod(UUID.randomUUID().toString())
            .trackingNumber(UUID.randomUUID().toString())
            .cancelReason(UUID.randomUUID().toString())
            .returnReason(UUID.randomUUID().toString());
    }
}
