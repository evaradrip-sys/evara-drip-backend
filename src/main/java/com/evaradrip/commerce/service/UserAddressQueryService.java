package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.repository.UserAddressRepository;
import com.evaradrip.commerce.service.criteria.UserAddressCriteria;
import com.evaradrip.commerce.service.dto.UserAddressDTO;
import com.evaradrip.commerce.service.mapper.UserAddressMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserAddress} entities in the database.
 * The main input is a {@link UserAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAddressQueryService extends QueryService<UserAddress> {

    private static final Logger LOG = LoggerFactory.getLogger(UserAddressQueryService.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressQueryService(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * Return a {@link Page} of {@link UserAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAddressDTO> findByCriteria(UserAddressCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.findAll(specification, page).map(userAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAddressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAddress> createSpecification(UserAddressCriteria criteria) {
        Specification<UserAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserAddress_.id),
                buildSpecification(criteria.getAddressType(), UserAddress_.addressType),
                buildStringSpecification(criteria.getFullName(), UserAddress_.fullName),
                buildStringSpecification(criteria.getPhoneNumber(), UserAddress_.phoneNumber),
                buildStringSpecification(criteria.getStreetAddress(), UserAddress_.streetAddress),
                buildStringSpecification(criteria.getStreetAddress2(), UserAddress_.streetAddress2),
                buildStringSpecification(criteria.getCity(), UserAddress_.city),
                buildStringSpecification(criteria.getState(), UserAddress_.state),
                buildStringSpecification(criteria.getZipCode(), UserAddress_.zipCode),
                buildStringSpecification(criteria.getCountry(), UserAddress_.country),
                buildStringSpecification(criteria.getLandmark(), UserAddress_.landmark),
                buildSpecification(criteria.getIsDefault(), UserAddress_.isDefault),
                buildSpecification(criteria.getUserId(), root -> root.join(UserAddress_.user, JoinType.LEFT).get(UserProfile_.id))
            );
        }
        return specification;
    }
}
