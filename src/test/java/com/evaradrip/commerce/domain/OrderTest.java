package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.OrderItemTestSamples.*;
import static com.evaradrip.commerce.domain.OrderTestSamples.*;
import static com.evaradrip.commerce.domain.PaymentTestSamples.*;
import static com.evaradrip.commerce.domain.ShippingTestSamples.*;
import static com.evaradrip.commerce.domain.UserAddressTestSamples.*;
import static com.evaradrip.commerce.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void itemsTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        order.addItems(orderItemBack);
        assertThat(order.getItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.removeItems(orderItemBack);
        assertThat(order.getItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();

        order.items(new HashSet<>(Set.of(orderItemBack)));
        assertThat(order.getItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getOrder()).isEqualTo(order);

        order.setItems(new HashSet<>());
        assertThat(order.getItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getOrder()).isNull();
    }

    @Test
    void paymentsTest() {
        Order order = getOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        order.addPayments(paymentBack);
        assertThat(order.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getOrder()).isEqualTo(order);

        order.removePayments(paymentBack);
        assertThat(order.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getOrder()).isNull();

        order.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(order.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getOrder()).isEqualTo(order);

        order.setPayments(new HashSet<>());
        assertThat(order.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getOrder()).isNull();
    }

    @Test
    void shippingTest() {
        Order order = getOrderRandomSampleGenerator();
        Shipping shippingBack = getShippingRandomSampleGenerator();

        order.addShipping(shippingBack);
        assertThat(order.getShippings()).containsOnly(shippingBack);
        assertThat(shippingBack.getOrder()).isEqualTo(order);

        order.removeShipping(shippingBack);
        assertThat(order.getShippings()).doesNotContain(shippingBack);
        assertThat(shippingBack.getOrder()).isNull();

        order.shippings(new HashSet<>(Set.of(shippingBack)));
        assertThat(order.getShippings()).containsOnly(shippingBack);
        assertThat(shippingBack.getOrder()).isEqualTo(order);

        order.setShippings(new HashSet<>());
        assertThat(order.getShippings()).doesNotContain(shippingBack);
        assertThat(shippingBack.getOrder()).isNull();
    }

    @Test
    void shippingAddressTest() {
        Order order = getOrderRandomSampleGenerator();
        UserAddress userAddressBack = getUserAddressRandomSampleGenerator();

        order.setShippingAddress(userAddressBack);
        assertThat(order.getShippingAddress()).isEqualTo(userAddressBack);

        order.shippingAddress(null);
        assertThat(order.getShippingAddress()).isNull();
    }

    @Test
    void billingAddressTest() {
        Order order = getOrderRandomSampleGenerator();
        UserAddress userAddressBack = getUserAddressRandomSampleGenerator();

        order.setBillingAddress(userAddressBack);
        assertThat(order.getBillingAddress()).isEqualTo(userAddressBack);

        order.billingAddress(null);
        assertThat(order.getBillingAddress()).isNull();
    }

    @Test
    void userTest() {
        Order order = getOrderRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        order.setUser(userProfileBack);
        assertThat(order.getUser()).isEqualTo(userProfileBack);

        order.user(null);
        assertThat(order.getUser()).isNull();
    }
}
