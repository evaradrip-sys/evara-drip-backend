package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.CategoryTestSamples.*;
import static com.evaradrip.commerce.domain.CategoryTestSamples.*;
import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void productsTest() {
        Category category = getCategoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        category.addProducts(productBack);
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.removeProducts(productBack);
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();

        category.products(new HashSet<>(Set.of(productBack)));
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.setProducts(new HashSet<>());
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();
    }

    @Test
    void subcategoriesTest() {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.addSubcategories(categoryBack);
        assertThat(category.getSubcategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getParent()).isEqualTo(category);

        category.removeSubcategories(categoryBack);
        assertThat(category.getSubcategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getParent()).isNull();

        category.subcategories(new HashSet<>(Set.of(categoryBack)));
        assertThat(category.getSubcategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getParent()).isEqualTo(category);

        category.setSubcategories(new HashSet<>());
        assertThat(category.getSubcategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getParent()).isNull();
    }

    @Test
    void featuredProductsTest() {
        Category category = getCategoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        category.addFeaturedProducts(productBack);
        assertThat(category.getFeaturedProducts()).containsOnly(productBack);

        category.removeFeaturedProducts(productBack);
        assertThat(category.getFeaturedProducts()).doesNotContain(productBack);

        category.featuredProducts(new HashSet<>(Set.of(productBack)));
        assertThat(category.getFeaturedProducts()).containsOnly(productBack);

        category.setFeaturedProducts(new HashSet<>());
        assertThat(category.getFeaturedProducts()).doesNotContain(productBack);
    }

    @Test
    void parentTest() {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.setParent(categoryBack);
        assertThat(category.getParent()).isEqualTo(categoryBack);

        category.parent(null);
        assertThat(category.getParent()).isNull();
    }
}
