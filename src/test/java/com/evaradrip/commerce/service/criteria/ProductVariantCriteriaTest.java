package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductVariantCriteriaTest {

    @Test
    void newProductVariantCriteriaHasAllFiltersNullTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        assertThat(productVariantCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productVariantCriteriaFluentMethodsCreatesFiltersTest() {
        var productVariantCriteria = new ProductVariantCriteria();

        setAllFilters(productVariantCriteria);

        assertThat(productVariantCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productVariantCriteriaCopyCreatesNullFilterTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        var copy = productVariantCriteria.copy();

        assertThat(productVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productVariantCriteria)
        );
    }

    @Test
    void productVariantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        setAllFilters(productVariantCriteria);

        var copy = productVariantCriteria.copy();

        assertThat(productVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productVariantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productVariantCriteria = new ProductVariantCriteria();

        assertThat(productVariantCriteria).hasToString("ProductVariantCriteria{}");
    }

    private static void setAllFilters(ProductVariantCriteria productVariantCriteria) {
        productVariantCriteria.id();
        productVariantCriteria.variantSize();
        productVariantCriteria.color();
        productVariantCriteria.sku();
        productVariantCriteria.stockCount();
        productVariantCriteria.priceAdjustment();
        productVariantCriteria.barcode();
        productVariantCriteria.weight();
        productVariantCriteria.productId();
        productVariantCriteria.distinct();
    }

    private static Condition<ProductVariantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVariantSize()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getStockCount()) &&
                condition.apply(criteria.getPriceAdjustment()) &&
                condition.apply(criteria.getBarcode()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductVariantCriteria> copyFiltersAre(
        ProductVariantCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVariantSize(), copy.getVariantSize()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getStockCount(), copy.getStockCount()) &&
                condition.apply(criteria.getPriceAdjustment(), copy.getPriceAdjustment()) &&
                condition.apply(criteria.getBarcode(), copy.getBarcode()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
