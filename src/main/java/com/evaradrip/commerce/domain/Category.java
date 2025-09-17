package com.evaradrip.commerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product Category entity
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @NotNull
    @Size(max = 200)
    @Pattern(regexp = "^\\/[a-z-]+$")
    @Column(name = "href", length = 200, nullable = false)
    private String href;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "subcategories", "featuredProducts", "parent" }, allowSetters = true)
    private Set<Category> subcategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_category__featured_products",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "featured_products_id")
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
    private Set<Product> featuredProducts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products", "subcategories", "featuredProducts", "parent" }, allowSetters = true)
    private Category parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Category imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHref() {
        return this.href;
    }

    public Category href(String href) {
        this.setHref(href);
        return this;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getIsFeatured() {
        return this.isFeatured;
    }

    public Category isFeatured(Boolean isFeatured) {
        this.setIsFeatured(isFeatured);
        return this;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public Category displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCategory(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCategory(this));
        }
        this.products = products;
    }

    public Category products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Category addProducts(Product product) {
        this.products.add(product);
        product.setCategory(this);
        return this;
    }

    public Category removeProducts(Product product) {
        this.products.remove(product);
        product.setCategory(null);
        return this;
    }

    public Set<Category> getSubcategories() {
        return this.subcategories;
    }

    public void setSubcategories(Set<Category> categories) {
        if (this.subcategories != null) {
            this.subcategories.forEach(i -> i.setParent(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setParent(this));
        }
        this.subcategories = categories;
    }

    public Category subcategories(Set<Category> categories) {
        this.setSubcategories(categories);
        return this;
    }

    public Category addSubcategories(Category category) {
        this.subcategories.add(category);
        category.setParent(this);
        return this;
    }

    public Category removeSubcategories(Category category) {
        this.subcategories.remove(category);
        category.setParent(null);
        return this;
    }

    public Set<Product> getFeaturedProducts() {
        return this.featuredProducts;
    }

    public void setFeaturedProducts(Set<Product> products) {
        this.featuredProducts = products;
    }

    public Category featuredProducts(Set<Product> products) {
        this.setFeaturedProducts(products);
        return this;
    }

    public Category addFeaturedProducts(Product product) {
        this.featuredProducts.add(product);
        return this;
    }

    public Category removeFeaturedProducts(Product product) {
        this.featuredProducts.remove(product);
        return this;
    }

    public Category getParent() {
        return this.parent;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", href='" + getHref() + "'" +
            ", isFeatured='" + getIsFeatured() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
