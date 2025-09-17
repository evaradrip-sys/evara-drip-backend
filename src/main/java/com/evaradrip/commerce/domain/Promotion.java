package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Promotion/Campaign entity
 */
@Entity
@Table(name = "promotion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Promotion implements Serializable {

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

    @Size(max = 50)
    @Column(name = "promo_code", length = 50, unique = true)
    private String promoCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "discount_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @DecimalMin(value = "0")
    @Column(name = "min_purchase_amount", precision = 21, scale = 2)
    private BigDecimal minPurchaseAmount;

    @DecimalMin(value = "0")
    @Column(name = "max_discount_amount", precision = 21, scale = 2)
    private BigDecimal maxDiscountAmount;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Min(value = 0)
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Min(value = 0)
    @Column(name = "usage_count")
    private Integer usageCount;

    @Column(name = "is_active")
    private Boolean isActive;

    @Lob
    @Column(name = "applicable_categories")
    private String applicableCategories;

    @Lob
    @Column(name = "excluded_products")
    private String excludedProducts;

    @Lob
    @Column(name = "terms_and_conditions")
    private String termsAndConditions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_promotion__applicable_products",
        joinColumns = @JoinColumn(name = "promotion_id"),
        inverseJoinColumns = @JoinColumn(name = "applicable_products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Product> applicableProducts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "promotions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Promotion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Promotion name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Promotion description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoCode() {
        return this.promoCode;
    }

    public Promotion promoCode(String promoCode) {
        this.setPromoCode(promoCode);
        return this;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Promotion discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public Promotion discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinPurchaseAmount() {
        return this.minPurchaseAmount;
    }

    public Promotion minPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.setMinPurchaseAmount(minPurchaseAmount);
        return this;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return this.maxDiscountAmount;
    }

    public Promotion maxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.setMaxDiscountAmount(maxDiscountAmount);
        return this;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Promotion startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public Promotion endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getUsageLimit() {
        return this.usageLimit;
    }

    public Promotion usageLimit(Integer usageLimit) {
        this.setUsageLimit(usageLimit);
        return this;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return this.usageCount;
    }

    public Promotion usageCount(Integer usageCount) {
        this.setUsageCount(usageCount);
        return this;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Promotion isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getApplicableCategories() {
        return this.applicableCategories;
    }

    public Promotion applicableCategories(String applicableCategories) {
        this.setApplicableCategories(applicableCategories);
        return this;
    }

    public void setApplicableCategories(String applicableCategories) {
        this.applicableCategories = applicableCategories;
    }

    public String getExcludedProducts() {
        return this.excludedProducts;
    }

    public Promotion excludedProducts(String excludedProducts) {
        this.setExcludedProducts(excludedProducts);
        return this;
    }

    public void setExcludedProducts(String excludedProducts) {
        this.excludedProducts = excludedProducts;
    }

    public String getTermsAndConditions() {
        return this.termsAndConditions;
    }

    public Promotion termsAndConditions(String termsAndConditions) {
        this.setTermsAndConditions(termsAndConditions);
        return this;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Set<Product> getApplicableProducts() {
        return this.applicableProducts;
    }

    public void setApplicableProducts(Set<Product> products) {
        this.applicableProducts = products;
    }

    public Promotion applicableProducts(Set<Product> products) {
        this.setApplicableProducts(products);
        return this;
    }

    public Promotion addApplicableProducts(Product product) {
        this.applicableProducts.add(product);
        return this;
    }

    public Promotion removeApplicableProducts(Product product) {
        this.applicableProducts.remove(product);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.removePromotions(this));
        }
        if (products != null) {
            products.forEach(i -> i.addPromotions(this));
        }
        this.products = products;
    }

    public Promotion products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Promotion addProducts(Product product) {
        this.products.add(product);
        product.getPromotions().add(this);
        return this;
    }

    public Promotion removeProducts(Product product) {
        this.products.remove(product);
        product.getPromotions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promotion)) {
            return false;
        }
        return getId() != null && getId().equals(((Promotion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promotion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", promoCode='" + getPromoCode() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", minPurchaseAmount=" + getMinPurchaseAmount() +
            ", maxDiscountAmount=" + getMaxDiscountAmount() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", usageLimit=" + getUsageLimit() +
            ", usageCount=" + getUsageCount() +
            ", isActive='" + getIsActive() + "'" +
            ", applicableCategories='" + getApplicableCategories() + "'" +
            ", excludedProducts='" + getExcludedProducts() + "'" +
            ", termsAndConditions='" + getTermsAndConditions() + "'" +
            "}";
    }
}
