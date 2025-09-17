package com.evaradrip.commerce.domain.enumeration;

/**
 * The OrderStatus enumeration.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    PACKED,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    REFUNDED,
    RETURNED,
    FAILED,
}
