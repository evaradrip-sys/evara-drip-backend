package com.evaradrip.commerce.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Inventory} entity.
 */
@Schema(description = "Inventory tracking entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    @Min(value = 0)
    private Integer reservedQuantity;

    @Size(max = 100)
    private String warehouse;

    private ZonedDateTime lastRestocked;

    @Min(value = 0)
    private Integer reorderLevel;

    @Min(value = 0)
    private Integer reorderQuantity;

    @NotNull
    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public ZonedDateTime getLastRestocked() {
        return lastRestocked;
    }

    public void setLastRestocked(ZonedDateTime lastRestocked) {
        this.lastRestocked = lastRestocked;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryDTO)) {
            return false;
        }

        InventoryDTO inventoryDTO = (InventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", reservedQuantity=" + getReservedQuantity() +
            ", warehouse='" + getWarehouse() + "'" +
            ", lastRestocked='" + getLastRestocked() + "'" +
            ", reorderLevel=" + getReorderLevel() +
            ", reorderQuantity=" + getReorderQuantity() +
            ", product=" + getProduct() +
            "}";
    }
}
