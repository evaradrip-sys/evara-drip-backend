package com.evaradrip.commerce.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserAddressCriteriaTest {

    @Test
    void newUserAddressCriteriaHasAllFiltersNullTest() {
        var userAddressCriteria = new UserAddressCriteria();
        assertThat(userAddressCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userAddressCriteriaFluentMethodsCreatesFiltersTest() {
        var userAddressCriteria = new UserAddressCriteria();

        setAllFilters(userAddressCriteria);

        assertThat(userAddressCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userAddressCriteriaCopyCreatesNullFilterTest() {
        var userAddressCriteria = new UserAddressCriteria();
        var copy = userAddressCriteria.copy();

        assertThat(userAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userAddressCriteria)
        );
    }

    @Test
    void userAddressCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userAddressCriteria = new UserAddressCriteria();
        setAllFilters(userAddressCriteria);

        var copy = userAddressCriteria.copy();

        assertThat(userAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userAddressCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userAddressCriteria = new UserAddressCriteria();

        assertThat(userAddressCriteria).hasToString("UserAddressCriteria{}");
    }

    private static void setAllFilters(UserAddressCriteria userAddressCriteria) {
        userAddressCriteria.id();
        userAddressCriteria.addressType();
        userAddressCriteria.fullName();
        userAddressCriteria.phoneNumber();
        userAddressCriteria.streetAddress();
        userAddressCriteria.streetAddress2();
        userAddressCriteria.city();
        userAddressCriteria.state();
        userAddressCriteria.zipCode();
        userAddressCriteria.country();
        userAddressCriteria.landmark();
        userAddressCriteria.isDefault();
        userAddressCriteria.userId();
        userAddressCriteria.distinct();
    }

    private static Condition<UserAddressCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAddressType()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getStreetAddress()) &&
                condition.apply(criteria.getStreetAddress2()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getState()) &&
                condition.apply(criteria.getZipCode()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getLandmark()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserAddressCriteria> copyFiltersAre(UserAddressCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAddressType(), copy.getAddressType()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getStreetAddress(), copy.getStreetAddress()) &&
                condition.apply(criteria.getStreetAddress2(), copy.getStreetAddress2()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getState(), copy.getState()) &&
                condition.apply(criteria.getZipCode(), copy.getZipCode()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getLandmark(), copy.getLandmark()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
