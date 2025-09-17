package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Product} entity.
 */
@Schema(description = "Main Product entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @DecimalMin(value = "0")
    private BigDecimal originalPrice;

    @Size(max = 100)
    private String sku;

    private Boolean isNew;

    private Boolean isOnSale;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private BigDecimal rating;

    @Min(value = 0)
    private Integer reviewsCount;

    @NotNull
    @Min(value = 0)
    private Integer stockCount;

    private Boolean inStock;

    @Lob
    private String features;

    @Size(max = 255)
    private String metaTitle;

    @Size(max = 500)
    private String metaDescription;

    @Size(max = 255)
    private String metaKeywords;

    private ProductStatus status;

    @DecimalMin(value = "0")
    private BigDecimal weight;

    @Size(max = 100)
    private String dimensions;

    private Set<PromotionDTO> promotions = new HashSet<>();

    private BrandDTO brand;

    @NotNull
    private CategoryDTO category;

    private Set<UserProfileDTO> wishlisteds = new HashSet<>();

    private Set<PromotionDTO> applicablePromotions = new HashSet<>();

    private Set<CategoryDTO> featuredInCategories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Set<PromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionDTO> promotions) {
        this.promotions = promotions;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Set<UserProfileDTO> getWishlisteds() {
        return wishlisteds;
    }

    public void setWishlisteds(Set<UserProfileDTO> wishlisteds) {
        this.wishlisteds = wishlisteds;
    }

    public Set<PromotionDTO> getApplicablePromotions() {
        return applicablePromotions;
    }

    public void setApplicablePromotions(Set<PromotionDTO> applicablePromotions) {
        this.applicablePromotions = applicablePromotions;
    }

    public Set<CategoryDTO> getFeaturedInCategories() {
        return featuredInCategories;
    }

    public void setFeaturedInCategories(Set<CategoryDTO> featuredInCategories) {
        this.featuredInCategories = featuredInCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", originalPrice=" + getOriginalPrice() +
            ", sku='" + getSku() + "'" +
            ", isNew='" + getIsNew() + "'" +
            ", isOnSale='" + getIsOnSale() + "'" +
            ", rating=" + getRating() +
            ", reviewsCount=" + getReviewsCount() +
            ", stockCount=" + getStockCount() +
            ", inStock='" + getInStock() + "'" +
            ", features='" + getFeatures() + "'" +
            ", metaTitle='" + getMetaTitle() + "'" +
            ", metaDescription='" + getMetaDescription() + "'" +
            ", metaKeywords='" + getMetaKeywords() + "'" +
            ", status='" + getStatus() + "'" +
            ", weight=" + getWeight() +
            ", dimensions='" + getDimensions() + "'" +
            ", promotions=" + getPromotions() +
            ", brand=" + getBrand() +
            ", category=" + getCategory() +
            ", wishlisteds=" + getWishlisteds() +
            ", applicablePromotions=" + getApplicablePromotions() +
            ", featuredInCategories=" + getFeaturedInCategories() +
            "}";
    }
}
