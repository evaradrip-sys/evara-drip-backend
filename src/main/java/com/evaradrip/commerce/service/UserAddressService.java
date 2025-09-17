package com.evaradrip.commerce.service;

import com.evaradrip.commerce.service.dto.UserAddressDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.evaradrip.commerce.domain.UserAddress}.
 */
public interface UserAddressService {
    /**
     * Save a userAddress.
     *
     * @param userAddressDTO the entity to save.
     * @return the persisted entity.
     */
    UserAddressDTO save(UserAddressDTO userAddressDTO);

    /**
     * Updates a userAddress.
     *
     * @param userAddressDTO the entity to update.
     * @return the persisted entity.
     */
    UserAddressDTO update(UserAddressDTO userAddressDTO);

    /**
     * Partially updates a userAddress.
     *
     * @param userAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAddressDTO> partialUpdate(UserAddressDTO userAddressDTO);

    /**
     * Get the "id" userAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAddressDTO> findOne(Long id);

    /**
     * Delete the "id" userAddress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
