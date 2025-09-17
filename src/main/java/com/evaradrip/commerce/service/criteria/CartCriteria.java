package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.CartStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Cart} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.CartResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CartStatus
     */
    public static class CartStatusFilter extends Filter<CartStatus> {

        public CartStatusFilter() {}

        public CartStatusFilter(CartStatusFilter filter) {
            super(filter);
        }

        @Override
        public CartStatusFilter copy() {
            return new CartStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sessionId;

    private CartStatusFilter status;

    private InstantFilter expiresAt;

    private LongFilter userId;

    private LongFilter itemsId;

    private Boolean distinct;

    public CartCriteria() {}

    public CartCriteria(CartCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sessionId = other.optionalSessionId().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CartStatusFilter::copy).orElse(null);
        this.expiresAt = other.optionalExpiresAt().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CartCriteria copy() {
        return new CartCriteria(this);
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

    public StringFilter getSessionId() {
        return sessionId;
    }

    public Optional<StringFilter> optionalSessionId() {
        return Optional.ofNullable(sessionId);
    }

    public StringFilter sessionId() {
        if (sessionId == null) {
            setSessionId(new StringFilter());
        }
        return sessionId;
    }

    public void setSessionId(StringFilter sessionId) {
        this.sessionId = sessionId;
    }

    public CartStatusFilter getStatus() {
        return status;
    }

    public Optional<CartStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public CartStatusFilter status() {
        if (status == null) {
            setStatus(new CartStatusFilter());
        }
        return status;
    }

    public void setStatus(CartStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getExpiresAt() {
        return expiresAt;
    }

    public Optional<InstantFilter> optionalExpiresAt() {
        return Optional.ofNullable(expiresAt);
    }

    public InstantFilter expiresAt() {
        if (expiresAt == null) {
            setExpiresAt(new InstantFilter());
        }
        return expiresAt;
    }

    public void setExpiresAt(InstantFilter expiresAt) {
        this.expiresAt = expiresAt;
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

    public LongFilter getItemsId() {
        return itemsId;
    }

    public Optional<LongFilter> optionalItemsId() {
        return Optional.ofNullable(itemsId);
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            setItemsId(new LongFilter());
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
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
        final CartCriteria that = (CartCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sessionId, that.sessionId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(expiresAt, that.expiresAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, status, expiresAt, userId, itemsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSessionId().map(f -> "sessionId=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalExpiresAt().map(f -> "expiresAt=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalItemsId().map(f -> "itemsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
