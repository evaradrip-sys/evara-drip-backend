package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCriteriaTest {

    @Test
    void newProductCriteriaHasAllFiltersNullTest() {
        var productCriteria = new ProductCriteria();
        assertThat(productCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCriteriaFluentMethodsCreatesFiltersTest() {
        var productCriteria = new ProductCriteria();

        setAllFilters(productCriteria);

        assertThat(productCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCriteriaCopyCreatesNullFilterTest() {
        var productCriteria = new ProductCriteria();
        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void productCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCriteria = new ProductCriteria();
        setAllFilters(productCriteria);

        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCriteria = new ProductCriteria();

        assertThat(productCriteria).hasToString("ProductCriteria{}");
    }

    private static void setAllFilters(ProductCriteria productCriteria) {
        productCriteria.id();
        productCriteria.name();
        productCriteria.price();
        productCriteria.originalPrice();
        productCriteria.sku();
        productCriteria.isNew();
        productCriteria.isOnSale();
        productCriteria.rating();
        productCriteria.reviewsCount();
        productCriteria.stockCount();
        productCriteria.inStock();
        productCriteria.metaTitle();
        productCriteria.metaDescription();
        productCriteria.metaKeywords();
        productCriteria.status();
        productCriteria.weight();
        productCriteria.dimensions();
        productCriteria.imagesId();
        productCriteria.variantsId();
        productCriteria.reviewsId();
        productCriteria.inventoryId();
        productCriteria.promotionsId();
        productCriteria.brandId();
        productCriteria.categoryId();
        productCriteria.wishlistedId();
        productCriteria.applicablePromotionsId();
        productCriteria.featuredInCategoriesId();
        productCriteria.distinct();
    }

    private static Condition<ProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getOriginalPrice()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getIsNew()) &&
                condition.apply(criteria.getIsOnSale()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getReviewsCount()) &&
                condition.apply(criteria.getStockCount()) &&
                condition.apply(criteria.getInStock()) &&
                condition.apply(criteria.getMetaTitle()) &&
                condition.apply(criteria.getMetaDescription()) &&
                condition.apply(criteria.getMetaKeywords()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getDimensions()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getVariantsId()) &&
                condition.apply(criteria.getReviewsId()) &&
                condition.apply(criteria.getInventoryId()) &&
                condition.apply(criteria.getPromotionsId()) &&
                condition.apply(criteria.getBrandId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getWishlistedId()) &&
                condition.apply(criteria.getApplicablePromotionsId()) &&
                condition.apply(criteria.getFeaturedInCategoriesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCriteria> copyFiltersAre(ProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getOriginalPrice(), copy.getOriginalPrice()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getIsNew(), copy.getIsNew()) &&
                condition.apply(criteria.getIsOnSale(), copy.getIsOnSale()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getReviewsCount(), copy.getReviewsCount()) &&
                condition.apply(criteria.getStockCount(), copy.getStockCount()) &&
                condition.apply(criteria.getInStock(), copy.getInStock()) &&
                condition.apply(criteria.getMetaTitle(), copy.getMetaTitle()) &&
                condition.apply(criteria.getMetaDescription(), copy.getMetaDescription()) &&
                condition.apply(criteria.getMetaKeywords(), copy.getMetaKeywords()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getDimensions(), copy.getDimensions()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getVariantsId(), copy.getVariantsId()) &&
                condition.apply(criteria.getReviewsId(), copy.getReviewsId()) &&
                condition.apply(criteria.getInventoryId(), copy.getInventoryId()) &&
                condition.apply(criteria.getPromotionsId(), copy.getPromotionsId()) &&
                condition.apply(criteria.getBrandId(), copy.getBrandId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getWishlistedId(), copy.getWishlistedId()) &&
                condition.apply(criteria.getApplicablePromotionsId(), copy.getApplicablePromotionsId()) &&
                condition.apply(criteria.getFeaturedInCategoriesId(), copy.getFeaturedInCategoriesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
