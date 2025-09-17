package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.CouponTestSamples.*;
import static com.evaradrip.commerce.domain.NotificationTestSamples.*;
import static com.evaradrip.commerce.domain.OrderTestSamples.*;
import static com.evaradrip.commerce.domain.ProductTestSamples.*;
import static com.evaradrip.commerce.domain.UserAddressTestSamples.*;
import static com.evaradrip.commerce.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void addressesTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserAddress userAddressBack = getUserAddressRandomSampleGenerator();

        userProfile.addAddresses(userAddressBack);
        assertThat(userProfile.getAddresses()).containsOnly(userAddressBack);
        assertThat(userAddressBack.getUser()).isEqualTo(userProfile);

        userProfile.removeAddresses(userAddressBack);
        assertThat(userProfile.getAddresses()).doesNotContain(userAddressBack);
        assertThat(userAddressBack.getUser()).isNull();

        userProfile.addresses(new HashSet<>(Set.of(userAddressBack)));
        assertThat(userProfile.getAddresses()).containsOnly(userAddressBack);
        assertThat(userAddressBack.getUser()).isEqualTo(userProfile);

        userProfile.setAddresses(new HashSet<>());
        assertThat(userProfile.getAddresses()).doesNotContain(userAddressBack);
        assertThat(userAddressBack.getUser()).isNull();
    }

    @Test
    void ordersTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        userProfile.addOrders(orderBack);
        assertThat(userProfile.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getUser()).isEqualTo(userProfile);

        userProfile.removeOrders(orderBack);
        assertThat(userProfile.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getUser()).isNull();

        userProfile.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(userProfile.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getUser()).isEqualTo(userProfile);

        userProfile.setOrders(new HashSet<>());
        assertThat(userProfile.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getUser()).isNull();
    }

    @Test
    void notificationsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userProfile.addNotifications(notificationBack);
        assertThat(userProfile.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getUser()).isEqualTo(userProfile);

        userProfile.removeNotifications(notificationBack);
        assertThat(userProfile.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getUser()).isNull();

        userProfile.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(userProfile.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getUser()).isEqualTo(userProfile);

        userProfile.setNotifications(new HashSet<>());
        assertThat(userProfile.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getUser()).isNull();
    }

    @Test
    void wishlistTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        userProfile.addWishlist(productBack);
        assertThat(userProfile.getWishlists()).containsOnly(productBack);

        userProfile.removeWishlist(productBack);
        assertThat(userProfile.getWishlists()).doesNotContain(productBack);

        userProfile.wishlists(new HashSet<>(Set.of(productBack)));
        assertThat(userProfile.getWishlists()).containsOnly(productBack);

        userProfile.setWishlists(new HashSet<>());
        assertThat(userProfile.getWishlists()).doesNotContain(productBack);
    }

    @Test
    void couponsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Coupon couponBack = getCouponRandomSampleGenerator();

        userProfile.addCoupons(couponBack);
        assertThat(userProfile.getCoupons()).containsOnly(couponBack);

        userProfile.removeCoupons(couponBack);
        assertThat(userProfile.getCoupons()).doesNotContain(couponBack);

        userProfile.coupons(new HashSet<>(Set.of(couponBack)));
        assertThat(userProfile.getCoupons()).containsOnly(couponBack);

        userProfile.setCoupons(new HashSet<>());
        assertThat(userProfile.getCoupons()).doesNotContain(couponBack);
    }
}
