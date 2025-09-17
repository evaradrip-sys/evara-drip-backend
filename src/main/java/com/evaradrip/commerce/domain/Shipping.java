package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.ShippingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Shipping entity
 */
@Entity
@Table(name = "shipping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "carrier", length = 100, nullable = false)
    private String carrier;

    @Size(max = 255)
    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "actual_delivery")
    private ZonedDateTime actualDelivery;

    @DecimalMin(value = "0")
    @Column(name = "shipping_cost", precision = 21, scale = 2)
    private BigDecimal shippingCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShippingStatus status;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "items", "payments", "shippings", "shippingAddress", "billingAddress", "user" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shipping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrier() {
        return this.carrier;
    }

    public Shipping carrier(String carrier) {
        this.setCarrier(carrier);
        return this;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public Shipping trackingNumber(String trackingNumber) {
        this.setTrackingNumber(trackingNumber);
        return this;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDate getEstimatedDelivery() {
        return this.estimatedDelivery;
    }

    public Shipping estimatedDelivery(LocalDate estimatedDelivery) {
        this.setEstimatedDelivery(estimatedDelivery);
        return this;
    }

    public void setEstimatedDelivery(LocalDate estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public ZonedDateTime getActualDelivery() {
        return this.actualDelivery;
    }

    public Shipping actualDelivery(ZonedDateTime actualDelivery) {
        this.setActualDelivery(actualDelivery);
        return this;
    }

    public void setActualDelivery(ZonedDateTime actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public BigDecimal getShippingCost() {
        return this.shippingCost;
    }

    public Shipping shippingCost(BigDecimal shippingCost) {
        this.setShippingCost(shippingCost);
        return this;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public ShippingStatus getStatus() {
        return this.status;
    }

    public Shipping status(ShippingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public Shipping notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shipping order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipping)) {
            return false;
        }
        return getId() != null && getId().equals(((Shipping) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shipping{" +
            "id=" + getId() +
            ", carrier='" + getCarrier() + "'" +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", estimatedDelivery='" + getEstimatedDelivery() + "'" +
            ", actualDelivery='" + getActualDelivery() + "'" +
            ", shippingCost=" + getShippingCost() +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
