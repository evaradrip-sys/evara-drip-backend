package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.BrandTestSamples.*;
import static com.evaradrip.commerce.domain.CategoryTestSamples.*;
import static com.evaradrip.commerce.domain.InventoryTestSamples.*;
import static com.evaradrip.commerce.domain.ProductImageTestSamples.*;
import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static com.evaradrip.commerce.domain.ProductVariantTestSamples.*;
import static com.evaradrip.commerce.domain.PromotionTestSamples.*;
import static com.evaradrip.commerce.domain.ReviewTestSamples.*;
import static com.evaradrip.commerce.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void imagesTest() {
        Product product = getProductRandomSampleGenerator();
        ProductImage productImageBack = getProductImageRandomSampleGenerator();

        product.addImages(productImageBack);
        assertThat(product.getImages()).containsOnly(productImageBack);
        assertThat(productImageBack.getProduct()).isEqualTo(product);

        product.removeImages(productImageBack);
        assertThat(product.getImages()).doesNotContain(productImageBack);
        assertThat(productImageBack.getProduct()).isNull();

        product.images(new HashSet<>(Set.of(productImageBack)));
        assertThat(product.getImages()).containsOnly(productImageBack);
        assertThat(productImageBack.getProduct()).isEqualTo(product);

        product.setImages(new HashSet<>());
        assertThat(product.getImages()).doesNotContain(productImageBack);
        assertThat(productImageBack.getProduct()).isNull();
    }

    @Test
    void variantsTest() {
        Product product = getProductRandomSampleGenerator();
        ProductVariant productVariantBack = getProductVariantRandomSampleGenerator();

        product.addVariants(productVariantBack);
        assertThat(product.getVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getProduct()).isEqualTo(product);

        product.removeVariants(productVariantBack);
        assertThat(product.getVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getProduct()).isNull();

        product.variants(new HashSet<>(Set.of(productVariantBack)));
        assertThat(product.getVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getProduct()).isEqualTo(product);

        product.setVariants(new HashSet<>());
        assertThat(product.getVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getProduct()).isNull();
    }

    @Test
    void reviewsTest() {
        Product product = getProductRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        product.addReviews(reviewBack);
        assertThat(product.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getProduct()).isEqualTo(product);

        product.removeReviews(reviewBack);
        assertThat(product.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getProduct()).isNull();

        product.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(product.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getProduct()).isEqualTo(product);

        product.setReviews(new HashSet<>());
        assertThat(product.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getProduct()).isNull();
    }

    @Test
    void inventoryTest() {
        Product product = getProductRandomSampleGenerator();
        Inventory inventoryBack = getInventoryRandomSampleGenerator();

        product.addInventory(inventoryBack);
        assertThat(product.getInventories()).containsOnly(inventoryBack);
        assertThat(inventoryBack.getProduct()).isEqualTo(product);

        product.removeInventory(inventoryBack);
        assertThat(product.getInventories()).doesNotContain(inventoryBack);
        assertThat(inventoryBack.getProduct()).isNull();

        product.inventories(new HashSet<>(Set.of(inventoryBack)));
        assertThat(product.getInventories()).containsOnly(inventoryBack);
        assertThat(inventoryBack.getProduct()).isEqualTo(product);

        product.setInventories(new HashSet<>());
        assertThat(product.getInventories()).doesNotContain(inventoryBack);
        assertThat(inventoryBack.getProduct()).isNull();
    }

    @Test
    void promotionsTest() {
        Product product = getProductRandomSampleGenerator();
        Promotion promotionBack = getPromotionRandomSampleGenerator();

        product.addPromotions(promotionBack);
        assertThat(product.getPromotions()).containsOnly(promotionBack);

        product.removePromotions(promotionBack);
        assertThat(product.getPromotions()).doesNotContain(promotionBack);

        product.promotions(new HashSet<>(Set.of(promotionBack)));
        assertThat(product.getPromotions()).containsOnly(promotionBack);

        product.setPromotions(new HashSet<>());
        assertThat(product.getPromotions()).doesNotContain(promotionBack);
    }

    @Test
    void brandTest() {
        Product product = getProductRandomSampleGenerator();
        Brand brandBack = getBrandRandomSampleGenerator();

        product.setBrand(brandBack);
        assertThat(product.getBrand()).isEqualTo(brandBack);

        product.brand(null);
        assertThat(product.getBrand()).isNull();
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void wishlistedTest() {
        Product product = getProductRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        product.addWishlisted(userProfileBack);
        assertThat(product.getWishlisteds()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getWishlists()).containsOnly(product);

        product.removeWishlisted(userProfileBack);
        assertThat(product.getWishlisteds()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getWishlists()).doesNotContain(product);

        product.wishlisteds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(product.getWishlisteds()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getWishlists()).containsOnly(product);

        product.setWishlisteds(new HashSet<>());
        assertThat(product.getWishlisteds()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getWishlists()).doesNotContain(product);
    }

    @Test
    void applicablePromotionsTest() {
        Product product = getProductRandomSampleGenerator();
        Promotion promotionBack = getPromotionRandomSampleGenerator();

        product.addApplicablePromotions(promotionBack);
        assertThat(product.getApplicablePromotions()).containsOnly(promotionBack);
        assertThat(promotionBack.getApplicableProducts()).containsOnly(product);

        product.removeApplicablePromotions(promotionBack);
        assertThat(product.getApplicablePromotions()).doesNotContain(promotionBack);
        assertThat(promotionBack.getApplicableProducts()).doesNotContain(product);

        product.applicablePromotions(new HashSet<>(Set.of(promotionBack)));
        assertThat(product.getApplicablePromotions()).containsOnly(promotionBack);
        assertThat(promotionBack.getApplicableProducts()).containsOnly(product);

        product.setApplicablePromotions(new HashSet<>());
        assertThat(product.getApplicablePromotions()).doesNotContain(promotionBack);
        assertThat(promotionBack.getApplicableProducts()).doesNotContain(product);
    }

    @Test
    void featuredInCategoriesTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.addFeaturedInCategories(categoryBack);
        assertThat(product.getFeaturedInCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getFeaturedProducts()).containsOnly(product);

        product.removeFeaturedInCategories(categoryBack);
        assertThat(product.getFeaturedInCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getFeaturedProducts()).doesNotContain(product);

        product.featuredInCategories(new HashSet<>(Set.of(categoryBack)));
        assertThat(product.getFeaturedInCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getFeaturedProducts()).containsOnly(product);

        product.setFeaturedInCategories(new HashSet<>());
        assertThat(product.getFeaturedInCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getFeaturedProducts()).doesNotContain(product);
    }
}
