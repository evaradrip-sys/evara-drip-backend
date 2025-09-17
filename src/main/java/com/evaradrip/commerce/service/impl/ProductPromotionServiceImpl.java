package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.ProductPromotion;
import com.evaradrip.commerce.repository.ProductPromotionRepository;
import com.evaradrip.commerce.service.ProductPromotionService;
import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
import com.evaradrip.commerce.service.mapper.ProductPromotionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.ProductPromotion}.
 */
@Service
@Transactional
public class ProductPromotionServiceImpl implements ProductPromotionService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPromotionServiceImpl.class);

    private final ProductPromotionRepository productPromotionRepository;

    private final ProductPromotionMapper productPromotionMapper;

    public ProductPromotionServiceImpl(
        ProductPromotionRepository productPromotionRepository,
        ProductPromotionMapper productPromotionMapper
    ) {
        this.productPromotionRepository = productPromotionRepository;
        this.productPromotionMapper = productPromotionMapper;
    }

    @Override
    public ProductPromotionDTO save(ProductPromotionDTO productPromotionDTO) {
        LOG.debug("Request to save ProductPromotion : {}", productPromotionDTO);
        ProductPromotion productPromotion = productPromotionMapper.toEntity(productPromotionDTO);
        productPromotion = productPromotionRepository.save(productPromotion);
        return productPromotionMapper.toDto(productPromotion);
    }

    @Override
    public ProductPromotionDTO update(ProductPromotionDTO productPromotionDTO) {
        LOG.debug("Request to update ProductPromotion : {}", productPromotionDTO);
        ProductPromotion productPromotion = productPromotionMapper.toEntity(productPromotionDTO);
        productPromotion = productPromotionRepository.save(productPromotion);
        return productPromotionMapper.toDto(productPromotion);
    }

    @Override
    public Optional<ProductPromotionDTO> partialUpdate(ProductPromotionDTO productPromotionDTO) {
        LOG.debug("Request to partially update ProductPromotion : {}", productPromotionDTO);

        return productPromotionRepository
            .findById(productPromotionDTO.getId())
            .map(existingProductPromotion -> {
                productPromotionMapper.partialUpdate(existingProductPromotion, productPromotionDTO);

                return existingProductPromotion;
            })
            .map(productPromotionRepository::save)
            .map(productPromotionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPromotionDTO> findOne(Long id) {
        LOG.debug("Request to get ProductPromotion : {}", id);
        return productPromotionRepository.findById(id).map(productPromotionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductPromotion : {}", id);
        productPromotionRepository.deleteById(id);
    }
}
