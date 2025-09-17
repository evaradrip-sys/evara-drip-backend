package com.evaradrip.commerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Inventory tracking entity
 */
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Min(value = 0)
    @Column(name = "reserved_quantity")
    private Integer reservedQuantity;

    @Size(max = 100)
    @Column(name = "warehouse", length = 100)
    private String warehouse;

    @Column(name = "last_restocked")
    private ZonedDateTime lastRestocked;

    @Min(value = 0)
    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Min(value = 0)
    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "images",
            "variants",
            "reviews",
            "inventories",
            "promotions",
            "brand",
            "category",
            "wishlisteds",
            "applicablePromotions",
            "featuredInCategories",
        },
        allowSetters = true
    )
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Inventory quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReservedQuantity() {
        return this.reservedQuantity;
    }

    public Inventory reservedQuantity(Integer reservedQuantity) {
        this.setReservedQuantity(reservedQuantity);
        return this;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getWarehouse() {
        return this.warehouse;
    }

    public Inventory warehouse(String warehouse) {
        this.setWarehouse(warehouse);
        return this;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public ZonedDateTime getLastRestocked() {
        return this.lastRestocked;
    }

    public Inventory lastRestocked(ZonedDateTime lastRestocked) {
        this.setLastRestocked(lastRestocked);
        return this;
    }

    public void setLastRestocked(ZonedDateTime lastRestocked) {
        this.lastRestocked = lastRestocked;
    }

    public Integer getReorderLevel() {
        return this.reorderLevel;
    }

    public Inventory reorderLevel(Integer reorderLevel) {
        this.setReorderLevel(reorderLevel);
        return this;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getReorderQuantity() {
        return this.reorderQuantity;
    }

    public Inventory reorderQuantity(Integer reorderQuantity) {
        this.setReorderQuantity(reorderQuantity);
        return this;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return getId() != null && getId().equals(((Inventory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", reservedQuantity=" + getReservedQuantity() +
            ", warehouse='" + getWarehouse() + "'" +
            ", lastRestocked='" + getLastRestocked() + "'" +
            ", reorderLevel=" + getReorderLevel() +
            ", reorderQuantity=" + getReorderQuantity() +
            "}";
    }
}
