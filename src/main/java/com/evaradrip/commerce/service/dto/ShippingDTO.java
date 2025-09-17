package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.ShippingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Shipping} entity.
 */
@Schema(description = "Shipping entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShippingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String carrier;

    @Size(max = 255)
    private String trackingNumber;

    private LocalDate estimatedDelivery;

    private ZonedDateTime actualDelivery;

    @DecimalMin(value = "0")
    private BigDecimal shippingCost;

    private ShippingStatus status;

    @Lob
    private String notes;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDate getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(LocalDate estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public ZonedDateTime getActualDelivery() {
        return actualDelivery;
    }

    public void setActualDelivery(ZonedDateTime actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public ShippingStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingDTO)) {
            return false;
        }

        ShippingDTO shippingDTO = (ShippingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shippingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingDTO{" +
            "id=" + getId() +
            ", carrier='" + getCarrier() + "'" +
            ", trackingNumber='" + getTrackingNumber() + "'" +
            ", estimatedDelivery='" + getEstimatedDelivery() + "'" +
            ", actualDelivery='" + getActualDelivery() + "'" +
            ", shippingCost=" + getShippingCost() +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
