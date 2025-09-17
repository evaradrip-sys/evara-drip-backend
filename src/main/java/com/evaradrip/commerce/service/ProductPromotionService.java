package com.evaradrip.commerce.service;

import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evaradrip.commerce.domain.ProductPromotion}.
 */
public interface ProductPromotionService {
    /**
     * Save a productPromotion.
     *
     * @param productPromotionDTO the entity to save.
     * @return the persisted entity.
     */
    ProductPromotionDTO save(ProductPromotionDTO productPromotionDTO);

    /**
     * Updates a productPromotion.
     *
     * @param productPromotionDTO the entity to update.
     * @return the persisted entity.
     */
    ProductPromotionDTO update(ProductPromotionDTO productPromotionDTO);

    /**
     * Partially updates a productPromotion.
     *
     * @param productPromotionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductPromotionDTO> partialUpdate(ProductPromotionDTO productPromotionDTO);

    /**
     * Get the "id" productPromotion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductPromotionDTO> findOne(Long id);

    /**
     * Delete the "id" productPromotion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
