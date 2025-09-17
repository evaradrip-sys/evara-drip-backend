package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.ProductVariant;
import com.evaradrip.commerce.repository.ProductVariantRepository;
import com.evaradrip.commerce.service.criteria.ProductVariantCriteria;
import com.evaradrip.commerce.service.dto.ProductVariantDTO;
import com.evaradrip.commerce.service.mapper.ProductVariantMapper;
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
 * Service for executing complex queries for {@link ProductVariant} entities in the database.
 * The main input is a {@link ProductVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantQueryService extends QueryService<ProductVariant> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantQueryService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantQueryService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductVariantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.findAll(specification, page).map(productVariantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariant> createSpecification(ProductVariantCriteria criteria) {
        Specification<ProductVariant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ProductVariant_.id),
                buildSpecification(criteria.getVariantSize(), ProductVariant_.variantSize),
                buildStringSpecification(criteria.getColor(), ProductVariant_.color),
                buildStringSpecification(criteria.getSku(), ProductVariant_.sku),
                buildRangeSpecification(criteria.getStockCount(), ProductVariant_.stockCount),
                buildRangeSpecification(criteria.getPriceAdjustment(), ProductVariant_.priceAdjustment),
                buildStringSpecification(criteria.getBarcode(), ProductVariant_.barcode),
                buildRangeSpecification(criteria.getWeight(), ProductVariant_.weight),
                buildSpecification(criteria.getProductId(), root -> root.join(ProductVariant_.product, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
