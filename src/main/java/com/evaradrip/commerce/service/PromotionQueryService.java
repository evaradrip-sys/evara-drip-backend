package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.repository.PromotionRepository;
import com.evaradrip.commerce.service.criteria.PromotionCriteria;
import com.evaradrip.commerce.service.dto.PromotionDTO;
import com.evaradrip.commerce.service.mapper.PromotionMapper;
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
 * Service for executing complex queries for {@link Promotion} entities in the database.
 * The main input is a {@link PromotionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PromotionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromotionQueryService extends QueryService<Promotion> {

    private static final Logger LOG = LoggerFactory.getLogger(PromotionQueryService.class);

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    public PromotionQueryService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    /**
     * Return a {@link Page} of {@link PromotionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromotionDTO> findByCriteria(PromotionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Promotion> specification = createSpecification(criteria);
        return promotionRepository.fetchBagRelationships(promotionRepository.findAll(specification, page)).map(promotionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromotionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Promotion> specification = createSpecification(criteria);
        return promotionRepository.count(specification);
    }

    /**
     * Function to convert {@link PromotionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Promotion> createSpecification(PromotionCriteria criteria) {
        Specification<Promotion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Promotion_.id),
                buildStringSpecification(criteria.getName(), Promotion_.name),
                buildStringSpecification(criteria.getPromoCode(), Promotion_.promoCode),
                buildSpecification(criteria.getDiscountType(), Promotion_.discountType),
                buildRangeSpecification(criteria.getDiscountValue(), Promotion_.discountValue),
                buildRangeSpecification(criteria.getMinPurchaseAmount(), Promotion_.minPurchaseAmount),
                buildRangeSpecification(criteria.getMaxDiscountAmount(), Promotion_.maxDiscountAmount),
                buildRangeSpecification(criteria.getStartDate(), Promotion_.startDate),
                buildRangeSpecification(criteria.getEndDate(), Promotion_.endDate),
                buildRangeSpecification(criteria.getUsageLimit(), Promotion_.usageLimit),
                buildRangeSpecification(criteria.getUsageCount(), Promotion_.usageCount),
                buildSpecification(criteria.getIsActive(), Promotion_.isActive),
                buildSpecification(criteria.getApplicableProductsId(), root ->
                    root.join(Promotion_.applicableProducts, JoinType.LEFT).get(Product_.id)
                ),
                buildSpecification(criteria.getProductsId(), root -> root.join(Promotion_.products, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
