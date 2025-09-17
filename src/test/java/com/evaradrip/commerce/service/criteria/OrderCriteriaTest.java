package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderCriteriaTest {

    @Test
    void newOrderCriteriaHasAllFiltersNullTest() {
        var orderCriteria = new OrderCriteria();
        assertThat(orderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderCriteriaFluentMethodsCreatesFiltersTest() {
        var orderCriteria = new OrderCriteria();

        setAllFilters(orderCriteria);

        assertThat(orderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderCriteriaCopyCreatesNullFilterTest() {
        var orderCriteria = new OrderCriteria();
        var copy = orderCriteria.copy();

        assertThat(orderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderCriteria)
        );
    }

    @Test
    void orderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderCriteria = new OrderCriteria();
        setAllFilters(orderCriteria);

        var copy = orderCriteria.copy();

        assertThat(orderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderCriteria = new OrderCriteria();

        assertThat(orderCriteria).hasToString("OrderCriteria{}");
    }

    private static void setAllFilters(OrderCriteria orderCriteria) {
        orderCriteria.id();
        orderCriteria.orderNumber();
        orderCriteria.status();
        orderCriteria.totalAmount();
        orderCriteria.subtotalAmount();
        orderCriteria.taxAmount();
        orderCriteria.shippingAmount();
        orderCriteria.discountAmount();
        orderCriteria.paymentMethod();
        orderCriteria.paymentStatus();
        orderCriteria.shippingMethod();
        orderCriteria.trackingNumber();
        orderCriteria.cancelReason();
        orderCriteria.returnReason();
        orderCriteria.refundAmount();
        orderCriteria.estimatedDeliveryDate();
        orderCriteria.deliveredDate();
        orderCriteria.shippedDate();
        orderCriteria.itemsId();
        orderCriteria.paymentsId();
        orderCriteria.shippingId();
        orderCriteria.shippingAddressId();
        orderCriteria.billingAddressId();
        orderCriteria.userId();
        orderCriteria.distinct();
    }

    private static Condition<OrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderNumber()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getSubtotalAmount()) &&
                condition.apply(criteria.getTaxAmount()) &&
                condition.apply(criteria.getShippingAmount()) &&
                condition.apply(criteria.getDiscountAmount()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getPaymentStatus()) &&
                condition.apply(criteria.getShippingMethod()) &&
                condition.apply(criteria.getTrackingNumber()) &&
                condition.apply(criteria.getCancelReason()) &&
                condition.apply(criteria.getReturnReason()) &&
                condition.apply(criteria.getRefundAmount()) &&
                condition.apply(criteria.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getDeliveredDate()) &&
                condition.apply(criteria.getShippedDate()) &&
                condition.apply(criteria.getItemsId()) &&
                condition.apply(criteria.getPaymentsId()) &&
                condition.apply(criteria.getShippingId()) &&
                condition.apply(criteria.getShippingAddressId()) &&
                condition.apply(criteria.getBillingAddressId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderCriteria> copyFiltersAre(OrderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderNumber(), copy.getOrderNumber()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getSubtotalAmount(), copy.getSubtotalAmount()) &&
                condition.apply(criteria.getTaxAmount(), copy.getTaxAmount()) &&
                condition.apply(criteria.getShippingAmount(), copy.getShippingAmount()) &&
                condition.apply(criteria.getDiscountAmount(), copy.getDiscountAmount()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getPaymentStatus(), copy.getPaymentStatus()) &&
                condition.apply(criteria.getShippingMethod(), copy.getShippingMethod()) &&
                condition.apply(criteria.getTrackingNumber(), copy.getTrackingNumber()) &&
                condition.apply(criteria.getCancelReason(), copy.getCancelReason()) &&
                condition.apply(criteria.getReturnReason(), copy.getReturnReason()) &&
                condition.apply(criteria.getRefundAmount(), copy.getRefundAmount()) &&
                condition.apply(criteria.getEstimatedDeliveryDate(), copy.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getDeliveredDate(), copy.getDeliveredDate()) &&
                condition.apply(criteria.getShippedDate(), copy.getShippedDate()) &&
                condition.apply(criteria.getItemsId(), copy.getItemsId()) &&
                condition.apply(criteria.getPaymentsId(), copy.getPaymentsId()) &&
                condition.apply(criteria.getShippingId(), copy.getShippingId()) &&
                condition.apply(criteria.getShippingAddressId(), copy.getShippingAddressId()) &&
                condition.apply(criteria.getBillingAddressId(), copy.getBillingAddressId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
