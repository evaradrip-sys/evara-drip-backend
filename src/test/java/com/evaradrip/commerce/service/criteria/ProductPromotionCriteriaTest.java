package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductPromotionCriteriaTest {

    @Test
    void newProductPromotionCriteriaHasAllFiltersNullTest() {
        var productPromotionCriteria = new ProductPromotionCriteria();
        assertThat(productPromotionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productPromotionCriteriaFluentMethodsCreatesFiltersTest() {
        var productPromotionCriteria = new ProductPromotionCriteria();

        setAllFilters(productPromotionCriteria);

        assertThat(productPromotionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productPromotionCriteriaCopyCreatesNullFilterTest() {
        var productPromotionCriteria = new ProductPromotionCriteria();
        var copy = productPromotionCriteria.copy();

        assertThat(productPromotionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productPromotionCriteria)
        );
    }

    @Test
    void productPromotionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productPromotionCriteria = new ProductPromotionCriteria();
        setAllFilters(productPromotionCriteria);

        var copy = productPromotionCriteria.copy();

        assertThat(productPromotionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productPromotionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productPromotionCriteria = new ProductPromotionCriteria();

        assertThat(productPromotionCriteria).hasToString("ProductPromotionCriteria{}");
    }

    private static void setAllFilters(ProductPromotionCriteria productPromotionCriteria) {
        productPromotionCriteria.id();
        productPromotionCriteria.priority();
        productPromotionCriteria.isExclusive();
        productPromotionCriteria.distinct();
    }

    private static Condition<ProductPromotionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getIsExclusive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductPromotionCriteria> copyFiltersAre(
        ProductPromotionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getIsExclusive(), copy.getIsExclusive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
