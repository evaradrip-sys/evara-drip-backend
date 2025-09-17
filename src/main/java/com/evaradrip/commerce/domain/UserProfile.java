package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.Gender;
import com.evaradrip.commerce.domain.enumeration.MembershipLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Extended User Profile
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Size(max = 500)
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Min(value = 0)
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel;

    @Lob
    @Column(name = "preferences")
    private String preferences;

    @Column(name = "newsletter_subscribed")
    private Boolean newsletterSubscribed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<UserAddress> addresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "items", "payments", "shippings", "shippingAddress", "billingAddress", "user" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__wishlist",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "wishlist_id")
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
    private Set<Product> wishlists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__coupons",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "coupons_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Set<Coupon> coupons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public UserProfile phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public UserProfile dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public UserProfile gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public UserProfile avatarUrl(String avatarUrl) {
        this.setAvatarUrl(avatarUrl);
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getLoyaltyPoints() {
        return this.loyaltyPoints;
    }

    public UserProfile loyaltyPoints(Integer loyaltyPoints) {
        this.setLoyaltyPoints(loyaltyPoints);
        return this;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public MembershipLevel getMembershipLevel() {
        return this.membershipLevel;
    }

    public UserProfile membershipLevel(MembershipLevel membershipLevel) {
        this.setMembershipLevel(membershipLevel);
        return this;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public String getPreferences() {
        return this.preferences;
    }

    public UserProfile preferences(String preferences) {
        this.setPreferences(preferences);
        return this;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Boolean getNewsletterSubscribed() {
        return this.newsletterSubscribed;
    }

    public UserProfile newsletterSubscribed(Boolean newsletterSubscribed) {
        this.setNewsletterSubscribed(newsletterSubscribed);
        return this;
    }

    public void setNewsletterSubscribed(Boolean newsletterSubscribed) {
        this.newsletterSubscribed = newsletterSubscribed;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<UserAddress> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<UserAddress> userAddresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setUser(null));
        }
        if (userAddresses != null) {
            userAddresses.forEach(i -> i.setUser(this));
        }
        this.addresses = userAddresses;
    }

    public UserProfile addresses(Set<UserAddress> userAddresses) {
        this.setAddresses(userAddresses);
        return this;
    }

    public UserProfile addAddresses(UserAddress userAddress) {
        this.addresses.add(userAddress);
        userAddress.setUser(this);
        return this;
    }

    public UserProfile removeAddresses(UserAddress userAddress) {
        this.addresses.remove(userAddress);
        userAddress.setUser(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setUser(this));
        }
        this.orders = orders;
    }

    public UserProfile orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public UserProfile addOrders(Order order) {
        this.orders.add(order);
        order.setUser(this);
        return this;
    }

    public UserProfile removeOrders(Order order) {
        this.orders.remove(order);
        order.setUser(null);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setUser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setUser(this));
        }
        this.notifications = notifications;
    }

    public UserProfile notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public UserProfile addNotifications(Notification notification) {
        this.notifications.add(notification);
        notification.setUser(this);
        return this;
    }

    public UserProfile removeNotifications(Notification notification) {
        this.notifications.remove(notification);
        notification.setUser(null);
        return this;
    }

    public Set<Product> getWishlists() {
        return this.wishlists;
    }

    public void setWishlists(Set<Product> products) {
        this.wishlists = products;
    }

    public UserProfile wishlists(Set<Product> products) {
        this.setWishlists(products);
        return this;
    }

    public UserProfile addWishlist(Product product) {
        this.wishlists.add(product);
        return this;
    }

    public UserProfile removeWishlist(Product product) {
        this.wishlists.remove(product);
        return this;
    }

    public Set<Coupon> getCoupons() {
        return this.coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public UserProfile coupons(Set<Coupon> coupons) {
        this.setCoupons(coupons);
        return this;
    }

    public UserProfile addCoupons(Coupon coupon) {
        this.coupons.add(coupon);
        return this;
    }

    public UserProfile removeCoupons(Coupon coupon) {
        this.coupons.remove(coupon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            ", loyaltyPoints=" + getLoyaltyPoints() +
            ", membershipLevel='" + getMembershipLevel() + "'" +
            ", preferences='" + getPreferences() + "'" +
            ", newsletterSubscribed='" + getNewsletterSubscribed() + "'" +
            "}";
    }
}
