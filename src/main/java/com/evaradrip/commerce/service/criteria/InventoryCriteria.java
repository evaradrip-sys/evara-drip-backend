package com.evaradrip.commerce.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Inventory} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.InventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private IntegerFilter reservedQuantity;

    private StringFilter warehouse;

    private ZonedDateTimeFilter lastRestocked;

    private IntegerFilter reorderLevel;

    private IntegerFilter reorderQuantity;

    private LongFilter productId;

    private Boolean distinct;

    public InventoryCriteria() {}

    public InventoryCriteria(InventoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.reservedQuantity = other.optionalReservedQuantity().map(IntegerFilter::copy).orElse(null);
        this.warehouse = other.optionalWarehouse().map(StringFilter::copy).orElse(null);
        this.lastRestocked = other.optionalLastRestocked().map(ZonedDateTimeFilter::copy).orElse(null);
        this.reorderLevel = other.optionalReorderLevel().map(IntegerFilter::copy).orElse(null);
        this.reorderQuantity = other.optionalReorderQuantity().map(IntegerFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryCriteria copy() {
        return new InventoryCriteria(this);
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

    public IntegerFilter getReservedQuantity() {
        return reservedQuantity;
    }

    public Optional<IntegerFilter> optionalReservedQuantity() {
        return Optional.ofNullable(reservedQuantity);
    }

    public IntegerFilter reservedQuantity() {
        if (reservedQuantity == null) {
            setReservedQuantity(new IntegerFilter());
        }
        return reservedQuantity;
    }

    public void setReservedQuantity(IntegerFilter reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public StringFilter getWarehouse() {
        return warehouse;
    }

    public Optional<StringFilter> optionalWarehouse() {
        return Optional.ofNullable(warehouse);
    }

    public StringFilter warehouse() {
        if (warehouse == null) {
            setWarehouse(new StringFilter());
        }
        return warehouse;
    }

    public void setWarehouse(StringFilter warehouse) {
        this.warehouse = warehouse;
    }

    public ZonedDateTimeFilter getLastRestocked() {
        return lastRestocked;
    }

    public Optional<ZonedDateTimeFilter> optionalLastRestocked() {
        return Optional.ofNullable(lastRestocked);
    }

    public ZonedDateTimeFilter lastRestocked() {
        if (lastRestocked == null) {
            setLastRestocked(new ZonedDateTimeFilter());
        }
        return lastRestocked;
    }

    public void setLastRestocked(ZonedDateTimeFilter lastRestocked) {
        this.lastRestocked = lastRestocked;
    }

    public IntegerFilter getReorderLevel() {
        return reorderLevel;
    }

    public Optional<IntegerFilter> optionalReorderLevel() {
        return Optional.ofNullable(reorderLevel);
    }

    public IntegerFilter reorderLevel() {
        if (reorderLevel == null) {
            setReorderLevel(new IntegerFilter());
        }
        return reorderLevel;
    }

    public void setReorderLevel(IntegerFilter reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public IntegerFilter getReorderQuantity() {
        return reorderQuantity;
    }

    public Optional<IntegerFilter> optionalReorderQuantity() {
        return Optional.ofNullable(reorderQuantity);
    }

    public IntegerFilter reorderQuantity() {
        if (reorderQuantity == null) {
            setReorderQuantity(new IntegerFilter());
        }
        return reorderQuantity;
    }

    public void setReorderQuantity(IntegerFilter reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
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
        final InventoryCriteria that = (InventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(reservedQuantity, that.reservedQuantity) &&
            Objects.equals(warehouse, that.warehouse) &&
            Objects.equals(lastRestocked, that.lastRestocked) &&
            Objects.equals(reorderLevel, that.reorderLevel) &&
            Objects.equals(reorderQuantity, that.reorderQuantity) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, reservedQuantity, warehouse, lastRestocked, reorderLevel, reorderQuantity, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalReservedQuantity().map(f -> "reservedQuantity=" + f + ", ").orElse("") +
            optionalWarehouse().map(f -> "warehouse=" + f + ", ").orElse("") +
            optionalLastRestocked().map(f -> "lastRestocked=" + f + ", ").orElse("") +
            optionalReorderLevel().map(f -> "reorderLevel=" + f + ", ").orElse("") +
            optionalReorderQuantity().map(f -> "reorderQuantity=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
