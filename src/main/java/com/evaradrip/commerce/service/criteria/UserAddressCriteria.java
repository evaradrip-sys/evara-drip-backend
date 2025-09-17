package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.AddressType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.UserAddress} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.UserAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAddressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AddressType
     */
    public static class AddressTypeFilter extends Filter<AddressType> {

        public AddressTypeFilter() {}

        public AddressTypeFilter(AddressTypeFilter filter) {
            super(filter);
        }

        @Override
        public AddressTypeFilter copy() {
            return new AddressTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AddressTypeFilter addressType;

    private StringFilter fullName;

    private StringFilter phoneNumber;

    private StringFilter streetAddress;

    private StringFilter streetAddress2;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipCode;

    private StringFilter country;

    private StringFilter landmark;

    private BooleanFilter isDefault;

    private LongFilter userId;

    private Boolean distinct;

    public UserAddressCriteria() {}

    public UserAddressCriteria(UserAddressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.addressType = other.optionalAddressType().map(AddressTypeFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.streetAddress = other.optionalStreetAddress().map(StringFilter::copy).orElse(null);
        this.streetAddress2 = other.optionalStreetAddress2().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.state = other.optionalState().map(StringFilter::copy).orElse(null);
        this.zipCode = other.optionalZipCode().map(StringFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.landmark = other.optionalLandmark().map(StringFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserAddressCriteria copy() {
        return new UserAddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public AddressTypeFilter getAddressType() {
        return addressType;
    }

    public Optional<AddressTypeFilter> optionalAddressType() {
        return Optional.ofNullable(addressType);
    }

    public AddressTypeFilter addressType() {
        if (addressType == null) {
            setAddressType(new AddressTypeFilter());
        }
        return addressType;
    }

    public void setAddressType(AddressTypeFilter addressType) {
        this.addressType = addressType;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public Optional<StringFilter> optionalStreetAddress() {
        return Optional.ofNullable(streetAddress);
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            setStreetAddress(new StringFilter());
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getStreetAddress2() {
        return streetAddress2;
    }

    public Optional<StringFilter> optionalStreetAddress2() {
        return Optional.ofNullable(streetAddress2);
    }

    public StringFilter streetAddress2() {
        if (streetAddress2 == null) {
            setStreetAddress2(new StringFilter());
        }
        return streetAddress2;
    }

    public void setStreetAddress2(StringFilter streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public Optional<StringFilter> optionalState() {
        return Optional.ofNullable(state);
    }

    public StringFilter state() {
        if (state == null) {
            setState(new StringFilter());
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public Optional<StringFilter> optionalZipCode() {
        return Optional.ofNullable(zipCode);
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            setZipCode(new StringFilter());
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getLandmark() {
        return landmark;
    }

    public Optional<StringFilter> optionalLandmark() {
        return Optional.ofNullable(landmark);
    }

    public StringFilter landmark() {
        if (landmark == null) {
            setLandmark(new StringFilter());
        }
        return landmark;
    }

    public void setLandmark(StringFilter landmark) {
        this.landmark = landmark;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserAddressCriteria that = (UserAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(addressType, that.addressType) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(streetAddress2, that.streetAddress2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(country, that.country) &&
            Objects.equals(landmark, that.landmark) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            addressType,
            fullName,
            phoneNumber,
            streetAddress,
            streetAddress2,
            city,
            state,
            zipCode,
            country,
            landmark,
            isDefault,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAddressType().map(f -> "addressType=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalStreetAddress().map(f -> "streetAddress=" + f + ", ").orElse("") +
            optionalStreetAddress2().map(f -> "streetAddress2=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalState().map(f -> "state=" + f + ", ").orElse("") +
            optionalZipCode().map(f -> "zipCode=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalLandmark().map(f -> "landmark=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
