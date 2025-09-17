package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.InventoryTestSamples.*;
import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = getInventorySample1();
        Inventory inventory2 = new Inventory();
        assertThat(inventory1).isNotEqualTo(inventory2);

        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);

        inventory2 = getInventorySample2();
        assertThat(inventory1).isNotEqualTo(inventory2);
    }

    @Test
    void productTest() {
        Inventory inventory = getInventoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        inventory.setProduct(productBack);
        assertThat(inventory.getProduct()).isEqualTo(productBack);

        inventory.product(null);
        assertThat(inventory.getProduct()).isNull();
    }
}
