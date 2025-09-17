package com.evaradrip.commerce.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.Wishlist} entity.
 */
@Schema(description = "Wishlist entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishlistDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    @Size(max = 500)
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishlistDTO)) {
            return false;
        }

        WishlistDTO wishlistDTO = (WishlistDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wishlistDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishlistDTO{" +
            "id=" + getId() +
            ", priority=" + getPriority() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
