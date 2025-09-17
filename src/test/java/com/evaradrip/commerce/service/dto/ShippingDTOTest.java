package com.evaradrip.commerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingDTO.class);
        ShippingDTO shippingDTO1 = new ShippingDTO();
        shippingDTO1.setId(1L);
        ShippingDTO shippingDTO2 = new ShippingDTO();
        assertThat(shippingDTO1).isNotEqualTo(shippingDTO2);
        shippingDTO2.setId(shippingDTO1.getId());
        assertThat(shippingDTO1).isEqualTo(shippingDTO2);
        shippingDTO2.setId(2L);
        assertThat(shippingDTO1).isNotEqualTo(shippingDTO2);
        shippingDTO1.setId(null);
        assertThat(shippingDTO1).isNotEqualTo(shippingDTO2);
    }
}
