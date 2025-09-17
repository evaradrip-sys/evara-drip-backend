package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.ProductPromotionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPromotionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPromotion.class);
        ProductPromotion productPromotion1 = getProductPromotionSample1();
        ProductPromotion productPromotion2 = new ProductPromotion();
        assertThat(productPromotion1).isNotEqualTo(productPromotion2);

        productPromotion2.setId(productPromotion1.getId());
        assertThat(productPromotion1).isEqualTo(productPromotion2);

        productPromotion2 = getProductPromotionSample2();
        assertThat(productPromotion1).isNotEqualTo(productPromotion2);
    }
}
