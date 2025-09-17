package com.evaradrip.commerce.service.criteria;

import com.evaradrip.commerce.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Product} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProductStatus
     */
    public static class ProductStatusFilter extends Filter<ProductStatus> {

        public ProductStatusFilter() {}

        public ProductStatusFilter(ProductStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProductStatusFilter copy() {
            return new ProductStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter price;

    private BigDecimalFilter originalPrice;

    private StringFilter sku;

    private BooleanFilter isNew;

    private BooleanFilter isOnSale;

    private BigDecimalFilter rating;

    private IntegerFilter reviewsCount;

    private IntegerFilter stockCount;

    private BooleanFilter inStock;

    private StringFilter metaTitle;

    private StringFilter metaDescription;

    private StringFilter metaKeywords;

    private ProductStatusFilter status;

    private BigDecimalFilter weight;

    private StringFilter dimensions;

    private LongFilter imagesId;

    private LongFilter variantsId;

    private LongFilter reviewsId;

    private LongFilter inventoryId;

    private LongFilter promotionsId;

    private LongFilter brandId;

    private LongFilter categoryId;

    private LongFilter wishlistedId;

    private LongFilter applicablePromotionsId;

    private LongFilter featuredInCategoriesId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.originalPrice = other.optionalOriginalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.isNew = other.optionalIsNew().map(BooleanFilter::copy).orElse(null);
        this.isOnSale = other.optionalIsOnSale().map(BooleanFilter::copy).orElse(null);
        this.rating = other.optionalRating().map(BigDecimalFilter::copy).orElse(null);
        this.reviewsCount = other.optionalReviewsCount().map(IntegerFilter::copy).orElse(null);
        this.stockCount = other.optionalStockCount().map(IntegerFilter::copy).orElse(null);
        this.inStock = other.optionalInStock().map(BooleanFilter::copy).orElse(null);
        this.metaTitle = other.optionalMetaTitle().map(StringFilter::copy).orElse(null);
        this.metaDescription = other.optionalMetaDescription().map(StringFilter::copy).orElse(null);
        this.metaKeywords = other.optionalMetaKeywords().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ProductStatusFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(BigDecimalFilter::copy).orElse(null);
        this.dimensions = other.optionalDimensions().map(StringFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.variantsId = other.optionalVariantsId().map(LongFilter::copy).orElse(null);
        this.reviewsId = other.optionalReviewsId().map(LongFilter::copy).orElse(null);
        this.inventoryId = other.optionalInventoryId().map(LongFilter::copy).orElse(null);
        this.promotionsId = other.optionalPromotionsId().map(LongFilter::copy).orElse(null);
        this.brandId = other.optionalBrandId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.wishlistedId = other.optionalWishlistedId().map(LongFilter::copy).orElse(null);
        this.applicablePromotionsId = other.optionalApplicablePromotionsId().map(LongFilter::copy).orElse(null);
        this.featuredInCategoriesId = other.optionalFeaturedInCategoriesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getOriginalPrice() {
        return originalPrice;
    }

    public Optional<BigDecimalFilter> optionalOriginalPrice() {
        return Optional.ofNullable(originalPrice);
    }

    public BigDecimalFilter originalPrice() {
        if (originalPrice == null) {
            setOriginalPrice(new BigDecimalFilter());
        }
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimalFilter originalPrice) {
        this.originalPrice = originalPrice;
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

    public BooleanFilter getIsNew() {
        return isNew;
    }

    public Optional<BooleanFilter> optionalIsNew() {
        return Optional.ofNullable(isNew);
    }

    public BooleanFilter isNew() {
        if (isNew == null) {
            setIsNew(new BooleanFilter());
        }
        return isNew;
    }

    public void setIsNew(BooleanFilter isNew) {
        this.isNew = isNew;
    }

    public BooleanFilter getIsOnSale() {
        return isOnSale;
    }

    public Optional<BooleanFilter> optionalIsOnSale() {
        return Optional.ofNullable(isOnSale);
    }

    public BooleanFilter isOnSale() {
        if (isOnSale == null) {
            setIsOnSale(new BooleanFilter());
        }
        return isOnSale;
    }

    public void setIsOnSale(BooleanFilter isOnSale) {
        this.isOnSale = isOnSale;
    }

    public BigDecimalFilter getRating() {
        return rating;
    }

    public Optional<BigDecimalFilter> optionalRating() {
        return Optional.ofNullable(rating);
    }

    public BigDecimalFilter rating() {
        if (rating == null) {
            setRating(new BigDecimalFilter());
        }
        return rating;
    }

    public void setRating(BigDecimalFilter rating) {
        this.rating = rating;
    }

    public IntegerFilter getReviewsCount() {
        return reviewsCount;
    }

    public Optional<IntegerFilter> optionalReviewsCount() {
        return Optional.ofNullable(reviewsCount);
    }

    public IntegerFilter reviewsCount() {
        if (reviewsCount == null) {
            setReviewsCount(new IntegerFilter());
        }
        return reviewsCount;
    }

    public void setReviewsCount(IntegerFilter reviewsCount) {
        this.reviewsCount = reviewsCount;
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

    public BooleanFilter getInStock() {
        return inStock;
    }

    public Optional<BooleanFilter> optionalInStock() {
        return Optional.ofNullable(inStock);
    }

    public BooleanFilter inStock() {
        if (inStock == null) {
            setInStock(new BooleanFilter());
        }
        return inStock;
    }

    public void setInStock(BooleanFilter inStock) {
        this.inStock = inStock;
    }

    public StringFilter getMetaTitle() {
        return metaTitle;
    }

    public Optional<StringFilter> optionalMetaTitle() {
        return Optional.ofNullable(metaTitle);
    }

    public StringFilter metaTitle() {
        if (metaTitle == null) {
            setMetaTitle(new StringFilter());
        }
        return metaTitle;
    }

    public void setMetaTitle(StringFilter metaTitle) {
        this.metaTitle = metaTitle;
    }

    public StringFilter getMetaDescription() {
        return metaDescription;
    }

    public Optional<StringFilter> optionalMetaDescription() {
        return Optional.ofNullable(metaDescription);
    }

    public StringFilter metaDescription() {
        if (metaDescription == null) {
            setMetaDescription(new StringFilter());
        }
        return metaDescription;
    }

    public void setMetaDescription(StringFilter metaDescription) {
        this.metaDescription = metaDescription;
    }

    public StringFilter getMetaKeywords() {
        return metaKeywords;
    }

    public Optional<StringFilter> optionalMetaKeywords() {
        return Optional.ofNullable(metaKeywords);
    }

    public StringFilter metaKeywords() {
        if (metaKeywords == null) {
            setMetaKeywords(new StringFilter());
        }
        return metaKeywords;
    }

    public void setMetaKeywords(StringFilter metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public ProductStatusFilter getStatus() {
        return status;
    }

    public Optional<ProductStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ProductStatusFilter status() {
        if (status == null) {
            setStatus(new ProductStatusFilter());
        }
        return status;
    }

    public void setStatus(ProductStatusFilter status) {
        this.status = status;
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

    public StringFilter getDimensions() {
        return dimensions;
    }

    public Optional<StringFilter> optionalDimensions() {
        return Optional.ofNullable(dimensions);
    }

    public StringFilter dimensions() {
        if (dimensions == null) {
            setDimensions(new StringFilter());
        }
        return dimensions;
    }

    public void setDimensions(StringFilter dimensions) {
        this.dimensions = dimensions;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public Optional<LongFilter> optionalImagesId() {
        return Optional.ofNullable(imagesId);
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            setImagesId(new LongFilter());
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getVariantsId() {
        return variantsId;
    }

    public Optional<LongFilter> optionalVariantsId() {
        return Optional.ofNullable(variantsId);
    }

    public LongFilter variantsId() {
        if (variantsId == null) {
            setVariantsId(new LongFilter());
        }
        return variantsId;
    }

    public void setVariantsId(LongFilter variantsId) {
        this.variantsId = variantsId;
    }

    public LongFilter getReviewsId() {
        return reviewsId;
    }

    public Optional<LongFilter> optionalReviewsId() {
        return Optional.ofNullable(reviewsId);
    }

    public LongFilter reviewsId() {
        if (reviewsId == null) {
            setReviewsId(new LongFilter());
        }
        return reviewsId;
    }

    public void setReviewsId(LongFilter reviewsId) {
        this.reviewsId = reviewsId;
    }

    public LongFilter getInventoryId() {
        return inventoryId;
    }

    public Optional<LongFilter> optionalInventoryId() {
        return Optional.ofNullable(inventoryId);
    }

    public LongFilter inventoryId() {
        if (inventoryId == null) {
            setInventoryId(new LongFilter());
        }
        return inventoryId;
    }

    public void setInventoryId(LongFilter inventoryId) {
        this.inventoryId = inventoryId;
    }

    public LongFilter getPromotionsId() {
        return promotionsId;
    }

    public Optional<LongFilter> optionalPromotionsId() {
        return Optional.ofNullable(promotionsId);
    }

    public LongFilter promotionsId() {
        if (promotionsId == null) {
            setPromotionsId(new LongFilter());
        }
        return promotionsId;
    }

    public void setPromotionsId(LongFilter promotionsId) {
        this.promotionsId = promotionsId;
    }

    public LongFilter getBrandId() {
        return brandId;
    }

    public Optional<LongFilter> optionalBrandId() {
        return Optional.ofNullable(brandId);
    }

    public LongFilter brandId() {
        if (brandId == null) {
            setBrandId(new LongFilter());
        }
        return brandId;
    }

    public void setBrandId(LongFilter brandId) {
        this.brandId = brandId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getWishlistedId() {
        return wishlistedId;
    }

    public Optional<LongFilter> optionalWishlistedId() {
        return Optional.ofNullable(wishlistedId);
    }

    public LongFilter wishlistedId() {
        if (wishlistedId == null) {
            setWishlistedId(new LongFilter());
        }
        return wishlistedId;
    }

    public void setWishlistedId(LongFilter wishlistedId) {
        this.wishlistedId = wishlistedId;
    }

    public LongFilter getApplicablePromotionsId() {
        return applicablePromotionsId;
    }

    public Optional<LongFilter> optionalApplicablePromotionsId() {
        return Optional.ofNullable(applicablePromotionsId);
    }

    public LongFilter applicablePromotionsId() {
        if (applicablePromotionsId == null) {
            setApplicablePromotionsId(new LongFilter());
        }
        return applicablePromotionsId;
    }

    public void setApplicablePromotionsId(LongFilter applicablePromotionsId) {
        this.applicablePromotionsId = applicablePromotionsId;
    }

    public LongFilter getFeaturedInCategoriesId() {
        return featuredInCategoriesId;
    }

    public Optional<LongFilter> optionalFeaturedInCategoriesId() {
        return Optional.ofNullable(featuredInCategoriesId);
    }

    public LongFilter featuredInCategoriesId() {
        if (featuredInCategoriesId == null) {
            setFeaturedInCategoriesId(new LongFilter());
        }
        return featuredInCategoriesId;
    }

    public void setFeaturedInCategoriesId(LongFilter featuredInCategoriesId) {
        this.featuredInCategoriesId = featuredInCategoriesId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(originalPrice, that.originalPrice) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(isNew, that.isNew) &&
            Objects.equals(isOnSale, that.isOnSale) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(reviewsCount, that.reviewsCount) &&
            Objects.equals(stockCount, that.stockCount) &&
            Objects.equals(inStock, that.inStock) &&
            Objects.equals(metaTitle, that.metaTitle) &&
            Objects.equals(metaDescription, that.metaDescription) &&
            Objects.equals(metaKeywords, that.metaKeywords) &&
            Objects.equals(status, that.status) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(dimensions, that.dimensions) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(variantsId, that.variantsId) &&
            Objects.equals(reviewsId, that.reviewsId) &&
            Objects.equals(inventoryId, that.inventoryId) &&
            Objects.equals(promotionsId, that.promotionsId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(wishlistedId, that.wishlistedId) &&
            Objects.equals(applicablePromotionsId, that.applicablePromotionsId) &&
            Objects.equals(featuredInCategoriesId, that.featuredInCategoriesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            price,
            originalPrice,
            sku,
            isNew,
            isOnSale,
            rating,
            reviewsCount,
            stockCount,
            inStock,
            metaTitle,
            metaDescription,
            metaKeywords,
            status,
            weight,
            dimensions,
            imagesId,
            variantsId,
            reviewsId,
            inventoryId,
            promotionsId,
            brandId,
            categoryId,
            wishlistedId,
            applicablePromotionsId,
            featuredInCategoriesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalOriginalPrice().map(f -> "originalPrice=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalIsNew().map(f -> "isNew=" + f + ", ").orElse("") +
            optionalIsOnSale().map(f -> "isOnSale=" + f + ", ").orElse("") +
            optionalRating().map(f -> "rating=" + f + ", ").orElse("") +
            optionalReviewsCount().map(f -> "reviewsCount=" + f + ", ").orElse("") +
            optionalStockCount().map(f -> "stockCount=" + f + ", ").orElse("") +
            optionalInStock().map(f -> "inStock=" + f + ", ").orElse("") +
            optionalMetaTitle().map(f -> "metaTitle=" + f + ", ").orElse("") +
            optionalMetaDescription().map(f -> "metaDescription=" + f + ", ").orElse("") +
            optionalMetaKeywords().map(f -> "metaKeywords=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalDimensions().map(f -> "dimensions=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalVariantsId().map(f -> "variantsId=" + f + ", ").orElse("") +
            optionalReviewsId().map(f -> "reviewsId=" + f + ", ").orElse("") +
            optionalInventoryId().map(f -> "inventoryId=" + f + ", ").orElse("") +
            optionalPromotionsId().map(f -> "promotionsId=" + f + ", ").orElse("") +
            optionalBrandId().map(f -> "brandId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalWishlistedId().map(f -> "wishlistedId=" + f + ", ").orElse("") +
            optionalApplicablePromotionsId().map(f -> "applicablePromotionsId=" + f + ", ").orElse("") +
            optionalFeaturedInCategoriesId().map(f -> "featuredInCategoriesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
