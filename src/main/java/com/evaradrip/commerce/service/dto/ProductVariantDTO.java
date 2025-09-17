package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.ClothingSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.ProductVariant} entity.
 */
@Schema(description = "Product Variants (Size, Color combinations)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariantDTO implements Serializable {

    private Long id;

    @NotNull
    private ClothingSize variantSize;

    @NotNull
    @Size(max = 50)
    private String color;

    @NotNull
    @Size(max = 100)
    private String sku;

    @NotNull
    @Min(value = 0)
    private Integer stockCount;

    private BigDecimal priceAdjustment;

    @Size(max = 100)
    private String barcode;

    @DecimalMin(value = "0")
    private BigDecimal weight;

    @NotNull
    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClothingSize getVariantSize() {
        return variantSize;
    }

    public void setVariantSize(ClothingSize variantSize) {
        this.variantSize = variantSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public BigDecimal getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(BigDecimal priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
        if (!(o instanceof ProductVariantDTO)) {
            return false;
        }

        ProductVariantDTO productVariantDTO = (ProductVariantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productVariantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantDTO{" +
            "id=" + getId() +
            ", variantSize='" + getVariantSize() + "'" +
            ", color='" + getColor() + "'" +
            ", sku='" + getSku() + "'" +
            ", stockCount=" + getStockCount() +
            ", priceAdjustment=" + getPriceAdjustment() +
            ", barcode='" + getBarcode() + "'" +
            ", weight=" + getWeight() +
            ", product=" + getProduct() +
            "}";
    }
}
