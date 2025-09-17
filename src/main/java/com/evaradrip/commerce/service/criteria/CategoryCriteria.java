package com.evaradrip.commerce.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evaradrip.commerce.domain.Category} entity. This class is used
 * in {@link com.evaradrip.commerce.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter imageUrl;

    private StringFilter href;

    private BooleanFilter isFeatured;

    private IntegerFilter displayOrder;

    private LongFilter productsId;

    private LongFilter subcategoriesId;

    private LongFilter featuredProductsId;

    private LongFilter parentId;

    private Boolean distinct;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.href = other.optionalHref().map(StringFilter::copy).orElse(null);
        this.isFeatured = other.optionalIsFeatured().map(BooleanFilter::copy).orElse(null);
        this.displayOrder = other.optionalDisplayOrder().map(IntegerFilter::copy).orElse(null);
        this.productsId = other.optionalProductsId().map(LongFilter::copy).orElse(null);
        this.subcategoriesId = other.optionalSubcategoriesId().map(LongFilter::copy).orElse(null);
        this.featuredProductsId = other.optionalFeaturedProductsId().map(LongFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getHref() {
        return href;
    }

    public Optional<StringFilter> optionalHref() {
        return Optional.ofNullable(href);
    }

    public StringFilter href() {
        if (href == null) {
            setHref(new StringFilter());
        }
        return href;
    }

    public void setHref(StringFilter href) {
        this.href = href;
    }

    public BooleanFilter getIsFeatured() {
        return isFeatured;
    }

    public Optional<BooleanFilter> optionalIsFeatured() {
        return Optional.ofNullable(isFeatured);
    }

    public BooleanFilter isFeatured() {
        if (isFeatured == null) {
            setIsFeatured(new BooleanFilter());
        }
        return isFeatured;
    }

    public void setIsFeatured(BooleanFilter isFeatured) {
        this.isFeatured = isFeatured;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public Optional<IntegerFilter> optionalDisplayOrder() {
        return Optional.ofNullable(displayOrder);
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            setDisplayOrder(new IntegerFilter());
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public Optional<LongFilter> optionalProductsId() {
        return Optional.ofNullable(productsId);
    }

    public LongFilter productsId() {
        if (productsId == null) {
            setProductsId(new LongFilter());
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getSubcategoriesId() {
        return subcategoriesId;
    }

    public Optional<LongFilter> optionalSubcategoriesId() {
        return Optional.ofNullable(subcategoriesId);
    }

    public LongFilter subcategoriesId() {
        if (subcategoriesId == null) {
            setSubcategoriesId(new LongFilter());
        }
        return subcategoriesId;
    }

    public void setSubcategoriesId(LongFilter subcategoriesId) {
        this.subcategoriesId = subcategoriesId;
    }

    public LongFilter getFeaturedProductsId() {
        return featuredProductsId;
    }

    public Optional<LongFilter> optionalFeaturedProductsId() {
        return Optional.ofNullable(featuredProductsId);
    }

    public LongFilter featuredProductsId() {
        if (featuredProductsId == null) {
            setFeaturedProductsId(new LongFilter());
        }
        return featuredProductsId;
    }

    public void setFeaturedProductsId(LongFilter featuredProductsId) {
        this.featuredProductsId = featuredProductsId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(href, that.href) &&
            Objects.equals(isFeatured, that.isFeatured) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(subcategoriesId, that.subcategoriesId) &&
            Objects.equals(featuredProductsId, that.featuredProductsId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            imageUrl,
            href,
            isFeatured,
            displayOrder,
            productsId,
            subcategoriesId,
            featuredProductsId,
            parentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalHref().map(f -> "href=" + f + ", ").orElse("") +
            optionalIsFeatured().map(f -> "isFeatured=" + f + ", ").orElse("") +
            optionalDisplayOrder().map(f -> "displayOrder=" + f + ", ").orElse("") +
            optionalProductsId().map(f -> "productsId=" + f + ", ").orElse("") +
            optionalSubcategoriesId().map(f -> "subcategoriesId=" + f + ", ").orElse("") +
            optionalFeaturedProductsId().map(f -> "featuredProductsId=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
