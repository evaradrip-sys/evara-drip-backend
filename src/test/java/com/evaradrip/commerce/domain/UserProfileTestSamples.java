package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile().id(1L).phoneNumber("phoneNumber1").avatarUrl("avatarUrl1").loyaltyPoints(1);
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile().id(2L).phoneNumber("phoneNumber2").avatarUrl("avatarUrl2").loyaltyPoints(2);
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .avatarUrl(UUID.randomUUID().toString())
            .loyaltyPoints(intCount.incrementAndGet());
    }
}
