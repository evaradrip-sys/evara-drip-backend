package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.CouponTestSamples.*;
import static com.evaradrip.commerce.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coupon.class);
        Coupon coupon1 = getCouponSample1();
        Coupon coupon2 = new Coupon();
        assertThat(coupon1).isNotEqualTo(coupon2);

        coupon2.setId(coupon1.getId());
        assertThat(coupon1).isEqualTo(coupon2);

        coupon2 = getCouponSample2();
        assertThat(coupon1).isNotEqualTo(coupon2);
    }

    @Test
    void usersTest() {
        Coupon coupon = getCouponRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        coupon.addUsers(userProfileBack);
        assertThat(coupon.getUsers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getCoupons()).containsOnly(coupon);

        coupon.removeUsers(userProfileBack);
        assertThat(coupon.getUsers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getCoupons()).doesNotContain(coupon);

        coupon.users(new HashSet<>(Set.of(userProfileBack)));
        assertThat(coupon.getUsers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getCoupons()).containsOnly(coupon);

        coupon.setUsers(new HashSet<>());
        assertThat(coupon.getUsers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getCoupons()).doesNotContain(coupon);
    }
}
