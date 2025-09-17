package com.evaradrip.commerce.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.CartItem} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.CartItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cart-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private BigDecimalFilter addedPrice;

    private LongFilter productId;

    private LongFilter variantId;

    private LongFilter cartId;

    private Boolean distinct;

    public CartItemCriteria() {}

    public CartItemCriteria(CartItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.addedPrice = other.optionalAddedPrice().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.variantId = other.optionalVariantId().map(LongFilter::copy).orElse(null);
        this.cartId = other.optionalCartId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CartItemCriteria copy() {
        return new CartItemCriteria(this);
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

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getAddedPrice() {
        return addedPrice;
    }

    public Optional<BigDecimalFilter> optionalAddedPrice() {
        return Optional.ofNullable(addedPrice);
    }

    public BigDecimalFilter addedPrice() {
        if (addedPrice == null) {
            setAddedPrice(new BigDecimalFilter());
        }
        return addedPrice;
    }

    public void setAddedPrice(BigDecimalFilter addedPrice) {
        this.addedPrice = addedPrice;
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

    public LongFilter getVariantId() {
        return variantId;
    }

    public Optional<LongFilter> optionalVariantId() {
        return Optional.ofNullable(variantId);
    }

    public LongFilter variantId() {
        if (variantId == null) {
            setVariantId(new LongFilter());
        }
        return variantId;
    }

    public void setVariantId(LongFilter variantId) {
        this.variantId = variantId;
    }

    public LongFilter getCartId() {
        return cartId;
    }

    public Optional<LongFilter> optionalCartId() {
        return Optional.ofNullable(cartId);
    }

    public LongFilter cartId() {
        if (cartId == null) {
            setCartId(new LongFilter());
        }
        return cartId;
    }

    public void setCartId(LongFilter cartId) {
        this.cartId = cartId;
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
        final CartItemCriteria that = (CartItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(addedPrice, that.addedPrice) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(variantId, that.variantId) &&
            Objects.equals(cartId, that.cartId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, addedPrice, productId, variantId, cartId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalAddedPrice().map(f -> "addedPrice=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalVariantId().map(f -> "variantId=" + f + ", ").orElse("") +
            optionalCartId().map(f -> "cartId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
