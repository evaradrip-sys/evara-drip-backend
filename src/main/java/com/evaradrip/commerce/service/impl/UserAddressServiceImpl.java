package com.evaradrip.commerce.service.impl;

import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.repository.UserAddressRepository;
import com.evaradrip.commerce.service.UserAddressService;
import com.evaradrip.commerce.service.dto.UserAddressDTO;
import com.evaradrip.commerce.service.mapper.UserAddressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.evaradrip.commerce.domain.UserAddress}.
 */
@Service
@Transactional
public class UserAddressServiceImpl implements UserAddressService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressServiceImpl(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    @Override
    public UserAddressDTO save(UserAddressDTO userAddressDTO) {
        LOG.debug("Request to save UserAddress : {}", userAddressDTO);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDTO);
        userAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toDto(userAddress);
    }

    @Override
    public UserAddressDTO update(UserAddressDTO userAddressDTO) {
        LOG.debug("Request to update UserAddress : {}", userAddressDTO);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDTO);
        userAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toDto(userAddress);
    }

    @Override
    public Optional<UserAddressDTO> partialUpdate(UserAddressDTO userAddressDTO) {
        LOG.debug("Request to partially update UserAddress : {}", userAddressDTO);

        return userAddressRepository
            .findById(userAddressDTO.getId())
            .map(existingUserAddress -> {
                userAddressMapper.partialUpdate(existingUserAddress, userAddressDTO);

                return existingUserAddress;
            })
            .map(userAddressRepository::save)
            .map(userAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAddressDTO> findOne(Long id) {
        LOG.debug("Request to get UserAddress : {}", id);
        return userAddressRepository.findById(id).map(userAddressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserAddress : {}", id);
        userAddressRepository.deleteById(id);
    }
}
