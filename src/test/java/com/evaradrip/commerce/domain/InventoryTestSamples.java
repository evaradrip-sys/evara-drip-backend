package com.evaradrip.commerce.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Inventory getInventorySample1() {
        return new Inventory().id(1L).quantity(1).reservedQuantity(1).warehouse("warehouse1").reorderLevel(1).reorderQuantity(1);
    }

    public static Inventory getInventorySample2() {
        return new Inventory().id(2L).quantity(2).reservedQuantity(2).warehouse("warehouse2").reorderLevel(2).reorderQuantity(2);
    }

    public static Inventory getInventoryRandomSampleGenerator() {
        return new Inventory()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .reservedQuantity(intCount.incrementAndGet())
            .warehouse(UUID.randomUUID().toString())
            .reorderLevel(intCount.incrementAndGet())
            .reorderQuantity(intCount.incrementAndGet());
    }
}
