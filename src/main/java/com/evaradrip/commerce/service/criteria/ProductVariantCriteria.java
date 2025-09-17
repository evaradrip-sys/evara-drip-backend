package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.ClothingSize;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.ProductVariant} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.ProductVariantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-variants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariantCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ClothingSize
     */
    public static class ClothingSizeFilter extends Filter<ClothingSize> {

        public ClothingSizeFilter() {}

        public ClothingSizeFilter(ClothingSizeFilter filter) {
            super(filter);
        }

        @Override
        public ClothingSizeFilter copy() {
            return new ClothingSizeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ClothingSizeFilter variantSize;

    private StringFilter color;

    private StringFilter sku;

    private IntegerFilter stockCount;

    private BigDecimalFilter priceAdjustment;

    private StringFilter barcode;

    private BigDecimalFilter weight;

    private LongFilter productId;

    private Boolean distinct;

    public ProductVariantCriteria() {}

    public ProductVariantCriteria(ProductVariantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.variantSize = other.optionalVariantSize().map(ClothingSizeFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.stockCount = other.optionalStockCount().map(IntegerFilter::copy).orElse(null);
        this.priceAdjustment = other.optionalPriceAdjustment().map(BigDecimalFilter::copy).orElse(null);
        this.barcode = other.optionalBarcode().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductVariantCriteria copy() {
        return new ProductVariantCriteria(this);
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

    public ClothingSizeFilter getVariantSize() {
        return variantSize;
    }

    public Optional<ClothingSizeFilter> optionalVariantSize() {
        return Optional.ofNullable(variantSize);
    }

    public ClothingSizeFilter variantSize() {
        if (variantSize == null) {
            setVariantSize(new ClothingSizeFilter());
        }
        return variantSize;
    }

    public void setVariantSize(ClothingSizeFilter variantSize) {
        this.variantSize = variantSize;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getSku() {
        return sku;
    }

    public Optional<StringFilter> optionalSku() {
        return Optional.ofNullable(sku);
    }

    public StringFilter sku() {
        if (sku == null) {
            setSku(new StringFilter());
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public IntegerFilter getStockCount() {
        return stockCount;
    }

    public Optional<IntegerFilter> optionalStockCount() {
        return Optional.ofNullable(stockCount);
    }

    public IntegerFilter stockCount() {
        if (stockCount == null) {
            setStockCount(new IntegerFilter());
        }
        return stockCount;
    }

    public void setStockCount(IntegerFilter stockCount) {
        this.stockCount = stockCount;
    }

    public BigDecimalFilter getPriceAdjustment() {
        return priceAdjustment;
    }

    public Optional<BigDecimalFilter> optionalPriceAdjustment() {
        return Optional.ofNullable(priceAdjustment);
    }

    public BigDecimalFilter priceAdjustment() {
        if (priceAdjustment == null) {
            setPriceAdjustment(new BigDecimalFilter());
        }
        return priceAdjustment;
    }

    public void setPriceAdjustment(BigDecimalFilter priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public Optional<StringFilter> optionalBarcode() {
        return Optional.ofNullable(barcode);
    }

    public StringFilter barcode() {
        if (barcode == null) {
            setBarcode(new StringFilter());
        }
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public Optional<BigDecimalFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public BigDecimalFilter weight() {
        if (weight == null) {
            setWeight(new BigDecimalFilter());
        }
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final ProductVariantCriteria that = (ProductVariantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(variantSize, that.variantSize) &&
            Objects.equals(color, that.color) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(stockCount, that.stockCount) &&
            Objects.equals(priceAdjustment, that.priceAdjustment) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, variantSize, color, sku, stockCount, priceAdjustment, barcode, weight, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVariantSize().map(f -> "variantSize=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalStockCount().map(f -> "stockCount=" + f + ", ").orElse("") +
            optionalPriceAdjustment().map(f -> "priceAdjustment=" + f + ", ").orElse("") +
            optionalBarcode().map(f -> "barcode=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
