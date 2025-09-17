package com.evaradrip.commerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPromotionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPromotionDTO.class);
        ProductPromotionDTO productPromotionDTO1 = new ProductPromotionDTO();
        productPromotionDTO1.setId(1L);
        ProductPromotionDTO productPromotionDTO2 = new ProductPromotionDTO();
        assertThat(productPromotionDTO1).isNotEqualTo(productPromotionDTO2);
        productPromotionDTO2.setId(productPromotionDTO1.getId());
        assertThat(productPromotionDTO1).isEqualTo(productPromotionDTO2);
        productPromotionDTO2.setId(2L);
        assertThat(productPromotionDTO1).isNotEqualTo(productPromotionDTO2);
        productPromotionDTO1.setId(null);
        assertThat(productPromotionDTO1).isNotEqualTo(productPromotionDTO2);
    }
}
