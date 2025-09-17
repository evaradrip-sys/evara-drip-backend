package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.Gender;
import com.evaradrip.commerce.domain.enumeration.MembershipLevel;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.UserProfile} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering MembershipLevel
     */
    public static class MembershipLevelFilter extends Filter<MembershipLevel> {

        public MembershipLevelFilter() {}

        public MembershipLevelFilter(MembershipLevelFilter filter) {
            super(filter);
        }

        @Override
        public MembershipLevelFilter copy() {
            return new MembershipLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phoneNumber;

    private LocalDateFilter dateOfBirth;

    private GenderFilter gender;

    private StringFilter avatarUrl;

    private IntegerFilter loyaltyPoints;

    private MembershipLevelFilter membershipLevel;

    private BooleanFilter newsletterSubscribed;

    private LongFilter userId;

    private LongFilter addressesId;

    private LongFilter ordersId;

    private LongFilter notificationsId;

    private LongFilter wishlistId;

    private LongFilter couponsId;

    private Boolean distinct;

    public UserProfileCriteria() {}

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.dateOfBirth = other.optionalDateOfBirth().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.avatarUrl = other.optionalAvatarUrl().map(StringFilter::copy).orElse(null);
        this.loyaltyPoints = other.optionalLoyaltyPoints().map(IntegerFilter::copy).orElse(null);
        this.membershipLevel = other.optionalMembershipLevel().map(MembershipLevelFilter::copy).orElse(null);
        this.newsletterSubscribed = other.optionalNewsletterSubscribed().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.addressesId = other.optionalAddressesId().map(LongFilter::copy).orElse(null);
        this.ordersId = other.optionalOrdersId().map(LongFilter::copy).orElse(null);
        this.notificationsId = other.optionalNotificationsId().map(LongFilter::copy).orElse(null);
        this.wishlistId = other.optionalWishlistId().map(LongFilter::copy).orElse(null);
        this.couponsId = other.optionalCouponsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
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

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDateFilter> optionalDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            setDateOfBirth(new LocalDateFilter());
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getAvatarUrl() {
        return avatarUrl;
    }

    public Optional<StringFilter> optionalAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public StringFilter avatarUrl() {
        if (avatarUrl == null) {
            setAvatarUrl(new StringFilter());
        }
        return avatarUrl;
    }

    public void setAvatarUrl(StringFilter avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public IntegerFilter getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public Optional<IntegerFilter> optionalLoyaltyPoints() {
        return Optional.ofNullable(loyaltyPoints);
    }

    public IntegerFilter loyaltyPoints() {
        if (loyaltyPoints == null) {
            setLoyaltyPoints(new IntegerFilter());
        }
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(IntegerFilter loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public MembershipLevelFilter getMembershipLevel() {
        return membershipLevel;
    }

    public Optional<MembershipLevelFilter> optionalMembershipLevel() {
        return Optional.ofNullable(membershipLevel);
    }

    public MembershipLevelFilter membershipLevel() {
        if (membershipLevel == null) {
            setMembershipLevel(new MembershipLevelFilter());
        }
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevelFilter membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public BooleanFilter getNewsletterSubscribed() {
        return newsletterSubscribed;
    }

    public Optional<BooleanFilter> optionalNewsletterSubscribed() {
        return Optional.ofNullable(newsletterSubscribed);
    }

    public BooleanFilter newsletterSubscribed() {
        if (newsletterSubscribed == null) {
            setNewsletterSubscribed(new BooleanFilter());
        }
        return newsletterSubscribed;
    }

    public void setNewsletterSubscribed(BooleanFilter newsletterSubscribed) {
        this.newsletterSubscribed = newsletterSubscribed;
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

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public Optional<LongFilter> optionalAddressesId() {
        return Optional.ofNullable(addressesId);
    }

    public LongFilter addressesId() {
        if (addressesId == null) {
            setAddressesId(new LongFilter());
        }
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public Optional<LongFilter> optionalOrdersId() {
        return Optional.ofNullable(ordersId);
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            setOrdersId(new LongFilter());
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getNotificationsId() {
        return notificationsId;
    }

    public Optional<LongFilter> optionalNotificationsId() {
        return Optional.ofNullable(notificationsId);
    }

    public LongFilter notificationsId() {
        if (notificationsId == null) {
            setNotificationsId(new LongFilter());
        }
        return notificationsId;
    }

    public void setNotificationsId(LongFilter notificationsId) {
        this.notificationsId = notificationsId;
    }

    public LongFilter getWishlistId() {
        return wishlistId;
    }

    public Optional<LongFilter> optionalWishlistId() {
        return Optional.ofNullable(wishlistId);
    }

    public LongFilter wishlistId() {
        if (wishlistId == null) {
            setWishlistId(new LongFilter());
        }
        return wishlistId;
    }

    public void setWishlistId(LongFilter wishlistId) {
        this.wishlistId = wishlistId;
    }

    public LongFilter getCouponsId() {
        return couponsId;
    }

    public Optional<LongFilter> optionalCouponsId() {
        return Optional.ofNullable(couponsId);
    }

    public LongFilter couponsId() {
        if (couponsId == null) {
            setCouponsId(new LongFilter());
        }
        return couponsId;
    }

    public void setCouponsId(LongFilter couponsId) {
        this.couponsId = couponsId;
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
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(avatarUrl, that.avatarUrl) &&
            Objects.equals(loyaltyPoints, that.loyaltyPoints) &&
            Objects.equals(membershipLevel, that.membershipLevel) &&
            Objects.equals(newsletterSubscribed, that.newsletterSubscribed) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(notificationsId, that.notificationsId) &&
            Objects.equals(wishlistId, that.wishlistId) &&
            Objects.equals(couponsId, that.couponsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            phoneNumber,
            dateOfBirth,
            gender,
            avatarUrl,
            loyaltyPoints,
            membershipLevel,
            newsletterSubscribed,
            userId,
            addressesId,
            ordersId,
            notificationsId,
            wishlistId,
            couponsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalDateOfBirth().map(f -> "dateOfBirth=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalAvatarUrl().map(f -> "avatarUrl=" + f + ", ").orElse("") +
            optionalLoyaltyPoints().map(f -> "loyaltyPoints=" + f + ", ").orElse("") +
            optionalMembershipLevel().map(f -> "membershipLevel=" + f + ", ").orElse("") +
            optionalNewsletterSubscribed().map(f -> "newsletterSubscribed=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalAddressesId().map(f -> "addressesId=" + f + ", ").orElse("") +
            optionalOrdersId().map(f -> "ordersId=" + f + ", ").orElse("") +
            optionalNotificationsId().map(f -> "notificationsId=" + f + ", ").orElse("") +
            optionalWishlistId().map(f -> "wishlistId=" + f + ", ").orElse("") +
            optionalCouponsId().map(f -> "couponsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
