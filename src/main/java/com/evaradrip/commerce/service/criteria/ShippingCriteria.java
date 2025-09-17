package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.ShippingStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Shipping} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.ShippingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shippings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShippingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ShippingStatus
     */
    public static class ShippingStatusFilter extends Filter<ShippingStatus> {

        public ShippingStatusFilter() {}

        public ShippingStatusFilter(ShippingStatusFilter filter) {
            super(filter);
        }

        @Override
        public ShippingStatusFilter copy() {
            return new ShippingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter carrier;

    private StringFilter trackingNumber;

    private LocalDateFilter estimatedDelivery;

    private ZonedDateTimeFilter actualDelivery;

    private BigDecimalFilter shippingCost;

    private ShippingStatusFilter status;

    private LongFilter orderId;

    private Boolean distinct;

    public ShippingCriteria() {}

    public ShippingCriteria(ShippingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.carrier = other.optionalCarrier().map(StringFilter::copy).orElse(null);
        this.trackingNumber = other.optionalTrackingNumber().map(StringFilter::copy).orElse(null);
        this.estimatedDelivery = other.optionalEstimatedDelivery().map(LocalDateFilter::copy).orElse(null);
        this.actualDelivery = other.optionalActualDelivery().map(ZonedDateTimeFilter::copy).orElse(null);
        this.shippingCost = other.optionalShippingCost().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ShippingStatusFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShippingCriteria copy() {
        return new ShippingCriteria(this);
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

    public StringFilter getCarrier() {
        return carrier;
    }

    public Optional<StringFilter> optionalCarrier() {
        return Optional.ofNullable(carrier);
    }

    public StringFilter carrier() {
        if (carrier == null) {
            setCarrier(new StringFilter());
        }
        return carrier;
    }

    public void setCarrier(StringFilter carrier) {
        this.carrier = carrier;
    }

    public StringFilter getTrackingNumber() {
        return trackingNumber;
    }

    public Optional<StringFilter> optionalTrackingNumber() {
        return Optional.ofNullable(trackingNumber);
    }

    public StringFilter trackingNumber() {
        if (trackingNumber == null) {
            setTrackingNumber(new StringFilter());
        }
        return trackingNumber;
    }

    public void setTrackingNumber(StringFilter trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateFilter getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public Optional<LocalDateFilter> optionalEstimatedDelivery() {
        return Optional.ofNullable(estimatedDelivery);
    }

    public LocalDateFilter estimatedDelivery() {
        if (estimatedDelivery == null) {
            setEstimatedDelivery(new LocalDateFilter());
        }
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(LocalDateFilter estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public ZonedDateTimeFilter getActualDelivery() {
        return actualDelivery;
    }

    public Optional<ZonedDateTimeFilter> optionalActualDelivery() {
        return Optional.ofNullable(actualDelivery);
    }

    public ZonedDateTimeFilter actualDelivery() {
        if (actualDelivery == null) {
            setActualDelivery(new ZonedDateTimeFilter());
        }
        return actualDelivery;
    }

    public void setActualDelivery(ZonedDateTimeFilter actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public BigDecimalFilter getShippingCost() {
        return shippingCost;
    }

    public Optional<BigDecimalFilter> optionalShippingCost() {
        return Optional.ofNullable(shippingCost);
    }

    public BigDecimalFilter shippingCost() {
        if (shippingCost == null) {
            setShippingCost(new BigDecimalFilter());
        }
        return shippingCost;
    }

    public void setShippingCost(BigDecimalFilter shippingCost) {
        this.shippingCost = shippingCost;
    }

    public ShippingStatusFilter getStatus() {
        return status;
    }

    public Optional<ShippingStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ShippingStatusFilter status() {
        if (status == null) {
            setStatus(new ShippingStatusFilter());
        }
        return status;
    }

    public void setStatus(ShippingStatusFilter status) {
        this.status = status;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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
        final ShippingCriteria that = (ShippingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(carrier, that.carrier) &&
            Objects.equals(trackingNumber, that.trackingNumber) &&
            Objects.equals(estimatedDelivery, that.estimatedDelivery) &&
            Objects.equals(actualDelivery, that.actualDelivery) &&
            Objects.equals(shippingCost, that.shippingCost) &&
            Objects.equals(status, that.status) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carrier, trackingNumber, estimatedDelivery, actualDelivery, shippingCost, status, orderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCarrier().map(f -> "carrier=" + f + ", ").orElse("") +
            optionalTrackingNumber().map(f -> "trackingNumber=" + f + ", ").orElse("") +
            optionalEstimatedDelivery().map(f -> "estimatedDelivery=" + f + ", ").orElse("") +
            optionalActualDelivery().map(f -> "actualDelivery=" + f + ", ").orElse("") +
            optionalShippingCost().map(f -> "shippingCost=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
