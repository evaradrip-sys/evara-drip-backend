package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.DiscountType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Promotion} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.PromotionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /promotions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionCriteria implements Serializable, Criteria {

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

    private StringFilter name;

    private StringFilter promoCode;

    private DiscountTypeFilter discountType;

    private BigDecimalFilter discountValue;

    private BigDecimalFilter minPurchaseAmount;

    private BigDecimalFilter maxDiscountAmount;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private IntegerFilter usageLimit;

    private IntegerFilter usageCount;

    private BooleanFilter isActive;

    private LongFilter applicableProductsId;

    private LongFilter productsId;

    private Boolean distinct;

    public PromotionCriteria() {}

    public PromotionCriteria(PromotionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.promoCode = other.optionalPromoCode().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(DiscountTypeFilter::copy).orElse(null);
        this.discountValue = other.optionalDiscountValue().map(BigDecimalFilter::copy).orElse(null);
        this.minPurchaseAmount = other.optionalMinPurchaseAmount().map(BigDecimalFilter::copy).orElse(null);
        this.maxDiscountAmount = other.optionalMaxDiscountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.usageLimit = other.optionalUsageLimit().map(IntegerFilter::copy).orElse(null);
        this.usageCount = other.optionalUsageCount().map(IntegerFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.applicableProductsId = other.optionalApplicableProductsId().map(LongFilter::copy).orElse(null);
        this.productsId = other.optionalProductsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PromotionCriteria copy() {
        return new PromotionCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPromoCode() {
        return promoCode;
    }

    public Optional<StringFilter> optionalPromoCode() {
        return Optional.ofNullable(promoCode);
    }

    public StringFilter promoCode() {
        if (promoCode == null) {
            setPromoCode(new StringFilter());
        }
        return promoCode;
    }

    public void setPromoCode(StringFilter promoCode) {
        this.promoCode = promoCode;
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

    public BigDecimalFilter getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public Optional<BigDecimalFilter> optionalMinPurchaseAmount() {
        return Optional.ofNullable(minPurchaseAmount);
    }

    public BigDecimalFilter minPurchaseAmount() {
        if (minPurchaseAmount == null) {
            setMinPurchaseAmount(new BigDecimalFilter());
        }
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimalFilter minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public BigDecimalFilter getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public Optional<BigDecimalFilter> optionalMaxDiscountAmount() {
        return Optional.ofNullable(maxDiscountAmount);
    }

    public BigDecimalFilter maxDiscountAmount() {
        if (maxDiscountAmount == null) {
            setMaxDiscountAmount(new BigDecimalFilter());
        }
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimalFilter maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public Optional<ZonedDateTimeFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            setStartDate(new ZonedDateTimeFilter());
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public Optional<ZonedDateTimeFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            setEndDate(new ZonedDateTimeFilter());
        }
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public IntegerFilter getUsageLimit() {
        return usageLimit;
    }

    public Optional<IntegerFilter> optionalUsageLimit() {
        return Optional.ofNullable(usageLimit);
    }

    public IntegerFilter usageLimit() {
        if (usageLimit == null) {
            setUsageLimit(new IntegerFilter());
        }
        return usageLimit;
    }

    public void setUsageLimit(IntegerFilter usageLimit) {
        this.usageLimit = usageLimit;
    }

    public IntegerFilter getUsageCount() {
        return usageCount;
    }

    public Optional<IntegerFilter> optionalUsageCount() {
        return Optional.ofNullable(usageCount);
    }

    public IntegerFilter usageCount() {
        if (usageCount == null) {
            setUsageCount(new IntegerFilter());
        }
        return usageCount;
    }

    public void setUsageCount(IntegerFilter usageCount) {
        this.usageCount = usageCount;
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

    public LongFilter getApplicableProductsId() {
        return applicableProductsId;
    }

    public Optional<LongFilter> optionalApplicableProductsId() {
        return Optional.ofNullable(applicableProductsId);
    }

    public LongFilter applicableProductsId() {
        if (applicableProductsId == null) {
            setApplicableProductsId(new LongFilter());
        }
        return applicableProductsId;
    }

    public void setApplicableProductsId(LongFilter applicableProductsId) {
        this.applicableProductsId = applicableProductsId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public Optional<LongFilter> optionalProductsId() {
        return Optional.ofNullable(productsId);
    }

    public LongFilter productsId() {
        if (productsId == null) {
            setProductsId(new LongFilter());
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
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
        final PromotionCriteria that = (PromotionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(promoCode, that.promoCode) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountValue, that.discountValue) &&
            Objects.equals(minPurchaseAmount, that.minPurchaseAmount) &&
            Objects.equals(maxDiscountAmount, that.maxDiscountAmount) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(usageLimit, that.usageLimit) &&
            Objects.equals(usageCount, that.usageCount) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(applicableProductsId, that.applicableProductsId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            promoCode,
            discountType,
            discountValue,
            minPurchaseAmount,
            maxDiscountAmount,
            startDate,
            endDate,
            usageLimit,
            usageCount,
            isActive,
            applicableProductsId,
            productsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPromoCode().map(f -> "promoCode=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountValue().map(f -> "discountValue=" + f + ", ").orElse("") +
            optionalMinPurchaseAmount().map(f -> "minPurchaseAmount=" + f + ", ").orElse("") +
            optionalMaxDiscountAmount().map(f -> "maxDiscountAmount=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalUsageLimit().map(f -> "usageLimit=" + f + ", ").orElse("") +
            optionalUsageCount().map(f -> "usageCount=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalApplicableProductsId().map(f -> "applicableProductsId=" + f + ", ").orElse("") +
            optionalProductsId().map(f -> "productsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
