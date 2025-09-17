package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.OrderStatus;
import com.evaradrip.commerce.domain.enumeration.PaymentMethod;
import com.evaradrip.commerce.domain.enumeration.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Order} entity.
 */
@Schema(description = "Order entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String orderNumber;

    @NotNull
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal subtotalAmount;

    @DecimalMin(value = "0")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0")
    private BigDecimal shippingAmount;

    @DecimalMin(value = "0")
    private BigDecimal discountAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    @Size(max = 100)
    private String shippingMethod;

    @Size(max = 255)
    private String trackingNumber;

    @Lob
    private String notes;

    @Size(max = 500)
    private String cancelReason;

    @Size(max = 500)
    private String returnReason;

    @DecimalMin(value = "0")
    private BigDecimal refundAmount;

    private LocalDate estimatedDeliveryDate;

    private ZonedDateTime deliveredDate;

    private ZonedDateTime shippedDate;

    @NotNull
    private UserAddressDTO shippingAddress;

    @NotNull
    private UserAddressDTO billingAddress;

    @NotNull
    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public ZonedDateTime getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(ZonedDateTime deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public ZonedDateTime getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(ZonedDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    public UserAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(UserAddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public UserAddressDTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(UserAddressDTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", subtotalAmount=" + getSubtotalAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", shippingAmount=" + getShippingAmount() +
            ", discountAmount=" + getDiscountAmount() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", shippingMethod='" + getShippingMethod() + "'" +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", notes='" + getNotes() + "'" +
            ", cancelReason='" + getCancelReason() + "'" +
            ", returnReason='" + getReturnReason() + "'" +
            ", refundAmount=" + getRefundAmount() +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", deliveredDate='" + getDeliveredDate() + "'" +
            ", shippedDate='" + getShippedDate() + "'" +
            ", shippingAddress=" + getShippingAddress() +
            ", billingAddress=" + getBillingAddress() +
            ", user=" + getUser() +
            "}";
    }
}
