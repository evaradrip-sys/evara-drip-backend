package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static com.evaradrip.commerce.domain.PromotionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotion.class);
        Promotion promotion1 = getPromotionSample1();
        Promotion promotion2 = new Promotion();
        assertThat(promotion1).isNotEqualTo(promotion2);

        promotion2.setId(promotion1.getId());
        assertThat(promotion1).isEqualTo(promotion2);

        promotion2 = getPromotionSample2();
        assertThat(promotion1).isNotEqualTo(promotion2);
    }

    @Test
    void applicableProductsTest() {
        Promotion promotion = getPromotionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        promotion.addApplicableProducts(productBack);
        assertThat(promotion.getApplicableProducts()).containsOnly(productBack);

        promotion.removeApplicableProducts(productBack);
        assertThat(promotion.getApplicableProducts()).doesNotContain(productBack);

        promotion.applicableProducts(new HashSet<>(Set.of(productBack)));
        assertThat(promotion.getApplicableProducts()).containsOnly(productBack);

        promotion.setApplicableProducts(new HashSet<>());
        assertThat(promotion.getApplicableProducts()).doesNotContain(productBack);
    }

    @Test
    void productsTest() {
        Promotion promotion = getPromotionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        promotion.addProducts(productBack);
        assertThat(promotion.getProducts()).containsOnly(productBack);
        assertThat(productBack.getPromotions()).containsOnly(promotion);

        promotion.removeProducts(productBack);
        assertThat(promotion.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getPromotions()).doesNotContain(promotion);

        promotion.products(new HashSet<>(Set.of(productBack)));
        assertThat(promotion.getProducts()).containsOnly(productBack);
        assertThat(productBack.getPromotions()).containsOnly(promotion);

        promotion.setProducts(new HashSet<>());
        assertThat(promotion.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getPromotions()).doesNotContain(promotion);
    }
}
