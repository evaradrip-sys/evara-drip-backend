package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.DiscountType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Coupon} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.CouponResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /coupons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DiscountType
     */
    public static class DiscountTypeFilter extends Filter<DiscountType> {

        public DiscountTypeFilter() {}

        public DiscountTypeFilter(DiscountTypeFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeFilter copy() {
            return new DiscountTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private DiscountTypeFilter discountType;

    private BigDecimalFilter discountValue;

    private ZonedDateTimeFilter validFrom;

    private ZonedDateTimeFilter validUntil;

    private IntegerFilter maxUses;

    private IntegerFilter currentUses;

    private BigDecimalFilter minOrderValue;

    private BooleanFilter isActive;

    private LongFilter usersId;

    private Boolean distinct;

    public CouponCriteria() {}

    public CouponCriteria(CouponCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(DiscountTypeFilter::copy).orElse(null);
        this.discountValue = other.optionalDiscountValue().map(BigDecimalFilter::copy).orElse(null);
        this.validFrom = other.optionalValidFrom().map(ZonedDateTimeFilter::copy).orElse(null);
        this.validUntil = other.optionalValidUntil().map(ZonedDateTimeFilter::copy).orElse(null);
        this.maxUses = other.optionalMaxUses().map(IntegerFilter::copy).orElse(null);
        this.currentUses = other.optionalCurrentUses().map(IntegerFilter::copy).orElse(null);
        this.minOrderValue = other.optionalMinOrderValue().map(BigDecimalFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.usersId = other.optionalUsersId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CouponCriteria copy() {
        return new CouponCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public DiscountTypeFilter getDiscountType() {
        return discountType;
    }

    public Optional<DiscountTypeFilter> optionalDiscountType() {
        return Optional.ofNullable(discountType);
    }

    public DiscountTypeFilter discountType() {
        if (discountType == null) {
            setDiscountType(new DiscountTypeFilter());
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountValue() {
        return discountValue;
    }

    public Optional<BigDecimalFilter> optionalDiscountValue() {
        return Optional.ofNullable(discountValue);
    }

    public BigDecimalFilter discountValue() {
        if (discountValue == null) {
            setDiscountValue(new BigDecimalFilter());
        }
        return discountValue;
    }

    public void setDiscountValue(BigDecimalFilter discountValue) {
        this.discountValue = discountValue;
    }

    public ZonedDateTimeFilter getValidFrom() {
        return validFrom;
    }

    public Optional<ZonedDateTimeFilter> optionalValidFrom() {
        return Optional.ofNullable(validFrom);
    }

    public ZonedDateTimeFilter validFrom() {
        if (validFrom == null) {
            setValidFrom(new ZonedDateTimeFilter());
        }
        return validFrom;
    }

    public void setValidFrom(ZonedDateTimeFilter validFrom) {
        this.validFrom = validFrom;
    }

    public ZonedDateTimeFilter getValidUntil() {
        return validUntil;
    }

    public Optional<ZonedDateTimeFilter> optionalValidUntil() {
        return Optional.ofNullable(validUntil);
    }

    public ZonedDateTimeFilter validUntil() {
        if (validUntil == null) {
            setValidUntil(new ZonedDateTimeFilter());
        }
        return validUntil;
    }

    public void setValidUntil(ZonedDateTimeFilter validUntil) {
        this.validUntil = validUntil;
    }

    public IntegerFilter getMaxUses() {
        return maxUses;
    }

    public Optional<IntegerFilter> optionalMaxUses() {
        return Optional.ofNullable(maxUses);
    }

    public IntegerFilter maxUses() {
        if (maxUses == null) {
            setMaxUses(new IntegerFilter());
        }
        return maxUses;
    }

    public void setMaxUses(IntegerFilter maxUses) {
        this.maxUses = maxUses;
    }

    public IntegerFilter getCurrentUses() {
        return currentUses;
    }

    public Optional<IntegerFilter> optionalCurrentUses() {
        return Optional.ofNullable(currentUses);
    }

    public IntegerFilter currentUses() {
        if (currentUses == null) {
            setCurrentUses(new IntegerFilter());
        }
        return currentUses;
    }

    public void setCurrentUses(IntegerFilter currentUses) {
        this.currentUses = currentUses;
    }

    public BigDecimalFilter getMinOrderValue() {
        return minOrderValue;
    }

    public Optional<BigDecimalFilter> optionalMinOrderValue() {
        return Optional.ofNullable(minOrderValue);
    }

    public BigDecimalFilter minOrderValue() {
        if (minOrderValue == null) {
            setMinOrderValue(new BigDecimalFilter());
        }
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimalFilter minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public Optional<LongFilter> optionalUsersId() {
        return Optional.ofNullable(usersId);
    }

    public LongFilter usersId() {
        if (usersId == null) {
            setUsersId(new LongFilter());
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
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
        final CouponCriteria that = (CouponCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountValue, that.discountValue) &&
            Objects.equals(validFrom, that.validFrom) &&
            Objects.equals(validUntil, that.validUntil) &&
            Objects.equals(maxUses, that.maxUses) &&
            Objects.equals(currentUses, that.currentUses) &&
            Objects.equals(minOrderValue, that.minOrderValue) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            discountType,
            discountValue,
            validFrom,
            validUntil,
            maxUses,
            currentUses,
            minOrderValue,
            isActive,
            usersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountValue().map(f -> "discountValue=" + f + ", ").orElse("") +
            optionalValidFrom().map(f -> "validFrom=" + f + ", ").orElse("") +
            optionalValidUntil().map(f -> "validUntil=" + f + ", ").orElse("") +
            optionalMaxUses().map(f -> "maxUses=" + f + ", ").orElse("") +
            optionalCurrentUses().map(f -> "currentUses=" + f + ", ").orElse("") +
            optionalMinOrderValue().map(f -> "minOrderValue=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalUsersId().map(f -> "usersId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
