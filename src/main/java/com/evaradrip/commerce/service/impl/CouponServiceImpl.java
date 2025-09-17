package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.Coupon;
import com.evaradrip.commerce.repository.CouponRepository;
import com.evaradrip.commerce.service.CouponService;
import com.evaradrip.commerce.service.dto.CouponDTO;
import com.evaradrip.commerce.service.mapper.CouponMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.Coupon}.
 */
@Service
@Transactional
public class CouponServiceImpl implements CouponService {

    private static final Logger LOG = LoggerFactory.getLogger(CouponServiceImpl.class);

    private final CouponRepository couponRepository;

    private final CouponMapper couponMapper;

    public CouponServiceImpl(CouponRepository couponRepository, CouponMapper couponMapper) {
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
    }

    @Override
    public CouponDTO save(CouponDTO couponDTO) {
        LOG.debug("Request to save Coupon : {}", couponDTO);
        Coupon coupon = couponMapper.toEntity(couponDTO);
        coupon = couponRepository.save(coupon);
        return couponMapper.toDto(coupon);
    }

    @Override
    public CouponDTO update(CouponDTO couponDTO) {
        LOG.debug("Request to update Coupon : {}", couponDTO);
        Coupon coupon = couponMapper.toEntity(couponDTO);
        coupon = couponRepository.save(coupon);
        return couponMapper.toDto(coupon);
    }

    @Override
    public Optional<CouponDTO> partialUpdate(CouponDTO couponDTO) {
        LOG.debug("Request to partially update Coupon : {}", couponDTO);

        return couponRepository
            .findById(couponDTO.getId())
            .map(existingCoupon -> {
                couponMapper.partialUpdate(existingCoupon, couponDTO);

                return existingCoupon;
            })
            .map(couponRepository::save)
            .map(couponMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CouponDTO> findOne(Long id) {
        LOG.debug("Request to get Coupon : {}", id);
        return couponRepository.findById(id).map(couponMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Coupon : {}", id);
        couponRepository.deleteById(id);
    }
}
