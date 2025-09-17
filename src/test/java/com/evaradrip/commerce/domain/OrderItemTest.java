package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.OrderItemTestSamples.*;
import static com.evaradrip.commerce.domain.OrderTestSamples.*;
import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static com.evaradrip.commerce.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void productTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderItem.setProduct(productBack);
        assertThat(orderItem.getProduct()).isEqualTo(productBack);

        orderItem.product(null);
        assertThat(orderItem.getProduct()).isNull();
    }

    @Test
    void variantTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        ProductVariant productVariantBack = getProductVariantRandomSampleGenerator();

        orderItem.setVariant(productVariantBack);
        assertThat(orderItem.getVariant()).isEqualTo(productVariantBack);

        orderItem.variant(null);
        assertThat(orderItem.getVariant()).isNull();
    }

    @Test
    void orderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderItem.setOrder(orderBack);
        assertThat(orderItem.getOrder()).isEqualTo(orderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }
}
