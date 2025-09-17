package com.evaradrip.commerce.repository;

import com.evaradrip.commerce.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CategoryRepositoryWithBagRelationshipsImpl implements CategoryRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CATEGORIES_PARAMETER = "categories";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Category> fetchBagRelationships(Optional<Category> category) {
        return category.map(this::fetchFeaturedProducts);
    }

    @Override
    public Page<Category> fetchBagRelationships(Page<Category> categories) {
        return new PageImpl<>(fetchBagRelationships(categories.getContent()), categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public List<Category> fetchBagRelationships(List<Category> categories) {
        return Optional.of(categories).map(this::fetchFeaturedProducts).orElse(Collections.emptyList());
    }

    Category fetchFeaturedProducts(Category result) {
        return entityManager
            .createQuery(
                "select category from Category category left join fetch category.featuredProducts where category.id = :id",
                Category.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Category> fetchFeaturedProducts(List<Category> categories) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, categories.size()).forEach(index -> order.put(categories.get(index).getId(), index));
        List<Category> result = entityManager
            .createQuery(
                "select category from Category category left join fetch category.featuredProducts where category in :categories",
                Category.class
            )
            .setParameter(CATEGORIES_PARAMETER, categories)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
