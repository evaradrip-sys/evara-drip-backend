package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WishlistTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Wishlist getWishlistSample1() {
        return new Wishlist().id(1L).priority(1).notes("notes1");
    }

    public static Wishlist getWishlistSample2() {
        return new Wishlist().id(2L).priority(2).notes("notes2");
    }

    public static Wishlist getWishlistRandomSampleGenerator() {
        return new Wishlist().id(longCount.incrementAndGet()).priority(intCount.incrementAndGet()).notes(UUID.randomUUID().toString());
    }
}
