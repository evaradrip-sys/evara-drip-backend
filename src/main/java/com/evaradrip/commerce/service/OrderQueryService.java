package com.evaradrip.commerce.service;

import com.evaradrip.commerce.domain.*; // for static metamodels
import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.repository.OrderRepository;
import com.evaradrip.commerce.service.criteria.OrderCriteria;
import com.evaradrip.commerce.service.dto.OrderDTO;
import com.evaradrip.commerce.service.mapper.OrderMapper;
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
 * Service for executing complex queries for {@link Order} entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderQueryService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Return a {@link Page} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findByCriteria(OrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page).map(orderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Order_.id),
                buildStringSpecification(criteria.getOrderNumber(), Order_.orderNumber),
                buildSpecification(criteria.getStatus(), Order_.status),
                buildRangeSpecification(criteria.getTotalAmount(), Order_.totalAmount),
                buildRangeSpecification(criteria.getSubtotalAmount(), Order_.subtotalAmount),
                buildRangeSpecification(criteria.getTaxAmount(), Order_.taxAmount),
                buildRangeSpecification(criteria.getShippingAmount(), Order_.shippingAmount),
                buildRangeSpecification(criteria.getDiscountAmount(), Order_.discountAmount),
                buildSpecification(criteria.getPaymentMethod(), Order_.paymentMethod),
                buildSpecification(criteria.getPaymentStatus(), Order_.paymentStatus),
                buildStringSpecification(criteria.getShippingMethod(), Order_.shippingMethod),
                buildStringSpecification(criteria.getTrackingNumber(), Order_.trackingNumber),
                buildStringSpecification(criteria.getCancelReason(), Order_.cancelReason),
                buildStringSpecification(criteria.getReturnReason(), Order_.returnReason),
                buildRangeSpecification(criteria.getRefundAmount(), Order_.refundAmount),
                buildRangeSpecification(criteria.getEstimatedDeliveryDate(), Order_.estimatedDeliveryDate),
                buildRangeSpecification(criteria.getDeliveredDate(), Order_.deliveredDate),
                buildRangeSpecification(criteria.getShippedDate(), Order_.shippedDate),
                buildSpecification(criteria.getItemsId(), root -> root.join(Order_.items, JoinType.LEFT).get(OrderItem_.id)),
                buildSpecification(criteria.getPaymentsId(), root -> root.join(Order_.payments, JoinType.LEFT).get(Payment_.id)),
                buildSpecification(criteria.getShippingId(), root -> root.join(Order_.shippings, JoinType.LEFT).get(Shipping_.id)),
                buildSpecification(criteria.getShippingAddressId(), root ->
                    root.join(Order_.shippingAddress, JoinType.LEFT).get(UserAddress_.id)
                ),
                buildSpecification(criteria.getBillingAddressId(), root ->
                    root.join(Order_.billingAddress, JoinType.LEFT).get(UserAddress_.id)
                ),
                buildSpecification(criteria.getUserId(), root -> root.join(Order_.user, JoinType.LEFT).get(UserProfile_.id))
            );
        }
        return specification;
    }
}
