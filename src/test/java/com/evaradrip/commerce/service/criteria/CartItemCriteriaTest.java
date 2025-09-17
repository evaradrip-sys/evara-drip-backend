package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CartItemCriteriaTest {

    @Test
    void newCartItemCriteriaHasAllFiltersNullTest() {
        var cartItemCriteria = new CartItemCriteria();
        assertThat(cartItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cartItemCriteriaFluentMethodsCreatesFiltersTest() {
        var cartItemCriteria = new CartItemCriteria();

        setAllFilters(cartItemCriteria);

        assertThat(cartItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cartItemCriteriaCopyCreatesNullFilterTest() {
        var cartItemCriteria = new CartItemCriteria();
        var copy = cartItemCriteria.copy();

        assertThat(cartItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cartItemCriteria)
        );
    }

    @Test
    void cartItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cartItemCriteria = new CartItemCriteria();
        setAllFilters(cartItemCriteria);

        var copy = cartItemCriteria.copy();

        assertThat(cartItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cartItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cartItemCriteria = new CartItemCriteria();

        assertThat(cartItemCriteria).hasToString("CartItemCriteria{}");
    }

    private static void setAllFilters(CartItemCriteria cartItemCriteria) {
        cartItemCriteria.id();
        cartItemCriteria.quantity();
        cartItemCriteria.addedPrice();
        cartItemCriteria.productId();
        cartItemCriteria.variantId();
        cartItemCriteria.cartId();
        cartItemCriteria.distinct();
    }

    private static Condition<CartItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getAddedPrice()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getVariantId()) &&
                condition.apply(criteria.getCartId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CartItemCriteria> copyFiltersAre(CartItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getAddedPrice(), copy.getAddedPrice()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getVariantId(), copy.getVariantId()) &&
                condition.apply(criteria.getCartId(), copy.getCartId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
