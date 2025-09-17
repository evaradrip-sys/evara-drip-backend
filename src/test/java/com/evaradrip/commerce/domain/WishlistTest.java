package com.evaradrip.commerce.domain;

import static com.evaradrip.commerce.domain.WishlistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evaradrip.commerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WishlistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wishlist.class);
        Wishlist wishlist1 = getWishlistSample1();
        Wishlist wishlist2 = new Wishlist();
        assertThat(wishlist1).isNotEqualTo(wishlist2);

        wishlist2.setId(wishlist1.getId());
        assertThat(wishlist1).isEqualTo(wishlist2);

        wishlist2 = getWishlistSample2();
        assertThat(wishlist1).isNotEqualTo(wishlist2);
    }
}
