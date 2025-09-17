package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Category getCategorySample1() {
        return new Category().id(1L).name("name1").imageUrl("imageUrl1").href("href1").displayOrder(1);
    }

    public static Category getCategorySample2() {
        return new Category().id(2L).name("name2").imageUrl("imageUrl2").href("href2").displayOrder(2);
    }

    public static Category getCategoryRandomSampleGenerator() {
        return new Category()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .href(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet());
    }
}
