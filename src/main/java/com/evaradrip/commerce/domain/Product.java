package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Main Product entity
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "0")
    @Column(name = "original_price", precision = 21, scale = 2)
    private BigDecimal originalPrice;

    @Size(max = 100)
    @Column(name = "sku", length = 100, unique = true)
    private String sku;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "is_on_sale")
    private Boolean isOnSale;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "rating", precision = 21, scale = 2)
    private BigDecimal rating;

    @Min(value = 0)
    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @NotNull
    @Min(value = 0)
    @Column(name = "stock_count", nullable = false)
    private Integer stockCount;

    @Column(name = "in_stock")
    private Boolean inStock;

    @Lob
    @Column(name = "features")
    private String features;

    @Size(max = 255)
    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Size(max = 500)
    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Size(max = 255)
    @Column(name = "meta_keywords", length = 255)
    private String metaKeywords;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @DecimalMin(value = "0")
    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Size(max = 100)
    @Column(name = "dimensions", length = 100)
    private String dimensions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductVariant> variants = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Inventory> inventories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_product__promotions",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "promotions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicableProducts", "products" }, allowSetters = true)
    private Set<Promotion> promotions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Brand brand;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "subcategories", "featuredProducts", "parent" }, allowSetters = true)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "wishlists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "addresses", "orders", "notifications", "wishlists", "coupons" }, allowSetters = true)
    private Set<UserProfile> wishlisteds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "applicableProducts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicableProducts", "products" }, allowSetters = true)
    private Set<Promotion> applicablePromotions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "featuredProducts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "subcategories", "featuredProducts", "parent" }, allowSetters = true)
    private Set<Category> featuredInCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return this.originalPrice;
    }

    public Product originalPrice(BigDecimal originalPrice) {
        this.setOriginalPrice(originalPrice);
        return this;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Boolean getIsNew() {
        return this.isNew;
    }

    public Product isNew(Boolean isNew) {
        this.setIsNew(isNew);
        return this;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsOnSale() {
        return this.isOnSale;
    }

    public Product isOnSale(Boolean isOnSale) {
        this.setIsOnSale(isOnSale);
        return this;
    }

    public void setIsOnSale(Boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public BigDecimal getRating() {
        return this.rating;
    }

    public Product rating(BigDecimal rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewsCount() {
        return this.reviewsCount;
    }

    public Product reviewsCount(Integer reviewsCount) {
        this.setReviewsCount(reviewsCount);
        return this;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getStockCount() {
        return this.stockCount;
    }

    public Product stockCount(Integer stockCount) {
        this.setStockCount(stockCount);
        return this;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Boolean getInStock() {
        return this.inStock;
    }

    public Product inStock(Boolean inStock) {
        this.setInStock(inStock);
        return this;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getFeatures() {
        return this.features;
    }

    public Product features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getMetaTitle() {
        return this.metaTitle;
    }

    public Product metaTitle(String metaTitle) {
        this.setMetaTitle(metaTitle);
        return this;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return this.metaDescription;
    }

    public Product metaDescription(String metaDescription) {
        this.setMetaDescription(metaDescription);
        return this;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return this.metaKeywords;
    }

    public Product metaKeywords(String metaKeywords) {
        this.setMetaKeywords(metaKeywords);
        return this;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public Product weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return this.dimensions;
    }

    public Product dimensions(String dimensions) {
        this.setDimensions(dimensions);
        return this;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Set<ProductImage> getImages() {
        return this.images;
    }

    public void setImages(Set<ProductImage> productImages) {
        if (this.images != null) {
            this.images.forEach(i -> i.setProduct(null));
        }
        if (productImages != null) {
            productImages.forEach(i -> i.setProduct(this));
        }
        this.images = productImages;
    }

    public Product images(Set<ProductImage> productImages) {
        this.setImages(productImages);
        return this;
    }

    public Product addImages(ProductImage productImage) {
        this.images.add(productImage);
        productImage.setProduct(this);
        return this;
    }

    public Product removeImages(ProductImage productImage) {
        this.images.remove(productImage);
        productImage.setProduct(null);
        return this;
    }

    public Set<ProductVariant> getVariants() {
        return this.variants;
    }

    public void setVariants(Set<ProductVariant> productVariants) {
        if (this.variants != null) {
            this.variants.forEach(i -> i.setProduct(null));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.setProduct(this));
        }
        this.variants = productVariants;
    }

    public Product variants(Set<ProductVariant> productVariants) {
        this.setVariants(productVariants);
        return this;
    }

    public Product addVariants(ProductVariant productVariant) {
        this.variants.add(productVariant);
        productVariant.setProduct(this);
        return this;
    }

    public Product removeVariants(ProductVariant productVariant) {
        this.variants.remove(productVariant);
        productVariant.setProduct(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setProduct(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setProduct(this));
        }
        this.reviews = reviews;
    }

    public Product reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Product addReviews(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
        return this;
    }

    public Product removeReviews(Review review) {
        this.reviews.remove(review);
        review.setProduct(null);
        return this;
    }

    public Set<Inventory> getInventories() {
        return this.inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        if (this.inventories != null) {
            this.inventories.forEach(i -> i.setProduct(null));
        }
        if (inventories != null) {
            inventories.forEach(i -> i.setProduct(this));
        }
        this.inventories = inventories;
    }

    public Product inventories(Set<Inventory> inventories) {
        this.setInventories(inventories);
        return this;
    }

    public Product addInventory(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.setProduct(this);
        return this;
    }

    public Product removeInventory(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.setProduct(null);
        return this;
    }

    public Set<Promotion> getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Product promotions(Set<Promotion> promotions) {
        this.setPromotions(promotions);
        return this;
    }

    public Product addPromotions(Promotion promotion) {
        this.promotions.add(promotion);
        return this;
    }

    public Product removePromotions(Promotion promotion) {
        this.promotions.remove(promotion);
        return this;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Product brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<UserProfile> getWishlisteds() {
        return this.wishlisteds;
    }

    public void setWishlisteds(Set<UserProfile> userProfiles) {
        if (this.wishlisteds != null) {
            this.wishlisteds.forEach(i -> i.removeWishlist(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addWishlist(this));
        }
        this.wishlisteds = userProfiles;
    }

    public Product wishlisteds(Set<UserProfile> userProfiles) {
        this.setWishlisteds(userProfiles);
        return this;
    }

    public Product addWishlisted(UserProfile userProfile) {
        this.wishlisteds.add(userProfile);
        userProfile.getWishlists().add(this);
        return this;
    }

    public Product removeWishlisted(UserProfile userProfile) {
        this.wishlisteds.remove(userProfile);
        userProfile.getWishlists().remove(this);
        return this;
    }

    public Set<Promotion> getApplicablePromotions() {
        return this.applicablePromotions;
    }

    public void setApplicablePromotions(Set<Promotion> promotions) {
        if (this.applicablePromotions != null) {
            this.applicablePromotions.forEach(i -> i.removeApplicableProducts(this));
        }
        if (promotions != null) {
            promotions.forEach(i -> i.addApplicableProducts(this));
        }
        this.applicablePromotions = promotions;
    }

    public Product applicablePromotions(Set<Promotion> promotions) {
        this.setApplicablePromotions(promotions);
        return this;
    }

    public Product addApplicablePromotions(Promotion promotion) {
        this.applicablePromotions.add(promotion);
        promotion.getApplicableProducts().add(this);
        return this;
    }

    public Product removeApplicablePromotions(Promotion promotion) {
        this.applicablePromotions.remove(promotion);
        promotion.getApplicableProducts().remove(this);
        return this;
    }

    public Set<Category> getFeaturedInCategories() {
        return this.featuredInCategories;
    }

    public void setFeaturedInCategories(Set<Category> categories) {
        if (this.featuredInCategories != null) {
            this.featuredInCategories.forEach(i -> i.removeFeaturedProducts(this));
        }
        if (categories != null) {
            categories.forEach(i -> i.addFeaturedProducts(this));
        }
        this.featuredInCategories = categories;
    }

    public Product featuredInCategories(Set<Category> categories) {
        this.setFeaturedInCategories(categories);
        return this;
    }

    public Product addFeaturedInCategories(Category category) {
        this.featuredInCategories.add(category);
        category.getFeaturedProducts().add(this);
        return this;
    }

    public Product removeFeaturedInCategories(Category category) {
        this.featuredInCategories.remove(category);
        category.getFeaturedProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
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
            "}";
    }
}
