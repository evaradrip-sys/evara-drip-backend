package com.evaradrip.commerce.domain;

import com.evaradrip.commerce.domain.enumeration.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product Reviews entity
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Size(max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @Lob
    @Column(name = "comment")
    private String comment;

    @Min(value = 0)
    @Column(name = "helpful_count")
    private Integer helpfulCount;

    @Min(value = 0)
    @Column(name = "not_helpful_count")
    private Integer notHelpfulCount;

    @Column(name = "verified_purchase")
    private Boolean verifiedPurchase;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReviewStatus status;

    @Lob
    @Column(name = "response")
    private String response;

    @Column(name = "response_date")
    private ZonedDateTime responseDate;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
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
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Review rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return this.title;
    }

    public Review title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return this.comment;
    }

    public Review comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getHelpfulCount() {
        return this.helpfulCount;
    }

    public Review helpfulCount(Integer helpfulCount) {
        this.setHelpfulCount(helpfulCount);
        return this;
    }

    public void setHelpfulCount(Integer helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public Integer getNotHelpfulCount() {
        return this.notHelpfulCount;
    }

    public Review notHelpfulCount(Integer notHelpfulCount) {
        this.setNotHelpfulCount(notHelpfulCount);
        return this;
    }

    public void setNotHelpfulCount(Integer notHelpfulCount) {
        this.notHelpfulCount = notHelpfulCount;
    }

    public Boolean getVerifiedPurchase() {
        return this.verifiedPurchase;
    }

    public Review verifiedPurchase(Boolean verifiedPurchase) {
        this.setVerifiedPurchase(verifiedPurchase);
        return this;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public ReviewStatus getStatus() {
        return this.status;
    }

    public Review status(ReviewStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getResponse() {
        return this.response;
    }

    public Review response(String response) {
        this.setResponse(response);
        return this;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ZonedDateTime getResponseDate() {
        return this.responseDate;
    }

    public Review responseDate(ZonedDateTime responseDate) {
        this.setResponseDate(responseDate);
        return this;
    }

    public void setResponseDate(ZonedDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review user(User user) {
        this.setUser(user);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Review product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return getId() != null && getId().equals(((Review) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", title='" + getTitle() + "'" +
            ", comment='" + getComment() + "'" +
            ", helpfulCount=" + getHelpfulCount() +
            ", notHelpfulCount=" + getNotHelpfulCount() +
            ", verifiedPurchase='" + getVerifiedPurchase() + "'" +
            ", status='" + getStatus() + "'" +
            ", response='" + getResponse() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            "}";
    }
}
