package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.OrderAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.domain.enumeration.OrderStatus;
import com.evaradrip.commerce.domain.enumeration.PaymentMethod;
import com.evaradrip.commerce.domain.enumeration.PaymentStatus;
import com.evaradrip.commerce.repository.OrderRepository;
import com.evaradrip.commerce.service.dto.OrderDTO;
import com.evaradrip.commerce.service.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.CONFIRMED;

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_SUBTOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_SUBTOTAL_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TAX_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_SHIPPING_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_SHIPPING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_SHIPPING_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISCOUNT_AMOUNT = new BigDecimal(0 - 1);

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.CREDIT_CARD;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.DEBIT_CARD;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.PROCESSING;

    private static final String DEFAULT_SHIPPING_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_RETURN_REASON = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_REASON = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REFUND_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_REFUND_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_REFUND_AMOUNT = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_ESTIMATED_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ESTIMATED_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_DELIVERED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELIVERED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DELIVERED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_SHIPPED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SHIPPED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SHIPPED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    private Order insertedOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .status(DEFAULT_STATUS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .subtotalAmount(DEFAULT_SUBTOTAL_AMOUNT)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .shippingAmount(DEFAULT_SHIPPING_AMOUNT)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .shippingMethod(DEFAULT_SHIPPING_METHOD)
            .trackingNumber(DEFAULT_TRACKING_NUMBER)
            .notes(DEFAULT_NOTES)
            .cancelReason(DEFAULT_CANCEL_REASON)
            .returnReason(DEFAULT_RETURN_REASON)
            .refundAmount(DEFAULT_REFUND_AMOUNT)
            .estimatedDeliveryDate(DEFAULT_ESTIMATED_DELIVERY_DATE)
            .deliveredDate(DEFAULT_DELIVERED_DATE)
            .shippedDate(DEFAULT_SHIPPED_DATE);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        order.setShippingAddress(userAddress);
        // Add required entity
        order.setBillingAddress(userAddress);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        order.setUser(userProfile);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order updatedOrder = new Order()
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subtotalAmount(UPDATED_SUBTOTAL_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .shippingMethod(UPDATED_SHIPPING_METHOD)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .notes(UPDATED_NOTES)
            .cancelReason(UPDATED_CANCEL_REASON)
            .returnReason(UPDATED_RETURN_REASON)
            .refundAmount(UPDATED_REFUND_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .deliveredDate(UPDATED_DELIVERED_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createUpdatedEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        updatedOrder.setShippingAddress(userAddress);
        // Add required entity
        updatedOrder.setBillingAddress(userAddress);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        updatedOrder.setUser(userProfile);
        return updatedOrder;
    }

    @BeforeEach
    void initTest() {
        order = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrder != null) {
            orderRepository.delete(insertedOrder);
            insertedOrder = null;
        }
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        var returnedOrderDTO = om.readValue(
            restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderDTO.class
        );

        // Validate the Order in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrder = orderMapper.toEntity(returnedOrderDTO);
        assertOrderUpdatableFieldsEquals(returnedOrder, getPersistedOrder(returnedOrder));

        insertedOrder = returnedOrder;
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        order.setOrderNumber(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        order.setStatus(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        order.setTotalAmount(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        order.setSubtotalAmount(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].subtotalAmount").value(hasItem(sameNumber(DEFAULT_SUBTOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].shippingAmount").value(hasItem(sameNumber(DEFAULT_SHIPPING_AMOUNT))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].shippingMethod").value(hasItem(DEFAULT_SHIPPING_METHOD)))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)))
            .andExpect(jsonPath("$.[*].returnReason").value(hasItem(DEFAULT_RETURN_REASON)))
            .andExpect(jsonPath("$.[*].refundAmount").value(hasItem(sameNumber(DEFAULT_REFUND_AMOUNT))))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveredDate").value(hasItem(sameInstant(DEFAULT_DELIVERED_DATE))))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(sameInstant(DEFAULT_SHIPPED_DATE))));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.subtotalAmount").value(sameNumber(DEFAULT_SUBTOTAL_AMOUNT)))
            .andExpect(jsonPath("$.taxAmount").value(sameNumber(DEFAULT_TAX_AMOUNT)))
            .andExpect(jsonPath("$.shippingAmount").value(sameNumber(DEFAULT_SHIPPING_AMOUNT)))
            .andExpect(jsonPath("$.discountAmount").value(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.shippingMethod").value(DEFAULT_SHIPPING_METHOD))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.cancelReason").value(DEFAULT_CANCEL_REASON))
            .andExpect(jsonPath("$.returnReason").value(DEFAULT_RETURN_REASON))
            .andExpect(jsonPath("$.refundAmount").value(sameNumber(DEFAULT_REFUND_AMOUNT)))
            .andExpect(jsonPath("$.estimatedDeliveryDate").value(DEFAULT_ESTIMATED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.deliveredDate").value(sameInstant(DEFAULT_DELIVERED_DATE)))
            .andExpect(jsonPath("$.shippedDate").value(sameInstant(DEFAULT_SHIPPED_DATE)));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderNumber equals to
        defaultOrderFiltering("orderNumber.equals=" + DEFAULT_ORDER_NUMBER, "orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderNumber in
        defaultOrderFiltering(
            "orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER,
            "orderNumber.in=" + UPDATED_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrdersByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderNumber is not null
        defaultOrderFiltering("orderNumber.specified=true", "orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderNumber contains
        defaultOrderFiltering("orderNumber.contains=" + DEFAULT_ORDER_NUMBER, "orderNumber.contains=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where orderNumber does not contain
        defaultOrderFiltering("orderNumber.doesNotContain=" + UPDATED_ORDER_NUMBER, "orderNumber.doesNotContain=" + DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where status equals to
        defaultOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where status in
        defaultOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where status is not null
        defaultOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount equals to
        defaultOrderFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount in
        defaultOrderFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is not null
        defaultOrderFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is greater than or equal to
        defaultOrderFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is less than or equal to
        defaultOrderFiltering("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is less than
        defaultOrderFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where totalAmount is greater than
        defaultOrderFiltering("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT, "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount equals to
        defaultOrderFiltering("subtotalAmount.equals=" + DEFAULT_SUBTOTAL_AMOUNT, "subtotalAmount.equals=" + UPDATED_SUBTOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount in
        defaultOrderFiltering(
            "subtotalAmount.in=" + DEFAULT_SUBTOTAL_AMOUNT + "," + UPDATED_SUBTOTAL_AMOUNT,
            "subtotalAmount.in=" + UPDATED_SUBTOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount is not null
        defaultOrderFiltering("subtotalAmount.specified=true", "subtotalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount is greater than or equal to
        defaultOrderFiltering(
            "subtotalAmount.greaterThanOrEqual=" + DEFAULT_SUBTOTAL_AMOUNT,
            "subtotalAmount.greaterThanOrEqual=" + UPDATED_SUBTOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount is less than or equal to
        defaultOrderFiltering(
            "subtotalAmount.lessThanOrEqual=" + DEFAULT_SUBTOTAL_AMOUNT,
            "subtotalAmount.lessThanOrEqual=" + SMALLER_SUBTOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount is less than
        defaultOrderFiltering("subtotalAmount.lessThan=" + UPDATED_SUBTOTAL_AMOUNT, "subtotalAmount.lessThan=" + DEFAULT_SUBTOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersBySubtotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where subtotalAmount is greater than
        defaultOrderFiltering(
            "subtotalAmount.greaterThan=" + SMALLER_SUBTOTAL_AMOUNT,
            "subtotalAmount.greaterThan=" + DEFAULT_SUBTOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount equals to
        defaultOrderFiltering("taxAmount.equals=" + DEFAULT_TAX_AMOUNT, "taxAmount.equals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount in
        defaultOrderFiltering("taxAmount.in=" + DEFAULT_TAX_AMOUNT + "," + UPDATED_TAX_AMOUNT, "taxAmount.in=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount is not null
        defaultOrderFiltering("taxAmount.specified=true", "taxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount is greater than or equal to
        defaultOrderFiltering("taxAmount.greaterThanOrEqual=" + DEFAULT_TAX_AMOUNT, "taxAmount.greaterThanOrEqual=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount is less than or equal to
        defaultOrderFiltering("taxAmount.lessThanOrEqual=" + DEFAULT_TAX_AMOUNT, "taxAmount.lessThanOrEqual=" + SMALLER_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount is less than
        defaultOrderFiltering("taxAmount.lessThan=" + UPDATED_TAX_AMOUNT, "taxAmount.lessThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where taxAmount is greater than
        defaultOrderFiltering("taxAmount.greaterThan=" + SMALLER_TAX_AMOUNT, "taxAmount.greaterThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount equals to
        defaultOrderFiltering("shippingAmount.equals=" + DEFAULT_SHIPPING_AMOUNT, "shippingAmount.equals=" + UPDATED_SHIPPING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount in
        defaultOrderFiltering(
            "shippingAmount.in=" + DEFAULT_SHIPPING_AMOUNT + "," + UPDATED_SHIPPING_AMOUNT,
            "shippingAmount.in=" + UPDATED_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount is not null
        defaultOrderFiltering("shippingAmount.specified=true", "shippingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount is greater than or equal to
        defaultOrderFiltering(
            "shippingAmount.greaterThanOrEqual=" + DEFAULT_SHIPPING_AMOUNT,
            "shippingAmount.greaterThanOrEqual=" + UPDATED_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount is less than or equal to
        defaultOrderFiltering(
            "shippingAmount.lessThanOrEqual=" + DEFAULT_SHIPPING_AMOUNT,
            "shippingAmount.lessThanOrEqual=" + SMALLER_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount is less than
        defaultOrderFiltering("shippingAmount.lessThan=" + UPDATED_SHIPPING_AMOUNT, "shippingAmount.lessThan=" + DEFAULT_SHIPPING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingAmount is greater than
        defaultOrderFiltering(
            "shippingAmount.greaterThan=" + SMALLER_SHIPPING_AMOUNT,
            "shippingAmount.greaterThan=" + DEFAULT_SHIPPING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount equals to
        defaultOrderFiltering("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT, "discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount in
        defaultOrderFiltering(
            "discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT,
            "discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount is not null
        defaultOrderFiltering("discountAmount.specified=true", "discountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount is greater than or equal to
        defaultOrderFiltering(
            "discountAmount.greaterThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.greaterThanOrEqual=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount is less than or equal to
        defaultOrderFiltering(
            "discountAmount.lessThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.lessThanOrEqual=" + SMALLER_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount is less than
        defaultOrderFiltering("discountAmount.lessThan=" + UPDATED_DISCOUNT_AMOUNT, "discountAmount.lessThan=" + DEFAULT_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where discountAmount is greater than
        defaultOrderFiltering(
            "discountAmount.greaterThan=" + SMALLER_DISCOUNT_AMOUNT,
            "discountAmount.greaterThan=" + DEFAULT_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod equals to
        defaultOrderFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod in
        defaultOrderFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod is not null
        defaultOrderFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus equals to
        defaultOrderFiltering("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS, "paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus in
        defaultOrderFiltering(
            "paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS,
            "paymentStatus.in=" + UPDATED_PAYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentStatus is not null
        defaultOrderFiltering("paymentStatus.specified=true", "paymentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByShippingMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingMethod equals to
        defaultOrderFiltering("shippingMethod.equals=" + DEFAULT_SHIPPING_METHOD, "shippingMethod.equals=" + UPDATED_SHIPPING_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingMethod in
        defaultOrderFiltering(
            "shippingMethod.in=" + DEFAULT_SHIPPING_METHOD + "," + UPDATED_SHIPPING_METHOD,
            "shippingMethod.in=" + UPDATED_SHIPPING_METHOD
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippingMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingMethod is not null
        defaultOrderFiltering("shippingMethod.specified=true", "shippingMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByShippingMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingMethod contains
        defaultOrderFiltering("shippingMethod.contains=" + DEFAULT_SHIPPING_METHOD, "shippingMethod.contains=" + UPDATED_SHIPPING_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippingMethod does not contain
        defaultOrderFiltering(
            "shippingMethod.doesNotContain=" + UPDATED_SHIPPING_METHOD,
            "shippingMethod.doesNotContain=" + DEFAULT_SHIPPING_METHOD
        );
    }

    @Test
    @Transactional
    void getAllOrdersByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where trackingNumber equals to
        defaultOrderFiltering("trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER, "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrdersByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where trackingNumber in
        defaultOrderFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrdersByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where trackingNumber is not null
        defaultOrderFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where trackingNumber contains
        defaultOrderFiltering("trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER, "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrdersByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where trackingNumber does not contain
        defaultOrderFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrdersByCancelReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelReason equals to
        defaultOrderFiltering("cancelReason.equals=" + DEFAULT_CANCEL_REASON, "cancelReason.equals=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllOrdersByCancelReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelReason in
        defaultOrderFiltering(
            "cancelReason.in=" + DEFAULT_CANCEL_REASON + "," + UPDATED_CANCEL_REASON,
            "cancelReason.in=" + UPDATED_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllOrdersByCancelReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelReason is not null
        defaultOrderFiltering("cancelReason.specified=true", "cancelReason.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByCancelReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelReason contains
        defaultOrderFiltering("cancelReason.contains=" + DEFAULT_CANCEL_REASON, "cancelReason.contains=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllOrdersByCancelReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where cancelReason does not contain
        defaultOrderFiltering(
            "cancelReason.doesNotContain=" + UPDATED_CANCEL_REASON,
            "cancelReason.doesNotContain=" + DEFAULT_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllOrdersByReturnReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where returnReason equals to
        defaultOrderFiltering("returnReason.equals=" + DEFAULT_RETURN_REASON, "returnReason.equals=" + UPDATED_RETURN_REASON);
    }

    @Test
    @Transactional
    void getAllOrdersByReturnReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where returnReason in
        defaultOrderFiltering(
            "returnReason.in=" + DEFAULT_RETURN_REASON + "," + UPDATED_RETURN_REASON,
            "returnReason.in=" + UPDATED_RETURN_REASON
        );
    }

    @Test
    @Transactional
    void getAllOrdersByReturnReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where returnReason is not null
        defaultOrderFiltering("returnReason.specified=true", "returnReason.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByReturnReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where returnReason contains
        defaultOrderFiltering("returnReason.contains=" + DEFAULT_RETURN_REASON, "returnReason.contains=" + UPDATED_RETURN_REASON);
    }

    @Test
    @Transactional
    void getAllOrdersByReturnReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where returnReason does not contain
        defaultOrderFiltering(
            "returnReason.doesNotContain=" + UPDATED_RETURN_REASON,
            "returnReason.doesNotContain=" + DEFAULT_RETURN_REASON
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount equals to
        defaultOrderFiltering("refundAmount.equals=" + DEFAULT_REFUND_AMOUNT, "refundAmount.equals=" + UPDATED_REFUND_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount in
        defaultOrderFiltering(
            "refundAmount.in=" + DEFAULT_REFUND_AMOUNT + "," + UPDATED_REFUND_AMOUNT,
            "refundAmount.in=" + UPDATED_REFUND_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount is not null
        defaultOrderFiltering("refundAmount.specified=true", "refundAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount is greater than or equal to
        defaultOrderFiltering(
            "refundAmount.greaterThanOrEqual=" + DEFAULT_REFUND_AMOUNT,
            "refundAmount.greaterThanOrEqual=" + UPDATED_REFUND_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount is less than or equal to
        defaultOrderFiltering(
            "refundAmount.lessThanOrEqual=" + DEFAULT_REFUND_AMOUNT,
            "refundAmount.lessThanOrEqual=" + SMALLER_REFUND_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount is less than
        defaultOrderFiltering("refundAmount.lessThan=" + UPDATED_REFUND_AMOUNT, "refundAmount.lessThan=" + DEFAULT_REFUND_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByRefundAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where refundAmount is greater than
        defaultOrderFiltering("refundAmount.greaterThan=" + SMALLER_REFUND_AMOUNT, "refundAmount.greaterThan=" + DEFAULT_REFUND_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate equals to
        defaultOrderFiltering(
            "estimatedDeliveryDate.equals=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.equals=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate in
        defaultOrderFiltering(
            "estimatedDeliveryDate.in=" + DEFAULT_ESTIMATED_DELIVERY_DATE + "," + UPDATED_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.in=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate is not null
        defaultOrderFiltering("estimatedDeliveryDate.specified=true", "estimatedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate is greater than or equal to
        defaultOrderFiltering(
            "estimatedDeliveryDate.greaterThanOrEqual=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.greaterThanOrEqual=" + UPDATED_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate is less than or equal to
        defaultOrderFiltering(
            "estimatedDeliveryDate.lessThanOrEqual=" + DEFAULT_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.lessThanOrEqual=" + SMALLER_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate is less than
        defaultOrderFiltering(
            "estimatedDeliveryDate.lessThan=" + UPDATED_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.lessThan=" + DEFAULT_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByEstimatedDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where estimatedDeliveryDate is greater than
        defaultOrderFiltering(
            "estimatedDeliveryDate.greaterThan=" + SMALLER_ESTIMATED_DELIVERY_DATE,
            "estimatedDeliveryDate.greaterThan=" + DEFAULT_ESTIMATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate equals to
        defaultOrderFiltering("deliveredDate.equals=" + DEFAULT_DELIVERED_DATE, "deliveredDate.equals=" + UPDATED_DELIVERED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate in
        defaultOrderFiltering(
            "deliveredDate.in=" + DEFAULT_DELIVERED_DATE + "," + UPDATED_DELIVERED_DATE,
            "deliveredDate.in=" + UPDATED_DELIVERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate is not null
        defaultOrderFiltering("deliveredDate.specified=true", "deliveredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate is greater than or equal to
        defaultOrderFiltering(
            "deliveredDate.greaterThanOrEqual=" + DEFAULT_DELIVERED_DATE,
            "deliveredDate.greaterThanOrEqual=" + UPDATED_DELIVERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate is less than or equal to
        defaultOrderFiltering(
            "deliveredDate.lessThanOrEqual=" + DEFAULT_DELIVERED_DATE,
            "deliveredDate.lessThanOrEqual=" + SMALLER_DELIVERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate is less than
        defaultOrderFiltering("deliveredDate.lessThan=" + UPDATED_DELIVERED_DATE, "deliveredDate.lessThan=" + DEFAULT_DELIVERED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveredDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveredDate is greater than
        defaultOrderFiltering("deliveredDate.greaterThan=" + SMALLER_DELIVERED_DATE, "deliveredDate.greaterThan=" + DEFAULT_DELIVERED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate equals to
        defaultOrderFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate in
        defaultOrderFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate is not null
        defaultOrderFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate is greater than or equal to
        defaultOrderFiltering(
            "shippedDate.greaterThanOrEqual=" + DEFAULT_SHIPPED_DATE,
            "shippedDate.greaterThanOrEqual=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate is less than or equal to
        defaultOrderFiltering("shippedDate.lessThanOrEqual=" + DEFAULT_SHIPPED_DATE, "shippedDate.lessThanOrEqual=" + SMALLER_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate is less than
        defaultOrderFiltering("shippedDate.lessThan=" + UPDATED_SHIPPED_DATE, "shippedDate.lessThan=" + DEFAULT_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByShippedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        // Get all the orderList where shippedDate is greater than
        defaultOrderFiltering("shippedDate.greaterThan=" + SMALLER_SHIPPED_DATE, "shippedDate.greaterThan=" + DEFAULT_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAddressIsEqualToSomething() throws Exception {
        UserAddress shippingAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            shippingAddress = UserAddressResourceIT.createEntity(em);
        } else {
            shippingAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        em.persist(shippingAddress);
        em.flush();
        order.setShippingAddress(shippingAddress);
        orderRepository.saveAndFlush(order);
        Long shippingAddressId = shippingAddress.getId();
        // Get all the orderList where shippingAddress equals to shippingAddressId
        defaultOrderShouldBeFound("shippingAddressId.equals=" + shippingAddressId);

        // Get all the orderList where shippingAddress equals to (shippingAddressId + 1)
        defaultOrderShouldNotBeFound("shippingAddressId.equals=" + (shippingAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByBillingAddressIsEqualToSomething() throws Exception {
        UserAddress billingAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            billingAddress = UserAddressResourceIT.createEntity(em);
        } else {
            billingAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        em.persist(billingAddress);
        em.flush();
        order.setBillingAddress(billingAddress);
        orderRepository.saveAndFlush(order);
        Long billingAddressId = billingAddress.getId();
        // Get all the orderList where billingAddress equals to billingAddressId
        defaultOrderShouldBeFound("billingAddressId.equals=" + billingAddressId);

        // Get all the orderList where billingAddress equals to (billingAddressId + 1)
        defaultOrderShouldNotBeFound("billingAddressId.equals=" + (billingAddressId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            user = UserProfileResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        order.setUser(user);
        orderRepository.saveAndFlush(order);
        Long userId = user.getId();
        // Get all the orderList where user equals to userId
        defaultOrderShouldBeFound("userId.equals=" + userId);

        // Get all the orderList where user equals to (userId + 1)
        defaultOrderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrderShouldBeFound(shouldBeFound);
        defaultOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].subtotalAmount").value(hasItem(sameNumber(DEFAULT_SUBTOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].shippingAmount").value(hasItem(sameNumber(DEFAULT_SHIPPING_AMOUNT))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].shippingMethod").value(hasItem(DEFAULT_SHIPPING_METHOD)))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)))
            .andExpect(jsonPath("$.[*].returnReason").value(hasItem(DEFAULT_RETURN_REASON)))
            .andExpect(jsonPath("$.[*].refundAmount").value(hasItem(sameNumber(DEFAULT_REFUND_AMOUNT))))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveredDate").value(hasItem(sameInstant(DEFAULT_DELIVERED_DATE))))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(sameInstant(DEFAULT_SHIPPED_DATE))));

        // Check, that the count call also returns 1
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subtotalAmount(UPDATED_SUBTOTAL_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .shippingMethod(UPDATED_SHIPPING_METHOD)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .notes(UPDATED_NOTES)
            .cancelReason(UPDATED_CANCEL_REASON)
            .returnReason(UPDATED_RETURN_REASON)
            .refundAmount(UPDATED_REFUND_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .deliveredDate(UPDATED_DELIVERED_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderToMatchAllProperties(updatedOrder);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subtotalAmount(UPDATED_SUBTOTAL_AMOUNT)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .notes(UPDATED_NOTES)
            .returnReason(UPDATED_RETURN_REASON)
            .refundAmount(UPDATED_REFUND_AMOUNT)
            .shippedDate(UPDATED_SHIPPED_DATE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrder, order), getPersistedOrder(order));
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderNumber(UPDATED_ORDER_NUMBER)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .subtotalAmount(UPDATED_SUBTOTAL_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .shippingAmount(UPDATED_SHIPPING_AMOUNT)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .shippingMethod(UPDATED_SHIPPING_METHOD)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .notes(UPDATED_NOTES)
            .cancelReason(UPDATED_CANCEL_REASON)
            .returnReason(UPDATED_RETURN_REASON)
            .refundAmount(UPDATED_REFUND_AMOUNT)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .deliveredDate(UPDATED_DELIVERED_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUpdatableFieldsEquals(partialUpdatedOrder, getPersistedOrder(partialUpdatedOrder));
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order.setId(longCount.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        insertedOrder = orderRepository.saveAndFlush(order);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Order getPersistedOrder(Order order) {
        return orderRepository.findById(order.getId()).orElseThrow();
    }

    protected void assertPersistedOrderToMatchAllProperties(Order expectedOrder) {
        assertOrderAllPropertiesEquals(expectedOrder, getPersistedOrder(expectedOrder));
    }

    protected void assertPersistedOrderToMatchUpdatableProperties(Order expectedOrder) {
        assertOrderAllUpdatablePropertiesEquals(expectedOrder, getPersistedOrder(expectedOrder));
    }
}
