package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.UserAddressTestSamples.*;
import static com.evaradrip.commerce.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAddress.class);
        UserAddress userAddress1 = getUserAddressSample1();
        UserAddress userAddress2 = new UserAddress();
        assertThat(userAddress1).isNotEqualTo(userAddress2);

        userAddress2.setId(userAddress1.getId());
        assertThat(userAddress1).isEqualTo(userAddress2);

        userAddress2 = getUserAddressSample2();
        assertThat(userAddress1).isNotEqualTo(userAddress2);
    }

    @Test
    void userTest() {
        UserAddress userAddress = getUserAddressRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userAddress.setUser(userProfileBack);
        assertThat(userAddress.getUser()).isEqualTo(userProfileBack);

        userAddress.user(null);
        assertThat(userAddress.getUser()).isNull();
    }
}
