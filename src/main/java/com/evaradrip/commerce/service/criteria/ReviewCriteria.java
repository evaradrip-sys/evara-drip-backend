package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.ReviewStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Review} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.ReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReviewStatus
     */
    public static class ReviewStatusFilter extends Filter<ReviewStatus> {

        public ReviewStatusFilter() {}

        public ReviewStatusFilter(ReviewStatusFilter filter) {
            super(filter);
        }

        @Override
        public ReviewStatusFilter copy() {
            return new ReviewStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rating;

    private StringFilter title;

    private IntegerFilter helpfulCount;

    private IntegerFilter notHelpfulCount;

    private BooleanFilter verifiedPurchase;

    private ReviewStatusFilter status;

    private ZonedDateTimeFilter responseDate;

    private LongFilter userId;

    private LongFilter productId;

    private Boolean distinct;

    public ReviewCriteria() {}

    public ReviewCriteria(ReviewCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.rating = other.optionalRating().map(IntegerFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.helpfulCount = other.optionalHelpfulCount().map(IntegerFilter::copy).orElse(null);
        this.notHelpfulCount = other.optionalNotHelpfulCount().map(IntegerFilter::copy).orElse(null);
        this.verifiedPurchase = other.optionalVerifiedPurchase().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ReviewStatusFilter::copy).orElse(null);
        this.responseDate = other.optionalResponseDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReviewCriteria copy() {
        return new ReviewCriteria(this);
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

    public IntegerFilter getRating() {
        return rating;
    }

    public Optional<IntegerFilter> optionalRating() {
        return Optional.ofNullable(rating);
    }

    public IntegerFilter rating() {
        if (rating == null) {
            setRating(new IntegerFilter());
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getHelpfulCount() {
        return helpfulCount;
    }

    public Optional<IntegerFilter> optionalHelpfulCount() {
        return Optional.ofNullable(helpfulCount);
    }

    public IntegerFilter helpfulCount() {
        if (helpfulCount == null) {
            setHelpfulCount(new IntegerFilter());
        }
        return helpfulCount;
    }

    public void setHelpfulCount(IntegerFilter helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public IntegerFilter getNotHelpfulCount() {
        return notHelpfulCount;
    }

    public Optional<IntegerFilter> optionalNotHelpfulCount() {
        return Optional.ofNullable(notHelpfulCount);
    }

    public IntegerFilter notHelpfulCount() {
        if (notHelpfulCount == null) {
            setNotHelpfulCount(new IntegerFilter());
        }
        return notHelpfulCount;
    }

    public void setNotHelpfulCount(IntegerFilter notHelpfulCount) {
        this.notHelpfulCount = notHelpfulCount;
    }

    public BooleanFilter getVerifiedPurchase() {
        return verifiedPurchase;
    }

    public Optional<BooleanFilter> optionalVerifiedPurchase() {
        return Optional.ofNullable(verifiedPurchase);
    }

    public BooleanFilter verifiedPurchase() {
        if (verifiedPurchase == null) {
            setVerifiedPurchase(new BooleanFilter());
        }
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(BooleanFilter verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public ReviewStatusFilter getStatus() {
        return status;
    }

    public Optional<ReviewStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ReviewStatusFilter status() {
        if (status == null) {
            setStatus(new ReviewStatusFilter());
        }
        return status;
    }

    public void setStatus(ReviewStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getResponseDate() {
        return responseDate;
    }

    public Optional<ZonedDateTimeFilter> optionalResponseDate() {
        return Optional.ofNullable(responseDate);
    }

    public ZonedDateTimeFilter responseDate() {
        if (responseDate == null) {
            setResponseDate(new ZonedDateTimeFilter());
        }
        return responseDate;
    }

    public void setResponseDate(ZonedDateTimeFilter responseDate) {
        this.responseDate = responseDate;
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

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final ReviewCriteria that = (ReviewCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(title, that.title) &&
            Objects.equals(helpfulCount, that.helpfulCount) &&
            Objects.equals(notHelpfulCount, that.notHelpfulCount) &&
            Objects.equals(verifiedPurchase, that.verifiedPurchase) &&
            Objects.equals(status, that.status) &&
            Objects.equals(responseDate, that.responseDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            rating,
            title,
            helpfulCount,
            notHelpfulCount,
            verifiedPurchase,
            status,
            responseDate,
            userId,
            productId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRating().map(f -> "rating=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalHelpfulCount().map(f -> "helpfulCount=" + f + ", ").orElse("") +
            optionalNotHelpfulCount().map(f -> "notHelpfulCount=" + f + ", ").orElse("") +
            optionalVerifiedPurchase().map(f -> "verifiedPurchase=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalResponseDate().map(f -> "responseDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
