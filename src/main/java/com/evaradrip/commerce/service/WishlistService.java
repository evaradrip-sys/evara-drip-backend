package com.evaradrip.commerce.service;

import com.evaradrip.commerce.service.dto.WishlistDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evaradrip.commerce.domain.Wishlist}.
 */
public interface WishlistService {
    /**
     * Save a wishlist.
     *
     * @param wishlistDTO the entity to save.
     * @return the persisted entity.
     */
    WishlistDTO save(WishlistDTO wishlistDTO);

    /**
     * Updates a wishlist.
     *
     * @param wishlistDTO the entity to update.
     * @return the persisted entity.
     */
    WishlistDTO update(WishlistDTO wishlistDTO);

    /**
     * Partially updates a wishlist.
     *
     * @param wishlistDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WishlistDTO> partialUpdate(WishlistDTO wishlistDTO);

    /**
     * Get the "id" wishlist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WishlistDTO> findOne(Long id);

    /**
     * Delete the "id" wishlist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
