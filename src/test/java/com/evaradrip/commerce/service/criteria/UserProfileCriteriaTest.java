package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserProfileCriteriaTest {

    @Test
    void newUserProfileCriteriaHasAllFiltersNullTest() {
        var userProfileCriteria = new UserProfileCriteria();
        assertThat(userProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var userProfileCriteria = new UserProfileCriteria();

        setAllFilters(userProfileCriteria);

        assertThat(userProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userProfileCriteriaCopyCreatesNullFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void userProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        setAllFilters(userProfileCriteria);

        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userProfileCriteria = new UserProfileCriteria();

        assertThat(userProfileCriteria).hasToString("UserProfileCriteria{}");
    }

    private static void setAllFilters(UserProfileCriteria userProfileCriteria) {
        userProfileCriteria.id();
        userProfileCriteria.phoneNumber();
        userProfileCriteria.dateOfBirth();
        userProfileCriteria.gender();
        userProfileCriteria.avatarUrl();
        userProfileCriteria.loyaltyPoints();
        userProfileCriteria.membershipLevel();
        userProfileCriteria.newsletterSubscribed();
        userProfileCriteria.userId();
        userProfileCriteria.addressesId();
        userProfileCriteria.ordersId();
        userProfileCriteria.notificationsId();
        userProfileCriteria.wishlistId();
        userProfileCriteria.couponsId();
        userProfileCriteria.distinct();
    }

    private static Condition<UserProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getAvatarUrl()) &&
                condition.apply(criteria.getLoyaltyPoints()) &&
                condition.apply(criteria.getMembershipLevel()) &&
                condition.apply(criteria.getNewsletterSubscribed()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getAddressesId()) &&
                condition.apply(criteria.getOrdersId()) &&
                condition.apply(criteria.getNotificationsId()) &&
                condition.apply(criteria.getWishlistId()) &&
                condition.apply(criteria.getCouponsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserProfileCriteria> copyFiltersAre(UserProfileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getAvatarUrl(), copy.getAvatarUrl()) &&
                condition.apply(criteria.getLoyaltyPoints(), copy.getLoyaltyPoints()) &&
                condition.apply(criteria.getMembershipLevel(), copy.getMembershipLevel()) &&
                condition.apply(criteria.getNewsletterSubscribed(), copy.getNewsletterSubscribed()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getAddressesId(), copy.getAddressesId()) &&
                condition.apply(criteria.getOrdersId(), copy.getOrdersId()) &&
                condition.apply(criteria.getNotificationsId(), copy.getNotificationsId()) &&
                condition.apply(criteria.getWishlistId(), copy.getWishlistId()) &&
                condition.apply(criteria.getCouponsId(), copy.getCouponsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
