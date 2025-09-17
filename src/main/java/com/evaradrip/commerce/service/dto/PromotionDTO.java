package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Promotion} entity.
 */
@Schema(description = "Promotion/Campaign entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @Size(max = 50)
    private String promoCode;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0")
    private BigDecimal minPurchaseAmount;

    @DecimalMin(value = "0")
    private BigDecimal maxDiscountAmount;

    @NotNull
    private ZonedDateTime startDate;

    @NotNull
    private ZonedDateTime endDate;

    @Min(value = 0)
    private Integer usageLimit;

    @Min(value = 0)
    private Integer usageCount;

    private Boolean isActive;

    @Lob
    private String applicableCategories;

    @Lob
    private String excludedProducts;

    @Lob
    private String termsAndConditions;

    private Set<ProductDTO> applicableProducts = new HashSet<>();

    private Set<ProductDTO> products = new HashSet<>();

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

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getApplicableCategories() {
        return applicableCategories;
    }

    public void setApplicableCategories(String applicableCategories) {
        this.applicableCategories = applicableCategories;
    }

    public String getExcludedProducts() {
        return excludedProducts;
    }

    public void setExcludedProducts(String excludedProducts) {
        this.excludedProducts = excludedProducts;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Set<ProductDTO> getApplicableProducts() {
        return applicableProducts;
    }

    public void setApplicableProducts(Set<ProductDTO> applicableProducts) {
        this.applicableProducts = applicableProducts;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionDTO)) {
            return false;
        }

        PromotionDTO promotionDTO = (PromotionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promotionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionDTO{" +
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
            ", applicableProducts=" + getApplicableProducts() +
            ", products=" + getProducts() +
            "}";
    }
}
