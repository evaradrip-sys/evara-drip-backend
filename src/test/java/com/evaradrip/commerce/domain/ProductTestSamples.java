package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .sku("sku1")
            .reviewsCount(1)
            .stockCount(1)
            .metaTitle("metaTitle1")
            .metaDescription("metaDescription1")
            .metaKeywords("metaKeywords1")
            .dimensions("dimensions1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .sku("sku2")
            .reviewsCount(2)
            .stockCount(2)
            .metaTitle("metaTitle2")
            .metaDescription("metaDescription2")
            .metaKeywords("metaKeywords2")
            .dimensions("dimensions2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .reviewsCount(intCount.incrementAndGet())
            .stockCount(intCount.incrementAndGet())
            .metaTitle(UUID.randomUUID().toString())
            .metaDescription(UUID.randomUUID().toString())
            .metaKeywords(UUID.randomUUID().toString())
            .dimensions(UUID.randomUUID().toString());
    }
}
