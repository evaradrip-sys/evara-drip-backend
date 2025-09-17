package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.Category;
import com.evaradrip.commerce.repository.CategoryRepository;
import com.evaradrip.commerce.service.criteria.CategoryCriteria;
import com.evaradrip.commerce.service.dto.CategoryDTO;
import com.evaradrip.commerce.service.mapper.CategoryMapper;
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
 * Service for executing complex queries for {@link Category} entities in the database.
 * The main input is a {@link CategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryQueryService extends QueryService<Category> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryQueryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryQueryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findByCriteria(CategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.fetchBagRelationships(categoryRepository.findAll(specification, page)).map(categoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Category> specification = createSpecification(criteria);
        return categoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Category> createSpecification(CategoryCriteria criteria) {
        Specification<Category> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Category_.id),
                buildStringSpecification(criteria.getName(), Category_.name),
                buildStringSpecification(criteria.getImageUrl(), Category_.imageUrl),
                buildStringSpecification(criteria.getHref(), Category_.href),
                buildSpecification(criteria.getIsFeatured(), Category_.isFeatured),
                buildRangeSpecification(criteria.getDisplayOrder(), Category_.displayOrder),
                buildSpecification(criteria.getProductsId(), root -> root.join(Category_.products, JoinType.LEFT).get(Product_.id)),
                buildSpecification(criteria.getSubcategoriesId(), root ->
                    root.join(Category_.subcategories, JoinType.LEFT).get(Category_.id)
                ),
                buildSpecification(criteria.getFeaturedProductsId(), root ->
                    root.join(Category_.featuredProducts, JoinType.LEFT).get(Product_.id)
                ),
                buildSpecification(criteria.getParentId(), root -> root.join(Category_.parent, JoinType.LEFT).get(Category_.id))
            );
        }
        return specification;
    }
}
