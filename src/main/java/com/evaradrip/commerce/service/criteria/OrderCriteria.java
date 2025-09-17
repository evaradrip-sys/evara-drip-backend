package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.OrderStatus;
import com.evaradrip.commerce.domain.enumeration.PaymentMethod;
import com.evaradrip.commerce.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Order} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {

        public PaymentMethodFilter() {}

        public PaymentMethodFilter(PaymentMethodFilter filter) {
            super(filter);
        }

        @Override
        public PaymentMethodFilter copy() {
            return new PaymentMethodFilter(this);
        }
    }

    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {

        public PaymentStatusFilter() {}

        public PaymentStatusFilter(PaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaymentStatusFilter copy() {
            return new PaymentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter orderNumber;

    private OrderStatusFilter status;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter subtotalAmount;

    private BigDecimalFilter taxAmount;

    private BigDecimalFilter shippingAmount;

    private BigDecimalFilter discountAmount;

    private PaymentMethodFilter paymentMethod;

    private PaymentStatusFilter paymentStatus;

    private StringFilter shippingMethod;

    private StringFilter trackingNumber;

    private StringFilter cancelReason;

    private StringFilter returnReason;

    private BigDecimalFilter refundAmount;

    private LocalDateFilter estimatedDeliveryDate;

    private ZonedDateTimeFilter deliveredDate;

    private ZonedDateTimeFilter shippedDate;

    private LongFilter itemsId;

    private LongFilter paymentsId;

    private LongFilter shippingId;

    private LongFilter shippingAddressId;

    private LongFilter billingAddressId;

    private LongFilter userId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderNumber = other.optionalOrderNumber().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OrderStatusFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.subtotalAmount = other.optionalSubtotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.taxAmount = other.optionalTaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.shippingAmount = other.optionalShippingAmount().map(BigDecimalFilter::copy).orElse(null);
        this.discountAmount = other.optionalDiscountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(PaymentMethodFilter::copy).orElse(null);
        this.paymentStatus = other.optionalPaymentStatus().map(PaymentStatusFilter::copy).orElse(null);
        this.shippingMethod = other.optionalShippingMethod().map(StringFilter::copy).orElse(null);
        this.trackingNumber = other.optionalTrackingNumber().map(StringFilter::copy).orElse(null);
        this.cancelReason = other.optionalCancelReason().map(StringFilter::copy).orElse(null);
        this.returnReason = other.optionalReturnReason().map(StringFilter::copy).orElse(null);
        this.refundAmount = other.optionalRefundAmount().map(BigDecimalFilter::copy).orElse(null);
        this.estimatedDeliveryDate = other.optionalEstimatedDeliveryDate().map(LocalDateFilter::copy).orElse(null);
        this.deliveredDate = other.optionalDeliveredDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.shippedDate = other.optionalShippedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.paymentsId = other.optionalPaymentsId().map(LongFilter::copy).orElse(null);
        this.shippingId = other.optionalShippingId().map(LongFilter::copy).orElse(null);
        this.shippingAddressId = other.optionalShippingAddressId().map(LongFilter::copy).orElse(null);
        this.billingAddressId = other.optionalBillingAddressId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
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

    public StringFilter getOrderNumber() {
        return orderNumber;
    }

    public Optional<StringFilter> optionalOrderNumber() {
        return Optional.ofNullable(orderNumber);
    }

    public StringFilter orderNumber() {
        if (orderNumber == null) {
            setOrderNumber(new StringFilter());
        }
        return orderNumber;
    }

    public void setOrderNumber(StringFilter orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public Optional<OrderStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OrderStatusFilter status() {
        if (status == null) {
            setStatus(new OrderStatusFilter());
        }
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimalFilter getSubtotalAmount() {
        return subtotalAmount;
    }

    public Optional<BigDecimalFilter> optionalSubtotalAmount() {
        return Optional.ofNullable(subtotalAmount);
    }

    public BigDecimalFilter subtotalAmount() {
        if (subtotalAmount == null) {
            setSubtotalAmount(new BigDecimalFilter());
        }
        return subtotalAmount;
    }

    public void setSubtotalAmount(BigDecimalFilter subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public Optional<BigDecimalFilter> optionalTaxAmount() {
        return Optional.ofNullable(taxAmount);
    }

    public BigDecimalFilter taxAmount() {
        if (taxAmount == null) {
            setTaxAmount(new BigDecimalFilter());
        }
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimalFilter getShippingAmount() {
        return shippingAmount;
    }

    public Optional<BigDecimalFilter> optionalShippingAmount() {
        return Optional.ofNullable(shippingAmount);
    }

    public BigDecimalFilter shippingAmount() {
        if (shippingAmount == null) {
            setShippingAmount(new BigDecimalFilter());
        }
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimalFilter shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public Optional<BigDecimalFilter> optionalDiscountAmount() {
        return Optional.ofNullable(discountAmount);
    }

    public BigDecimalFilter discountAmount() {
        if (discountAmount == null) {
            setDiscountAmount(new BigDecimalFilter());
        }
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public PaymentMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<PaymentMethodFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public PaymentMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new PaymentMethodFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public Optional<PaymentStatusFilter> optionalPaymentStatus() {
        return Optional.ofNullable(paymentStatus);
    }

    public PaymentStatusFilter paymentStatus() {
        if (paymentStatus == null) {
            setPaymentStatus(new PaymentStatusFilter());
        }
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getShippingMethod() {
        return shippingMethod;
    }

    public Optional<StringFilter> optionalShippingMethod() {
        return Optional.ofNullable(shippingMethod);
    }

    public StringFilter shippingMethod() {
        if (shippingMethod == null) {
            setShippingMethod(new StringFilter());
        }
        return shippingMethod;
    }

    public void setShippingMethod(StringFilter shippingMethod) {
        this.shippingMethod = shippingMethod;
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

    public StringFilter getCancelReason() {
        return cancelReason;
    }

    public Optional<StringFilter> optionalCancelReason() {
        return Optional.ofNullable(cancelReason);
    }

    public StringFilter cancelReason() {
        if (cancelReason == null) {
            setCancelReason(new StringFilter());
        }
        return cancelReason;
    }

    public void setCancelReason(StringFilter cancelReason) {
        this.cancelReason = cancelReason;
    }

    public StringFilter getReturnReason() {
        return returnReason;
    }

    public Optional<StringFilter> optionalReturnReason() {
        return Optional.ofNullable(returnReason);
    }

    public StringFilter returnReason() {
        if (returnReason == null) {
            setReturnReason(new StringFilter());
        }
        return returnReason;
    }

    public void setReturnReason(StringFilter returnReason) {
        this.returnReason = returnReason;
    }

    public BigDecimalFilter getRefundAmount() {
        return refundAmount;
    }

    public Optional<BigDecimalFilter> optionalRefundAmount() {
        return Optional.ofNullable(refundAmount);
    }

    public BigDecimalFilter refundAmount() {
        if (refundAmount == null) {
            setRefundAmount(new BigDecimalFilter());
        }
        return refundAmount;
    }

    public void setRefundAmount(BigDecimalFilter refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateFilter getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public Optional<LocalDateFilter> optionalEstimatedDeliveryDate() {
        return Optional.ofNullable(estimatedDeliveryDate);
    }

    public LocalDateFilter estimatedDeliveryDate() {
        if (estimatedDeliveryDate == null) {
            setEstimatedDeliveryDate(new LocalDateFilter());
        }
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDateFilter estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public ZonedDateTimeFilter getDeliveredDate() {
        return deliveredDate;
    }

    public Optional<ZonedDateTimeFilter> optionalDeliveredDate() {
        return Optional.ofNullable(deliveredDate);
    }

    public ZonedDateTimeFilter deliveredDate() {
        if (deliveredDate == null) {
            setDeliveredDate(new ZonedDateTimeFilter());
        }
        return deliveredDate;
    }

    public void setDeliveredDate(ZonedDateTimeFilter deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public ZonedDateTimeFilter getShippedDate() {
        return shippedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalShippedDate() {
        return Optional.ofNullable(shippedDate);
    }

    public ZonedDateTimeFilter shippedDate() {
        if (shippedDate == null) {
            setShippedDate(new ZonedDateTimeFilter());
        }
        return shippedDate;
    }

    public void setShippedDate(ZonedDateTimeFilter shippedDate) {
        this.shippedDate = shippedDate;
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

    public LongFilter getPaymentsId() {
        return paymentsId;
    }

    public Optional<LongFilter> optionalPaymentsId() {
        return Optional.ofNullable(paymentsId);
    }

    public LongFilter paymentsId() {
        if (paymentsId == null) {
            setPaymentsId(new LongFilter());
        }
        return paymentsId;
    }

    public void setPaymentsId(LongFilter paymentsId) {
        this.paymentsId = paymentsId;
    }

    public LongFilter getShippingId() {
        return shippingId;
    }

    public Optional<LongFilter> optionalShippingId() {
        return Optional.ofNullable(shippingId);
    }

    public LongFilter shippingId() {
        if (shippingId == null) {
            setShippingId(new LongFilter());
        }
        return shippingId;
    }

    public void setShippingId(LongFilter shippingId) {
        this.shippingId = shippingId;
    }

    public LongFilter getShippingAddressId() {
        return shippingAddressId;
    }

    public Optional<LongFilter> optionalShippingAddressId() {
        return Optional.ofNullable(shippingAddressId);
    }

    public LongFilter shippingAddressId() {
        if (shippingAddressId == null) {
            setShippingAddressId(new LongFilter());
        }
        return shippingAddressId;
    }

    public void setShippingAddressId(LongFilter shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public LongFilter getBillingAddressId() {
        return billingAddressId;
    }

    public Optional<LongFilter> optionalBillingAddressId() {
        return Optional.ofNullable(billingAddressId);
    }

    public LongFilter billingAddressId() {
        if (billingAddressId == null) {
            setBillingAddressId(new LongFilter());
        }
        return billingAddressId;
    }

    public void setBillingAddressId(LongFilter billingAddressId) {
        this.billingAddressId = billingAddressId;
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
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(subtotalAmount, that.subtotalAmount) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(shippingAmount, that.shippingAmount) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(shippingMethod, that.shippingMethod) &&
            Objects.equals(trackingNumber, that.trackingNumber) &&
            Objects.equals(cancelReason, that.cancelReason) &&
            Objects.equals(returnReason, that.returnReason) &&
            Objects.equals(refundAmount, that.refundAmount) &&
            Objects.equals(estimatedDeliveryDate, that.estimatedDeliveryDate) &&
            Objects.equals(deliveredDate, that.deliveredDate) &&
            Objects.equals(shippedDate, that.shippedDate) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(paymentsId, that.paymentsId) &&
            Objects.equals(shippingId, that.shippingId) &&
            Objects.equals(shippingAddressId, that.shippingAddressId) &&
            Objects.equals(billingAddressId, that.billingAddressId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            orderNumber,
            status,
            totalAmount,
            subtotalAmount,
            taxAmount,
            shippingAmount,
            discountAmount,
            paymentMethod,
            paymentStatus,
            shippingMethod,
            trackingNumber,
            cancelReason,
            returnReason,
            refundAmount,
            estimatedDeliveryDate,
            deliveredDate,
            shippedDate,
            itemsId,
            paymentsId,
            shippingId,
            shippingAddressId,
            billingAddressId,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderNumber().map(f -> "orderNumber=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalSubtotalAmount().map(f -> "subtotalAmount=" + f + ", ").orElse("") +
            optionalTaxAmount().map(f -> "taxAmount=" + f + ", ").orElse("") +
            optionalShippingAmount().map(f -> "shippingAmount=" + f + ", ").orElse("") +
            optionalDiscountAmount().map(f -> "discountAmount=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalPaymentStatus().map(f -> "paymentStatus=" + f + ", ").orElse("") +
            optionalShippingMethod().map(f -> "shippingMethod=" + f + ", ").orElse("") +
            optionalTrackingNumber().map(f -> "trackingNumber=" + f + ", ").orElse("") +
            optionalCancelReason().map(f -> "cancelReason=" + f + ", ").orElse("") +
            optionalReturnReason().map(f -> "returnReason=" + f + ", ").orElse("") +
            optionalRefundAmount().map(f -> "refundAmount=" + f + ", ").orElse("") +
            optionalEstimatedDeliveryDate().map(f -> "estimatedDeliveryDate=" + f + ", ").orElse("") +
            optionalDeliveredDate().map(f -> "deliveredDate=" + f + ", ").orElse("") +
            optionalShippedDate().map(f -> "shippedDate=" + f + ", ").orElse("") +
            optionalItemsId().map(f -> "itemsId=" + f + ", ").orElse("") +
            optionalPaymentsId().map(f -> "paymentsId=" + f + ", ").orElse("") +
            optionalShippingId().map(f -> "shippingId=" + f + ", ").orElse("") +
            optionalShippingAddressId().map(f -> "shippingAddressId=" + f + ", ").orElse("") +
            optionalBillingAddressId().map(f -> "billingAddressId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
