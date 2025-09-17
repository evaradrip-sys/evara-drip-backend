package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.ShippingAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Order;
import com.evaradrip.commerce.domain.Shipping;
import com.evaradrip.commerce.domain.enumeration.ShippingStatus;
import com.evaradrip.commerce.repository.ShippingRepository;
import com.evaradrip.commerce.service.dto.ShippingDTO;
import com.evaradrip.commerce.service.mapper.ShippingMapper;
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
 * Integration tests for the {@link ShippingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingResourceIT {

    private static final String DEFAULT_CARRIER = "AAAAAAAAAA";
    private static final String UPDATED_CARRIER = "BBBBBBBBBB";

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESTIMATED_DELIVERY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_DELIVERY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ESTIMATED_DELIVERY = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_ACTUAL_DELIVERY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTUAL_DELIVERY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ACTUAL_DELIVERY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_SHIPPING_COST = new BigDecimal(1);
    private static final BigDecimal SMALLER_SHIPPING_COST = new BigDecimal(0 - 1);

    private static final ShippingStatus DEFAULT_STATUS = ShippingStatus.PENDING;
    private static final ShippingStatus UPDATED_STATUS = ShippingStatus.PROCESSING;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shippings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingMockMvc;

    private Shipping shipping;

    private Shipping insertedShipping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createEntity() {
        return new Shipping()
            .carrier(DEFAULT_CARRIER)
            .trackingNumber(DEFAULT_TRACKING_NUMBER)
            .estimatedDelivery(DEFAULT_ESTIMATED_DELIVERY)
            .actualDelivery(DEFAULT_ACTUAL_DELIVERY)
            .shippingCost(DEFAULT_SHIPPING_COST)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createUpdatedEntity() {
        return new Shipping()
            .carrier(UPDATED_CARRIER)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .estimatedDelivery(UPDATED_ESTIMATED_DELIVERY)
            .actualDelivery(UPDATED_ACTUAL_DELIVERY)
            .shippingCost(UPDATED_SHIPPING_COST)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        shipping = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShipping != null) {
            shippingRepository.delete(insertedShipping);
            insertedShipping = null;
        }
    }

    @Test
    @Transactional
    void createShipping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);
        var returnedShippingDTO = om.readValue(
            restShippingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShippingDTO.class
        );

        // Validate the Shipping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipping = shippingMapper.toEntity(returnedShippingDTO);
        assertShippingUpdatableFieldsEquals(returnedShipping, getPersistedShipping(returnedShipping));

        insertedShipping = returnedShipping;
    }

    @Test
    @Transactional
    void createShippingWithExistingId() throws Exception {
        // Create the Shipping with an existing ID
        shipping.setId(1L);
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCarrierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipping.setCarrier(null);

        // Create the Shipping, which fails.
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        restShippingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShippings() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipping.getId().intValue())))
            .andExpect(jsonPath("$.[*].carrier").value(hasItem(DEFAULT_CARRIER)))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].estimatedDelivery").value(hasItem(DEFAULT_ESTIMATED_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].actualDelivery").value(hasItem(sameInstant(DEFAULT_ACTUAL_DELIVERY))))
            .andExpect(jsonPath("$.[*].shippingCost").value(hasItem(sameNumber(DEFAULT_SHIPPING_COST))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get the shipping
        restShippingMockMvc
            .perform(get(ENTITY_API_URL_ID, shipping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipping.getId().intValue()))
            .andExpect(jsonPath("$.carrier").value(DEFAULT_CARRIER))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.estimatedDelivery").value(DEFAULT_ESTIMATED_DELIVERY.toString()))
            .andExpect(jsonPath("$.actualDelivery").value(sameInstant(DEFAULT_ACTUAL_DELIVERY)))
            .andExpect(jsonPath("$.shippingCost").value(sameNumber(DEFAULT_SHIPPING_COST)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getShippingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        Long id = shipping.getId();

        defaultShippingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShippingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShippingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShippingsByCarrierIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where carrier equals to
        defaultShippingFiltering("carrier.equals=" + DEFAULT_CARRIER, "carrier.equals=" + UPDATED_CARRIER);
    }

    @Test
    @Transactional
    void getAllShippingsByCarrierIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where carrier in
        defaultShippingFiltering("carrier.in=" + DEFAULT_CARRIER + "," + UPDATED_CARRIER, "carrier.in=" + UPDATED_CARRIER);
    }

    @Test
    @Transactional
    void getAllShippingsByCarrierIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where carrier is not null
        defaultShippingFiltering("carrier.specified=true", "carrier.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByCarrierContainsSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where carrier contains
        defaultShippingFiltering("carrier.contains=" + DEFAULT_CARRIER, "carrier.contains=" + UPDATED_CARRIER);
    }

    @Test
    @Transactional
    void getAllShippingsByCarrierNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where carrier does not contain
        defaultShippingFiltering("carrier.doesNotContain=" + UPDATED_CARRIER, "carrier.doesNotContain=" + DEFAULT_CARRIER);
    }

    @Test
    @Transactional
    void getAllShippingsByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where trackingNumber equals to
        defaultShippingFiltering("trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER, "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER);
    }

    @Test
    @Transactional
    void getAllShippingsByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where trackingNumber in
        defaultShippingFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShippingsByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where trackingNumber is not null
        defaultShippingFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where trackingNumber contains
        defaultShippingFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShippingsByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where trackingNumber does not contain
        defaultShippingFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery equals to
        defaultShippingFiltering(
            "estimatedDelivery.equals=" + DEFAULT_ESTIMATED_DELIVERY,
            "estimatedDelivery.equals=" + UPDATED_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery in
        defaultShippingFiltering(
            "estimatedDelivery.in=" + DEFAULT_ESTIMATED_DELIVERY + "," + UPDATED_ESTIMATED_DELIVERY,
            "estimatedDelivery.in=" + UPDATED_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery is not null
        defaultShippingFiltering("estimatedDelivery.specified=true", "estimatedDelivery.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery is greater than or equal to
        defaultShippingFiltering(
            "estimatedDelivery.greaterThanOrEqual=" + DEFAULT_ESTIMATED_DELIVERY,
            "estimatedDelivery.greaterThanOrEqual=" + UPDATED_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery is less than or equal to
        defaultShippingFiltering(
            "estimatedDelivery.lessThanOrEqual=" + DEFAULT_ESTIMATED_DELIVERY,
            "estimatedDelivery.lessThanOrEqual=" + SMALLER_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery is less than
        defaultShippingFiltering(
            "estimatedDelivery.lessThan=" + UPDATED_ESTIMATED_DELIVERY,
            "estimatedDelivery.lessThan=" + DEFAULT_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByEstimatedDeliveryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where estimatedDelivery is greater than
        defaultShippingFiltering(
            "estimatedDelivery.greaterThan=" + SMALLER_ESTIMATED_DELIVERY,
            "estimatedDelivery.greaterThan=" + DEFAULT_ESTIMATED_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery equals to
        defaultShippingFiltering("actualDelivery.equals=" + DEFAULT_ACTUAL_DELIVERY, "actualDelivery.equals=" + UPDATED_ACTUAL_DELIVERY);
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery in
        defaultShippingFiltering(
            "actualDelivery.in=" + DEFAULT_ACTUAL_DELIVERY + "," + UPDATED_ACTUAL_DELIVERY,
            "actualDelivery.in=" + UPDATED_ACTUAL_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery is not null
        defaultShippingFiltering("actualDelivery.specified=true", "actualDelivery.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery is greater than or equal to
        defaultShippingFiltering(
            "actualDelivery.greaterThanOrEqual=" + DEFAULT_ACTUAL_DELIVERY,
            "actualDelivery.greaterThanOrEqual=" + UPDATED_ACTUAL_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery is less than or equal to
        defaultShippingFiltering(
            "actualDelivery.lessThanOrEqual=" + DEFAULT_ACTUAL_DELIVERY,
            "actualDelivery.lessThanOrEqual=" + SMALLER_ACTUAL_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery is less than
        defaultShippingFiltering(
            "actualDelivery.lessThan=" + UPDATED_ACTUAL_DELIVERY,
            "actualDelivery.lessThan=" + DEFAULT_ACTUAL_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByActualDeliveryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where actualDelivery is greater than
        defaultShippingFiltering(
            "actualDelivery.greaterThan=" + SMALLER_ACTUAL_DELIVERY,
            "actualDelivery.greaterThan=" + DEFAULT_ACTUAL_DELIVERY
        );
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost equals to
        defaultShippingFiltering("shippingCost.equals=" + DEFAULT_SHIPPING_COST, "shippingCost.equals=" + UPDATED_SHIPPING_COST);
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost in
        defaultShippingFiltering(
            "shippingCost.in=" + DEFAULT_SHIPPING_COST + "," + UPDATED_SHIPPING_COST,
            "shippingCost.in=" + UPDATED_SHIPPING_COST
        );
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost is not null
        defaultShippingFiltering("shippingCost.specified=true", "shippingCost.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost is greater than or equal to
        defaultShippingFiltering(
            "shippingCost.greaterThanOrEqual=" + DEFAULT_SHIPPING_COST,
            "shippingCost.greaterThanOrEqual=" + UPDATED_SHIPPING_COST
        );
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost is less than or equal to
        defaultShippingFiltering(
            "shippingCost.lessThanOrEqual=" + DEFAULT_SHIPPING_COST,
            "shippingCost.lessThanOrEqual=" + SMALLER_SHIPPING_COST
        );
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost is less than
        defaultShippingFiltering("shippingCost.lessThan=" + UPDATED_SHIPPING_COST, "shippingCost.lessThan=" + DEFAULT_SHIPPING_COST);
    }

    @Test
    @Transactional
    void getAllShippingsByShippingCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where shippingCost is greater than
        defaultShippingFiltering("shippingCost.greaterThan=" + SMALLER_SHIPPING_COST, "shippingCost.greaterThan=" + DEFAULT_SHIPPING_COST);
    }

    @Test
    @Transactional
    void getAllShippingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where status equals to
        defaultShippingFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where status in
        defaultShippingFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList where status is not null
        defaultShippingFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            shippingRepository.saveAndFlush(shipping);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        shipping.setOrder(order);
        shippingRepository.saveAndFlush(shipping);
        Long orderId = order.getId();
        // Get all the shippingList where order equals to orderId
        defaultShippingShouldBeFound("orderId.equals=" + orderId);

        // Get all the shippingList where order equals to (orderId + 1)
        defaultShippingShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    private void defaultShippingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShippingShouldBeFound(shouldBeFound);
        defaultShippingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShippingShouldBeFound(String filter) throws Exception {
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipping.getId().intValue())))
            .andExpect(jsonPath("$.[*].carrier").value(hasItem(DEFAULT_CARRIER)))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].estimatedDelivery").value(hasItem(DEFAULT_ESTIMATED_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].actualDelivery").value(hasItem(sameInstant(DEFAULT_ACTUAL_DELIVERY))))
            .andExpect(jsonPath("$.[*].shippingCost").value(hasItem(sameNumber(DEFAULT_SHIPPING_COST))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShippingShouldNotBeFound(String filter) throws Exception {
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipping() throws Exception {
        // Get the shipping
        restShippingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping
        Shipping updatedShipping = shippingRepository.findById(shipping.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipping are not directly saved in db
        em.detach(updatedShipping);
        updatedShipping
            .carrier(UPDATED_CARRIER)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .estimatedDelivery(UPDATED_ESTIMATED_DELIVERY)
            .actualDelivery(UPDATED_ACTUAL_DELIVERY)
            .shippingCost(UPDATED_SHIPPING_COST)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        ShippingDTO shippingDTO = shippingMapper.toDto(updatedShipping);

        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShippingToMatchAllProperties(updatedShipping);
    }

    @Test
    @Transactional
    void putNonExistingShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping
            .carrier(UPDATED_CARRIER)
            .estimatedDelivery(UPDATED_ESTIMATED_DELIVERY)
            .shippingCost(UPDATED_SHIPPING_COST)
            .status(UPDATED_STATUS);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShippingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShipping, shipping), getPersistedShipping(shipping));
    }

    @Test
    @Transactional
    void fullUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping
            .carrier(UPDATED_CARRIER)
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .estimatedDelivery(UPDATED_ESTIMATED_DELIVERY)
            .actualDelivery(UPDATED_ACTUAL_DELIVERY)
            .shippingCost(UPDATED_SHIPPING_COST)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShippingUpdatableFieldsEquals(partialUpdatedShipping, getPersistedShipping(partialUpdatedShipping));
    }

    @Test
    @Transactional
    void patchNonExistingShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipping
        restShippingMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shippingRepository.count();
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

    protected Shipping getPersistedShipping(Shipping shipping) {
        return shippingRepository.findById(shipping.getId()).orElseThrow();
    }

    protected void assertPersistedShippingToMatchAllProperties(Shipping expectedShipping) {
        assertShippingAllPropertiesEquals(expectedShipping, getPersistedShipping(expectedShipping));
    }

    protected void assertPersistedShippingToMatchUpdatableProperties(Shipping expectedShipping) {
        assertShippingAllUpdatablePropertiesEquals(expectedShipping, getPersistedShipping(expectedShipping));
    }
}
