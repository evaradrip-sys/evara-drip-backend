package com.evaradrip.commerce.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.ProductPromotion} entity.
 */
@Schema(description = "Product-Promotion junction table")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductPromotionDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    private Integer priority;

    private Boolean isExclusive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsExclusive() {
        return isExclusive;
    }

    public void setIsExclusive(Boolean isExclusive) {
        this.isExclusive = isExclusive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPromotionDTO)) {
            return false;
        }

        ProductPromotionDTO productPromotionDTO = (ProductPromotionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productPromotionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductPromotionDTO{" +
            "id=" + getId() +
            ", priority=" + getPriority() +
            ", isExclusive='" + getIsExclusive() + "'" +
            "}";
    }
}
