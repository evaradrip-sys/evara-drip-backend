package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment()
            .id(1L)
            .transactionId("transactionId1")
            .currency("currency1")
            .referenceNumber("referenceNumber1")
            .failureReason("failureReason1");
    }

    public static Payment getPaymentSample2() {
        return new Payment()
            .id(2L)
            .transactionId("transactionId2")
            .currency("currency2")
            .referenceNumber("referenceNumber2")
            .failureReason("failureReason2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .transactionId(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .referenceNumber(UUID.randomUUID().toString())
            .failureReason(UUID.randomUUID().toString());
    }
}
