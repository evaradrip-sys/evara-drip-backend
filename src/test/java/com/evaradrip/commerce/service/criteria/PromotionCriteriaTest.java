package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PromotionCriteriaTest {

    @Test
    void newPromotionCriteriaHasAllFiltersNullTest() {
        var promotionCriteria = new PromotionCriteria();
        assertThat(promotionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void promotionCriteriaFluentMethodsCreatesFiltersTest() {
        var promotionCriteria = new PromotionCriteria();

        setAllFilters(promotionCriteria);

        assertThat(promotionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void promotionCriteriaCopyCreatesNullFilterTest() {
        var promotionCriteria = new PromotionCriteria();
        var copy = promotionCriteria.copy();

        assertThat(promotionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(promotionCriteria)
        );
    }

    @Test
    void promotionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var promotionCriteria = new PromotionCriteria();
        setAllFilters(promotionCriteria);

        var copy = promotionCriteria.copy();

        assertThat(promotionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(promotionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var promotionCriteria = new PromotionCriteria();

        assertThat(promotionCriteria).hasToString("PromotionCriteria{}");
    }

    private static void setAllFilters(PromotionCriteria promotionCriteria) {
        promotionCriteria.id();
        promotionCriteria.name();
        promotionCriteria.promoCode();
        promotionCriteria.discountType();
        promotionCriteria.discountValue();
        promotionCriteria.minPurchaseAmount();
        promotionCriteria.maxDiscountAmount();
        promotionCriteria.startDate();
        promotionCriteria.endDate();
        promotionCriteria.usageLimit();
        promotionCriteria.usageCount();
        promotionCriteria.isActive();
        promotionCriteria.applicableProductsId();
        promotionCriteria.productsId();
        promotionCriteria.distinct();
    }

    private static Condition<PromotionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPromoCode()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue()) &&
                condition.apply(criteria.getMinPurchaseAmount()) &&
                condition.apply(criteria.getMaxDiscountAmount()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getUsageLimit()) &&
                condition.apply(criteria.getUsageCount()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getApplicableProductsId()) &&
                condition.apply(criteria.getProductsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PromotionCriteria> copyFiltersAre(PromotionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPromoCode(), copy.getPromoCode()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue(), copy.getDiscountValue()) &&
                condition.apply(criteria.getMinPurchaseAmount(), copy.getMinPurchaseAmount()) &&
                condition.apply(criteria.getMaxDiscountAmount(), copy.getMaxDiscountAmount()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getUsageLimit(), copy.getUsageLimit()) &&
                condition.apply(criteria.getUsageCount(), copy.getUsageCount()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getApplicableProductsId(), copy.getApplicableProductsId()) &&
                condition.apply(criteria.getProductsId(), copy.getProductsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
