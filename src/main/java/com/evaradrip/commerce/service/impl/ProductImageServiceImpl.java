package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.ProductImage;
import com.evaradrip.commerce.repository.ProductImageRepository;
import com.evaradrip.commerce.service.ProductImageService;
import com.evaradrip.commerce.service.dto.ProductImageDTO;
import com.evaradrip.commerce.service.mapper.ProductImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.ProductImage}.
 */
@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageServiceImpl.class);

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductImageMapper productImageMapper) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public ProductImageDTO save(ProductImageDTO productImageDTO) {
        LOG.debug("Request to save ProductImage : {}", productImageDTO);
        ProductImage productImage = productImageMapper.toEntity(productImageDTO);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(productImage);
    }

    @Override
    public ProductImageDTO update(ProductImageDTO productImageDTO) {
        LOG.debug("Request to update ProductImage : {}", productImageDTO);
        ProductImage productImage = productImageMapper.toEntity(productImageDTO);
        productImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(productImage);
    }

    @Override
    public Optional<ProductImageDTO> partialUpdate(ProductImageDTO productImageDTO) {
        LOG.debug("Request to partially update ProductImage : {}", productImageDTO);

        return productImageRepository
            .findById(productImageDTO.getId())
            .map(existingProductImage -> {
                productImageMapper.partialUpdate(existingProductImage, productImageDTO);

                return existingProductImage;
            })
            .map(productImageRepository::save)
            .map(productImageMapper::toDto);
    }

    public Page<ProductImageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productImageRepository.findAllWithEagerRelationships(pageable).map(productImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductImageDTO> findOne(Long id) {
        LOG.debug("Request to get ProductImage : {}", id);
        return productImageRepository.findOneWithEagerRelationships(id).map(productImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductImage : {}", id);
        productImageRepository.deleteById(id);
    }
}
