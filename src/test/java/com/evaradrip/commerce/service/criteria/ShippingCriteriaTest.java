package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShippingCriteriaTest {

    @Test
    void newShippingCriteriaHasAllFiltersNullTest() {
        var shippingCriteria = new ShippingCriteria();
        assertThat(shippingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void shippingCriteriaFluentMethodsCreatesFiltersTest() {
        var shippingCriteria = new ShippingCriteria();

        setAllFilters(shippingCriteria);

        assertThat(shippingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void shippingCriteriaCopyCreatesNullFilterTest() {
        var shippingCriteria = new ShippingCriteria();
        var copy = shippingCriteria.copy();

        assertThat(shippingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(shippingCriteria)
        );
    }

    @Test
    void shippingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shippingCriteria = new ShippingCriteria();
        setAllFilters(shippingCriteria);

        var copy = shippingCriteria.copy();

        assertThat(shippingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(shippingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shippingCriteria = new ShippingCriteria();

        assertThat(shippingCriteria).hasToString("ShippingCriteria{}");
    }

    private static void setAllFilters(ShippingCriteria shippingCriteria) {
        shippingCriteria.id();
        shippingCriteria.carrier();
        shippingCriteria.trackingNumber();
        shippingCriteria.estimatedDelivery();
        shippingCriteria.actualDelivery();
        shippingCriteria.shippingCost();
        shippingCriteria.status();
        shippingCriteria.orderId();
        shippingCriteria.distinct();
    }

    private static Condition<ShippingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCarrier()) &&
                condition.apply(criteria.getTrackingNumber()) &&
                condition.apply(criteria.getEstimatedDelivery()) &&
                condition.apply(criteria.getActualDelivery()) &&
                condition.apply(criteria.getShippingCost()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShippingCriteria> copyFiltersAre(ShippingCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCarrier(), copy.getCarrier()) &&
                condition.apply(criteria.getTrackingNumber(), copy.getTrackingNumber()) &&
                condition.apply(criteria.getEstimatedDelivery(), copy.getEstimatedDelivery()) &&
                condition.apply(criteria.getActualDelivery(), copy.getActualDelivery()) &&
                condition.apply(criteria.getShippingCost(), copy.getShippingCost()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
