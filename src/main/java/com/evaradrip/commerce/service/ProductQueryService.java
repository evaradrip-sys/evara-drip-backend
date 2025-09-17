package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.repository.ProductRepository;
import com.evaradrip.commerce.service.criteria.ProductCriteria;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.mapper.ProductMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.fetchBagRelationships(productRepository.findAll(specification, page)).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Product_.id),
                buildStringSpecification(criteria.getName(), Product_.name),
                buildRangeSpecification(criteria.getPrice(), Product_.price),
                buildRangeSpecification(criteria.getOriginalPrice(), Product_.originalPrice),
                buildStringSpecification(criteria.getSku(), Product_.sku),
                buildSpecification(criteria.getIsNew(), Product_.isNew),
                buildSpecification(criteria.getIsOnSale(), Product_.isOnSale),
                buildRangeSpecification(criteria.getRating(), Product_.rating),
                buildRangeSpecification(criteria.getReviewsCount(), Product_.reviewsCount),
                buildRangeSpecification(criteria.getStockCount(), Product_.stockCount),
                buildSpecification(criteria.getInStock(), Product_.inStock),
                buildStringSpecification(criteria.getMetaTitle(), Product_.metaTitle),
                buildStringSpecification(criteria.getMetaDescription(), Product_.metaDescription),
                buildStringSpecification(criteria.getMetaKeywords(), Product_.metaKeywords),
                buildSpecification(criteria.getStatus(), Product_.status),
                buildRangeSpecification(criteria.getWeight(), Product_.weight),
                buildStringSpecification(criteria.getDimensions(), Product_.dimensions),
                buildSpecification(criteria.getImagesId(), root -> root.join(Product_.images, JoinType.LEFT).get(ProductImage_.id)),
                buildSpecification(criteria.getVariantsId(), root -> root.join(Product_.variants, JoinType.LEFT).get(ProductVariant_.id)),
                buildSpecification(criteria.getReviewsId(), root -> root.join(Product_.reviews, JoinType.LEFT).get(Review_.id)),
                buildSpecification(criteria.getInventoryId(), root -> root.join(Product_.inventories, JoinType.LEFT).get(Inventory_.id)),
                buildSpecification(criteria.getPromotionsId(), root -> root.join(Product_.promotions, JoinType.LEFT).get(Promotion_.id)),
                buildSpecification(criteria.getBrandId(), root -> root.join(Product_.brand, JoinType.LEFT).get(Brand_.id)),
                buildSpecification(criteria.getCategoryId(), root -> root.join(Product_.category, JoinType.LEFT).get(Category_.id)),
                buildSpecification(criteria.getWishlistedId(), root -> root.join(Product_.wishlisteds, JoinType.LEFT).get(UserProfile_.id)),
                buildSpecification(criteria.getApplicablePromotionsId(), root ->
                    root.join(Product_.applicablePromotions, JoinType.LEFT).get(Promotion_.id)
                ),
                buildSpecification(criteria.getFeaturedInCategoriesId(), root ->
                    root.join(Product_.featuredInCategories, JoinType.LEFT).get(Category_.id)
                )
            );
        }
        return specification;
    }
}
