package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.OrderStatus;
import com.evaradrip.commerce.domain.enumeration.PaymentMethod;
import com.evaradrip.commerce.domain.enumeration.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Order entity
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "order_number", length = 50, nullable = false, unique = true)
    private String orderNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotalAmount;

    @DecimalMin(value = "0")
    @Column(name = "tax_amount", precision = 21, scale = 2)
    private BigDecimal taxAmount;

    @DecimalMin(value = "0")
    @Column(name = "shipping_amount", precision = 21, scale = 2)
    private BigDecimal shippingAmount;

    @DecimalMin(value = "0")
    @Column(name = "discount_amount", precision = 21, scale = 2)
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Size(max = 100)
    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Size(max = 255)
    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Size(max = 500)
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Size(max = 500)
    @Column(name = "return_reason", length = 500)
    private String returnReason;

    @DecimalMin(value = "0")
    @Column(name = "refund_amount", precision = 21, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "delivered_date")
    private ZonedDateTime deliveredDate;

    @Column(name = "shipped_date")
    private ZonedDateTime shippedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "variant", "order" }, allowSetters = true)
    private Set<OrderItem> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<Shipping> shippings = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private UserAddress shippingAddress;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private UserAddress billingAddress;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "addresses", "orders", "notifications", "wishlists", "coupons" }, allowSetters = true)
    private UserProfile user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public Order orderNumber(String orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Order totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSubtotalAmount() {
        return this.subtotalAmount;
    }

    public Order subtotalAmount(BigDecimal subtotalAmount) {
        this.setSubtotalAmount(subtotalAmount);
        return this;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public Order taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getShippingAmount() {
        return this.shippingAmount;
    }

    public Order shippingAmount(BigDecimal shippingAmount) {
        this.setShippingAmount(shippingAmount);
        return this;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Order discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public Order paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Order paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingMethod() {
        return this.shippingMethod;
    }

    public Order shippingMethod(String shippingMethod) {
        this.setShippingMethod(shippingMethod);
        return this;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public Order trackingNumber(String trackingNumber) {
        this.setTrackingNumber(trackingNumber);
        return this;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public Order notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public Order cancelReason(String cancelReason) {
        this.setCancelReason(cancelReason);
        return this;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getReturnReason() {
        return this.returnReason;
    }

    public Order returnReason(String returnReason) {
        this.setReturnReason(returnReason);
        return this;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public Order refundAmount(BigDecimal refundAmount) {
        this.setRefundAmount(refundAmount);
        return this;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return this.estimatedDeliveryDate;
    }

    public Order estimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return this;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public ZonedDateTime getDeliveredDate() {
        return this.deliveredDate;
    }

    public Order deliveredDate(ZonedDateTime deliveredDate) {
        this.setDeliveredDate(deliveredDate);
        return this;
    }

    public void setDeliveredDate(ZonedDateTime deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public ZonedDateTime getShippedDate() {
        return this.shippedDate;
    }

    public Order shippedDate(ZonedDateTime shippedDate) {
        this.setShippedDate(shippedDate);
        return this;
    }

    public void setShippedDate(ZonedDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Set<OrderItem> getItems() {
        return this.items;
    }

    public void setItems(Set<OrderItem> orderItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.items = orderItems;
    }

    public Order items(Set<OrderItem> orderItems) {
        this.setItems(orderItems);
        return this;
    }

    public Order addItems(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeItems(OrderItem orderItem) {
        this.items.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setOrder(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setOrder(this));
        }
        this.payments = payments;
    }

    public Order payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Order addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setOrder(this);
        return this;
    }

    public Order removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setOrder(null);
        return this;
    }

    public Set<Shipping> getShippings() {
        return this.shippings;
    }

    public void setShippings(Set<Shipping> shippings) {
        if (this.shippings != null) {
            this.shippings.forEach(i -> i.setOrder(null));
        }
        if (shippings != null) {
            shippings.forEach(i -> i.setOrder(this));
        }
        this.shippings = shippings;
    }

    public Order shippings(Set<Shipping> shippings) {
        this.setShippings(shippings);
        return this;
    }

    public Order addShipping(Shipping shipping) {
        this.shippings.add(shipping);
        shipping.setOrder(this);
        return this;
    }

    public Order removeShipping(Shipping shipping) {
        this.shippings.remove(shipping);
        shipping.setOrder(null);
        return this;
    }

    public UserAddress getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(UserAddress userAddress) {
        this.shippingAddress = userAddress;
    }

    public Order shippingAddress(UserAddress userAddress) {
        this.setShippingAddress(userAddress);
        return this;
    }

    public UserAddress getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(UserAddress userAddress) {
        this.billingAddress = userAddress;
    }

    public Order billingAddress(UserAddress userAddress) {
        this.setBillingAddress(userAddress);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public Order user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
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
            "}";
    }
}
