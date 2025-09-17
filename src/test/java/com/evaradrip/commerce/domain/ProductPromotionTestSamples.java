package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductPromotionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductPromotion getProductPromotionSample1() {
        return new ProductPromotion().id(1L).priority(1);
    }

    public static ProductPromotion getProductPromotionSample2() {
        return new ProductPromotion().id(2L).priority(2);
    }

    public static ProductPromotion getProductPromotionRandomSampleGenerator() {
        return new ProductPromotion().id(longCount.incrementAndGet()).priority(intCount.incrementAndGet());
    }
}
