package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.ClothingSize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product Variants (Size, Color combinations)
 */
@Entity
@Table(name = "product_variant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "variant_size", nullable = false)
    private ClothingSize variantSize;

    @NotNull
    @Size(max = 50)
    @Column(name = "color", length = 50, nullable = false)
    private String color;

    @NotNull
    @Size(max = 100)
    @Column(name = "sku", length = 100, nullable = false, unique = true)
    private String sku;

    @NotNull
    @Min(value = 0)
    @Column(name = "stock_count", nullable = false)
    private Integer stockCount;

    @Column(name = "price_adjustment", precision = 21, scale = 2)
    private BigDecimal priceAdjustment;

    @Size(max = 100)
    @Column(name = "barcode", length = 100)
    private String barcode;

    @DecimalMin(value = "0")
    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

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

    public ProductVariant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClothingSize getVariantSize() {
        return this.variantSize;
    }

    public ProductVariant variantSize(ClothingSize variantSize) {
        this.setVariantSize(variantSize);
        return this;
    }

    public void setVariantSize(ClothingSize variantSize) {
        this.variantSize = variantSize;
    }

    public String getColor() {
        return this.color;
    }

    public ProductVariant color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSku() {
        return this.sku;
    }

    public ProductVariant sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockCount() {
        return this.stockCount;
    }

    public ProductVariant stockCount(Integer stockCount) {
        this.setStockCount(stockCount);
        return this;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public BigDecimal getPriceAdjustment() {
        return this.priceAdjustment;
    }

    public ProductVariant priceAdjustment(BigDecimal priceAdjustment) {
        this.setPriceAdjustment(priceAdjustment);
        return this;
    }

    public void setPriceAdjustment(BigDecimal priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public ProductVariant barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public ProductVariant weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariant)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductVariant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariant{" +
            "id=" + getId() +
            ", variantSize='" + getVariantSize() + "'" +
            ", color='" + getColor() + "'" +
            ", sku='" + getSku() + "'" +
            ", stockCount=" + getStockCount() +
            ", priceAdjustment=" + getPriceAdjustment() +
            ", barcode='" + getBarcode() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}
