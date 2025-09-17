package com.evaradrip.commerce.service;

import com.evaradrip.commerce.service.dto.ShippingDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evaradrip.commerce.domain.Shipping}.
 */
public interface ShippingService {
    /**
     * Save a shipping.
     *
     * @param shippingDTO the entity to save.
     * @return the persisted entity.
     */
    ShippingDTO save(ShippingDTO shippingDTO);

    /**
     * Updates a shipping.
     *
     * @param shippingDTO the entity to update.
     * @return the persisted entity.
     */
    ShippingDTO update(ShippingDTO shippingDTO);

    /**
     * Partially updates a shipping.
     *
     * @param shippingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShippingDTO> partialUpdate(ShippingDTO shippingDTO);

    /**
     * Get the "id" shipping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShippingDTO> findOne(Long id);

    /**
     * Delete the "id" shipping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
