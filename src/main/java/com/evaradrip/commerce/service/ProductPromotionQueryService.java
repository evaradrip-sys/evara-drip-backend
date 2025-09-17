package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.ProductPromotion;
import com.evaradrip.commerce.repository.ProductPromotionRepository;
import com.evaradrip.commerce.service.criteria.ProductPromotionCriteria;
import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
import com.evaradrip.commerce.service.mapper.ProductPromotionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductPromotion} entities in the database.
 * The main input is a {@link ProductPromotionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductPromotionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductPromotionQueryService extends QueryService<ProductPromotion> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPromotionQueryService.class);

    private final ProductPromotionRepository productPromotionRepository;

    private final ProductPromotionMapper productPromotionMapper;

    public ProductPromotionQueryService(
        ProductPromotionRepository productPromotionRepository,
        ProductPromotionMapper productPromotionMapper
    ) {
        this.productPromotionRepository = productPromotionRepository;
        this.productPromotionMapper = productPromotionMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductPromotionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductPromotionDTO> findByCriteria(ProductPromotionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductPromotion> specification = createSpecification(criteria);
        return productPromotionRepository.findAll(specification, page).map(productPromotionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductPromotionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductPromotion> specification = createSpecification(criteria);
        return productPromotionRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductPromotionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductPromotion> createSpecification(ProductPromotionCriteria criteria) {
        Specification<ProductPromotion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ProductPromotion_.id),
                buildRangeSpecification(criteria.getPriority(), ProductPromotion_.priority),
                buildSpecification(criteria.getIsExclusive(), ProductPromotion_.isExclusive)
            );
        }
        return specification;
    }
}
