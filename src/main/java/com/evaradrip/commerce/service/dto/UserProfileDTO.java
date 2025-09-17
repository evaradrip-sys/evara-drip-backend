package com.evaradrip.commerce.service.dto;

import com.evaradrip.commerce.domain.enumeration.Gender;
import com.evaradrip.commerce.domain.enumeration.MembershipLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.evaradrip.commerce.domain.UserProfile} entity.
 */
@Schema(description = "Extended User Profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    @Size(max = 500)
    private String avatarUrl;

    @Min(value = 0)
    private Integer loyaltyPoints;

    private MembershipLevel membershipLevel;

    @Lob
    private String preferences;

    private Boolean newsletterSubscribed;

    private UserDTO user;

    private Set<ProductDTO> wishlists = new HashSet<>();

    private Set<CouponDTO> coupons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Boolean getNewsletterSubscribed() {
        return newsletterSubscribed;
    }

    public void setNewsletterSubscribed(Boolean newsletterSubscribed) {
        this.newsletterSubscribed = newsletterSubscribed;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ProductDTO> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<ProductDTO> wishlists) {
        this.wishlists = wishlists;
    }

    public Set<CouponDTO> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<CouponDTO> coupons) {
        this.coupons = coupons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            ", loyaltyPoints=" + getLoyaltyPoints() +
            ", membershipLevel='" + getMembershipLevel() + "'" +
            ", preferences='" + getPreferences() + "'" +
            ", newsletterSubscribed='" + getNewsletterSubscribed() + "'" +
            ", user=" + getUser() +
            ", wishlists=" + getWishlists() +
            ", coupons=" + getCoupons() +
            "}";
    }
}
