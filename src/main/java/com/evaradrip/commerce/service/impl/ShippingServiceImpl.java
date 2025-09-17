package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.Shipping;
import com.evaradrip.commerce.repository.ShippingRepository;
import com.evaradrip.commerce.service.ShippingService;
import com.evaradrip.commerce.service.dto.ShippingDTO;
import com.evaradrip.commerce.service.mapper.ShippingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.Shipping}.
 */
@Service
@Transactional
public class ShippingServiceImpl implements ShippingService {

    private static final Logger LOG = LoggerFactory.getLogger(ShippingServiceImpl.class);

    private final ShippingRepository shippingRepository;

    private final ShippingMapper shippingMapper;

    public ShippingServiceImpl(ShippingRepository shippingRepository, ShippingMapper shippingMapper) {
        this.shippingRepository = shippingRepository;
        this.shippingMapper = shippingMapper;
    }

    @Override
    public ShippingDTO save(ShippingDTO shippingDTO) {
        LOG.debug("Request to save Shipping : {}", shippingDTO);
        Shipping shipping = shippingMapper.toEntity(shippingDTO);
        shipping = shippingRepository.save(shipping);
        return shippingMapper.toDto(shipping);
    }

    @Override
    public ShippingDTO update(ShippingDTO shippingDTO) {
        LOG.debug("Request to update Shipping : {}", shippingDTO);
        Shipping shipping = shippingMapper.toEntity(shippingDTO);
        shipping = shippingRepository.save(shipping);
        return shippingMapper.toDto(shipping);
    }

    @Override
    public Optional<ShippingDTO> partialUpdate(ShippingDTO shippingDTO) {
        LOG.debug("Request to partially update Shipping : {}", shippingDTO);

        return shippingRepository
            .findById(shippingDTO.getId())
            .map(existingShipping -> {
                shippingMapper.partialUpdate(existingShipping, shippingDTO);

                return existingShipping;
            })
            .map(shippingRepository::save)
            .map(shippingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShippingDTO> findOne(Long id) {
        LOG.debug("Request to get Shipping : {}", id);
        return shippingRepository.findById(id).map(shippingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Shipping : {}", id);
        shippingRepository.deleteById(id);
    }
}
