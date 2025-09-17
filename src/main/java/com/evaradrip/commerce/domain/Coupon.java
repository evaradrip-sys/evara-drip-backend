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
 * Coupon entity for user-specific discounts
 */
@Entity
@Table(name = "coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "discount_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private ZonedDateTime validFrom;

    @NotNull
    @Column(name = "valid_until", nullable = false)
    private ZonedDateTime validUntil;

    @Min(value = 1)
    @Column(name = "max_uses")
    private Integer maxUses;

    @Min(value = 0)
    @Column(name = "current_uses")
    private Integer currentUses;

    @DecimalMin(value = "0")
    @Column(name = "min_order_value", precision = 21, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "coupons")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "addresses", "orders", "notifications", "wishlists", "coupons" }, allowSetters = true)
    private Set<UserProfile> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coupon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Coupon code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Coupon description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Coupon discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public Coupon discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public ZonedDateTime getValidFrom() {
        return this.validFrom;
    }

    public Coupon validFrom(ZonedDateTime validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(ZonedDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public ZonedDateTime getValidUntil() {
        return this.validUntil;
    }

    public Coupon validUntil(ZonedDateTime validUntil) {
        this.setValidUntil(validUntil);
        return this;
    }

    public void setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getMaxUses() {
        return this.maxUses;
    }

    public Coupon maxUses(Integer maxUses) {
        this.setMaxUses(maxUses);
        return this;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getCurrentUses() {
        return this.currentUses;
    }

    public Coupon currentUses(Integer currentUses) {
        this.setCurrentUses(currentUses);
        return this;
    }

    public void setCurrentUses(Integer currentUses) {
        this.currentUses = currentUses;
    }

    public BigDecimal getMinOrderValue() {
        return this.minOrderValue;
    }

    public Coupon minOrderValue(BigDecimal minOrderValue) {
        this.setMinOrderValue(minOrderValue);
        return this;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Coupon isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<UserProfile> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserProfile> userProfiles) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeCoupons(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addCoupons(this));
        }
        this.users = userProfiles;
    }

    public Coupon users(Set<UserProfile> userProfiles) {
        this.setUsers(userProfiles);
        return this;
    }

    public Coupon addUsers(UserProfile userProfile) {
        this.users.add(userProfile);
        userProfile.getCoupons().add(this);
        return this;
    }

    public Coupon removeUsers(UserProfile userProfile) {
        this.users.remove(userProfile);
        userProfile.getCoupons().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coupon)) {
            return false;
        }
        return getId() != null && getId().equals(((Coupon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coupon{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", maxUses=" + getMaxUses() +
            ", currentUses=" + getCurrentUses() +
            ", minOrderValue=" + getMinOrderValue() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
