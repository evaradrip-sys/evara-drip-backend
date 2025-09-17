package com.evaradrip.commerce.service;

import com.evaradrip.commerce.service.dto.CouponDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evaradrip.commerce.domain.Coupon}.
 */
public interface CouponService {
    /**
     * Save a coupon.
     *
     * @param couponDTO the entity to save.
     * @return the persisted entity.
     */
    CouponDTO save(CouponDTO couponDTO);

    /**
     * Updates a coupon.
     *
     * @param couponDTO the entity to update.
     * @return the persisted entity.
     */
    CouponDTO update(CouponDTO couponDTO);

    /**
     * Partially updates a coupon.
     *
     * @param couponDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CouponDTO> partialUpdate(CouponDTO couponDTO);

    /**
     * Get the "id" coupon.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CouponDTO> findOne(Long id);

    /**
     * Delete the "id" coupon.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
