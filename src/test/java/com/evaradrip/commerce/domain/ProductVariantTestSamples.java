package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductVariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductVariant getProductVariantSample1() {
        return new ProductVariant().id(1L).color("color1").sku("sku1").stockCount(1).barcode("barcode1");
    }

    public static ProductVariant getProductVariantSample2() {
        return new ProductVariant().id(2L).color("color2").sku("sku2").stockCount(2).barcode("barcode2");
    }

    public static ProductVariant getProductVariantRandomSampleGenerator() {
        return new ProductVariant()
            .id(longCount.incrementAndGet())
            .color(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .stockCount(intCount.incrementAndGet())
            .barcode(UUID.randomUUID().toString());
    }
}
