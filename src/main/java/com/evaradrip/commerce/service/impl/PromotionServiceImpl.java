package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.repository.PromotionRepository;
import com.evaradrip.commerce.service.PromotionService;
import com.evaradrip.commerce.service.dto.PromotionDTO;
import com.evaradrip.commerce.service.mapper.PromotionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.Promotion}.
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private static final Logger LOG = LoggerFactory.getLogger(PromotionServiceImpl.class);

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public PromotionDTO save(PromotionDTO promotionDTO) {
        LOG.debug("Request to save Promotion : {}", promotionDTO);
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        promotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(promotion);
    }

    @Override
    public PromotionDTO update(PromotionDTO promotionDTO) {
        LOG.debug("Request to update Promotion : {}", promotionDTO);
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        promotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(promotion);
    }

    @Override
    public Optional<PromotionDTO> partialUpdate(PromotionDTO promotionDTO) {
        LOG.debug("Request to partially update Promotion : {}", promotionDTO);

        return promotionRepository
            .findById(promotionDTO.getId())
            .map(existingPromotion -> {
                promotionMapper.partialUpdate(existingPromotion, promotionDTO);

                return existingPromotion;
            })
            .map(promotionRepository::save)
            .map(promotionMapper::toDto);
    }

    public Page<PromotionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return promotionRepository.findAllWithEagerRelationships(pageable).map(promotionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromotionDTO> findOne(Long id) {
        LOG.debug("Request to get Promotion : {}", id);
        return promotionRepository.findOneWithEagerRelationships(id).map(promotionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Promotion : {}", id);
        promotionRepository.deleteById(id);
    }
}
