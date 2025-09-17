package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.ProductImage;
import com.evaradrip.commerce.repository.ProductImageRepository;
import com.evaradrip.commerce.service.criteria.ProductImageCriteria;
import com.evaradrip.commerce.service.dto.ProductImageDTO;
import com.evaradrip.commerce.service.mapper.ProductImageMapper;
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
 * Service for executing complex queries for {@link ProductImage} entities in the database.
 * The main input is a {@link ProductImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductImageQueryService extends QueryService<ProductImage> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageQueryService.class);

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    public ProductImageQueryService(ProductImageRepository productImageRepository, ProductImageMapper productImageMapper) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductImageDTO> findByCriteria(ProductImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductImage> specification = createSpecification(criteria);
        return productImageRepository.findAll(specification, page).map(productImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductImage> specification = createSpecification(criteria);
        return productImageRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductImage> createSpecification(ProductImageCriteria criteria) {
        Specification<ProductImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ProductImage_.id),
                buildStringSpecification(criteria.getImageUrl(), ProductImage_.imageUrl),
                buildStringSpecification(criteria.getAltText(), ProductImage_.altText),
                buildSpecification(criteria.getIsPrimary(), ProductImage_.isPrimary),
                buildRangeSpecification(criteria.getDisplayOrder(), ProductImage_.displayOrder),
                buildSpecification(criteria.getProductId(), root -> root.join(ProductImage_.product, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
