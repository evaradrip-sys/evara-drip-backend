package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.CouponAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Coupon;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.domain.enumeration.DiscountType;
import com.evaradrip.commerce.repository.CouponRepository;
import com.evaradrip.commerce.service.dto.CouponDTO;
import com.evaradrip.commerce.service.mapper.CouponMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link CouponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CouponResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.FIXED_AMOUNT;

    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISCOUNT_VALUE = new BigDecimal(0 - 1);

    private static final ZonedDateTime DEFAULT_VALID_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALID_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_VALID_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_VALID_UNTIL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALID_UNTIL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_VALID_UNTIL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_MAX_USES = 1;
    private static final Integer UPDATED_MAX_USES = 2;
    private static final Integer SMALLER_MAX_USES = 1 - 1;

    private static final Integer DEFAULT_CURRENT_USES = 0;
    private static final Integer UPDATED_CURRENT_USES = 1;
    private static final Integer SMALLER_CURRENT_USES = 0 - 1;

    private static final BigDecimal DEFAULT_MIN_ORDER_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MIN_ORDER_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_MIN_ORDER_VALUE = new BigDecimal(0 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/coupons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponMockMvc;

    private Coupon coupon;

    private Coupon insertedCoupon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createEntity() {
        return new Coupon()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .validFrom(DEFAULT_VALID_FROM)
            .validUntil(DEFAULT_VALID_UNTIL)
            .maxUses(DEFAULT_MAX_USES)
            .currentUses(DEFAULT_CURRENT_USES)
            .minOrderValue(DEFAULT_MIN_ORDER_VALUE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createUpdatedEntity() {
        return new Coupon()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .maxUses(UPDATED_MAX_USES)
            .currentUses(UPDATED_CURRENT_USES)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        coupon = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCoupon != null) {
            couponRepository.delete(insertedCoupon);
            insertedCoupon = null;
        }
    }

    @Test
    @Transactional
    void createCoupon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);
        var returnedCouponDTO = om.readValue(
            restCouponMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CouponDTO.class
        );

        // Validate the Coupon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoupon = couponMapper.toEntity(returnedCouponDTO);
        assertCouponUpdatableFieldsEquals(returnedCoupon, getPersistedCoupon(returnedCoupon));

        insertedCoupon = returnedCoupon;
    }

    @Test
    @Transactional
    void createCouponWithExistingId() throws Exception {
        // Create the Coupon with an existing ID
        coupon.setId(1L);
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setCode(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setDiscountType(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setDiscountValue(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setValidFrom(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidUntilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setValidUntil(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoupons() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(sameInstant(DEFAULT_VALID_FROM))))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(sameInstant(DEFAULT_VALID_UNTIL))))
            .andExpect(jsonPath("$.[*].maxUses").value(hasItem(DEFAULT_MAX_USES)))
            .andExpect(jsonPath("$.[*].currentUses").value(hasItem(DEFAULT_CURRENT_USES)))
            .andExpect(jsonPath("$.[*].minOrderValue").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_VALUE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get the coupon
        restCouponMockMvc
            .perform(get(ENTITY_API_URL_ID, coupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coupon.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountValue").value(sameNumber(DEFAULT_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.validFrom").value(sameInstant(DEFAULT_VALID_FROM)))
            .andExpect(jsonPath("$.validUntil").value(sameInstant(DEFAULT_VALID_UNTIL)))
            .andExpect(jsonPath("$.maxUses").value(DEFAULT_MAX_USES))
            .andExpect(jsonPath("$.currentUses").value(DEFAULT_CURRENT_USES))
            .andExpect(jsonPath("$.minOrderValue").value(sameNumber(DEFAULT_MIN_ORDER_VALUE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getCouponsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        Long id = coupon.getId();

        defaultCouponFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCouponFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCouponFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code equals to
        defaultCouponFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code in
        defaultCouponFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code is not null
        defaultCouponFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code contains
        defaultCouponFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code does not contain
        defaultCouponFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType equals to
        defaultCouponFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType in
        defaultCouponFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType is not null
        defaultCouponFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue equals to
        defaultCouponFiltering("discountValue.equals=" + DEFAULT_DISCOUNT_VALUE, "discountValue.equals=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue in
        defaultCouponFiltering(
            "discountValue.in=" + DEFAULT_DISCOUNT_VALUE + "," + UPDATED_DISCOUNT_VALUE,
            "discountValue.in=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is not null
        defaultCouponFiltering("discountValue.specified=true", "discountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is greater than or equal to
        defaultCouponFiltering(
            "discountValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is less than or equal to
        defaultCouponFiltering(
            "discountValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.lessThanOrEqual=" + SMALLER_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is less than
        defaultCouponFiltering("discountValue.lessThan=" + UPDATED_DISCOUNT_VALUE, "discountValue.lessThan=" + DEFAULT_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is greater than
        defaultCouponFiltering(
            "discountValue.greaterThan=" + SMALLER_DISCOUNT_VALUE,
            "discountValue.greaterThan=" + DEFAULT_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom equals to
        defaultCouponFiltering("validFrom.equals=" + DEFAULT_VALID_FROM, "validFrom.equals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom in
        defaultCouponFiltering("validFrom.in=" + DEFAULT_VALID_FROM + "," + UPDATED_VALID_FROM, "validFrom.in=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is not null
        defaultCouponFiltering("validFrom.specified=true", "validFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is greater than or equal to
        defaultCouponFiltering("validFrom.greaterThanOrEqual=" + DEFAULT_VALID_FROM, "validFrom.greaterThanOrEqual=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is less than or equal to
        defaultCouponFiltering("validFrom.lessThanOrEqual=" + DEFAULT_VALID_FROM, "validFrom.lessThanOrEqual=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is less than
        defaultCouponFiltering("validFrom.lessThan=" + UPDATED_VALID_FROM, "validFrom.lessThan=" + DEFAULT_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is greater than
        defaultCouponFiltering("validFrom.greaterThan=" + SMALLER_VALID_FROM, "validFrom.greaterThan=" + DEFAULT_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil equals to
        defaultCouponFiltering("validUntil.equals=" + DEFAULT_VALID_UNTIL, "validUntil.equals=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil in
        defaultCouponFiltering("validUntil.in=" + DEFAULT_VALID_UNTIL + "," + UPDATED_VALID_UNTIL, "validUntil.in=" + UPDATED_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil is not null
        defaultCouponFiltering("validUntil.specified=true", "validUntil.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil is greater than or equal to
        defaultCouponFiltering(
            "validUntil.greaterThanOrEqual=" + DEFAULT_VALID_UNTIL,
            "validUntil.greaterThanOrEqual=" + UPDATED_VALID_UNTIL
        );
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil is less than or equal to
        defaultCouponFiltering("validUntil.lessThanOrEqual=" + DEFAULT_VALID_UNTIL, "validUntil.lessThanOrEqual=" + SMALLER_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil is less than
        defaultCouponFiltering("validUntil.lessThan=" + UPDATED_VALID_UNTIL, "validUntil.lessThan=" + DEFAULT_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCouponsByValidUntilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validUntil is greater than
        defaultCouponFiltering("validUntil.greaterThan=" + SMALLER_VALID_UNTIL, "validUntil.greaterThan=" + DEFAULT_VALID_UNTIL);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses equals to
        defaultCouponFiltering("maxUses.equals=" + DEFAULT_MAX_USES, "maxUses.equals=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses in
        defaultCouponFiltering("maxUses.in=" + DEFAULT_MAX_USES + "," + UPDATED_MAX_USES, "maxUses.in=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses is not null
        defaultCouponFiltering("maxUses.specified=true", "maxUses.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses is greater than or equal to
        defaultCouponFiltering("maxUses.greaterThanOrEqual=" + DEFAULT_MAX_USES, "maxUses.greaterThanOrEqual=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses is less than or equal to
        defaultCouponFiltering("maxUses.lessThanOrEqual=" + DEFAULT_MAX_USES, "maxUses.lessThanOrEqual=" + SMALLER_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses is less than
        defaultCouponFiltering("maxUses.lessThan=" + UPDATED_MAX_USES, "maxUses.lessThan=" + DEFAULT_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxUsesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxUses is greater than
        defaultCouponFiltering("maxUses.greaterThan=" + SMALLER_MAX_USES, "maxUses.greaterThan=" + DEFAULT_MAX_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses equals to
        defaultCouponFiltering("currentUses.equals=" + DEFAULT_CURRENT_USES, "currentUses.equals=" + UPDATED_CURRENT_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses in
        defaultCouponFiltering(
            "currentUses.in=" + DEFAULT_CURRENT_USES + "," + UPDATED_CURRENT_USES,
            "currentUses.in=" + UPDATED_CURRENT_USES
        );
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses is not null
        defaultCouponFiltering("currentUses.specified=true", "currentUses.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses is greater than or equal to
        defaultCouponFiltering(
            "currentUses.greaterThanOrEqual=" + DEFAULT_CURRENT_USES,
            "currentUses.greaterThanOrEqual=" + UPDATED_CURRENT_USES
        );
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses is less than or equal to
        defaultCouponFiltering(
            "currentUses.lessThanOrEqual=" + DEFAULT_CURRENT_USES,
            "currentUses.lessThanOrEqual=" + SMALLER_CURRENT_USES
        );
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses is less than
        defaultCouponFiltering("currentUses.lessThan=" + UPDATED_CURRENT_USES, "currentUses.lessThan=" + DEFAULT_CURRENT_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByCurrentUsesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where currentUses is greater than
        defaultCouponFiltering("currentUses.greaterThan=" + SMALLER_CURRENT_USES, "currentUses.greaterThan=" + DEFAULT_CURRENT_USES);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue equals to
        defaultCouponFiltering("minOrderValue.equals=" + DEFAULT_MIN_ORDER_VALUE, "minOrderValue.equals=" + UPDATED_MIN_ORDER_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue in
        defaultCouponFiltering(
            "minOrderValue.in=" + DEFAULT_MIN_ORDER_VALUE + "," + UPDATED_MIN_ORDER_VALUE,
            "minOrderValue.in=" + UPDATED_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is not null
        defaultCouponFiltering("minOrderValue.specified=true", "minOrderValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is greater than or equal to
        defaultCouponFiltering(
            "minOrderValue.greaterThanOrEqual=" + DEFAULT_MIN_ORDER_VALUE,
            "minOrderValue.greaterThanOrEqual=" + UPDATED_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is less than or equal to
        defaultCouponFiltering(
            "minOrderValue.lessThanOrEqual=" + DEFAULT_MIN_ORDER_VALUE,
            "minOrderValue.lessThanOrEqual=" + SMALLER_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is less than
        defaultCouponFiltering("minOrderValue.lessThan=" + UPDATED_MIN_ORDER_VALUE, "minOrderValue.lessThan=" + DEFAULT_MIN_ORDER_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is greater than
        defaultCouponFiltering(
            "minOrderValue.greaterThan=" + SMALLER_MIN_ORDER_VALUE,
            "minOrderValue.greaterThan=" + DEFAULT_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where isActive equals to
        defaultCouponFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCouponsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where isActive in
        defaultCouponFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCouponsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where isActive is not null
        defaultCouponFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByUsersIsEqualToSomething() throws Exception {
        UserProfile users;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            couponRepository.saveAndFlush(coupon);
            users = UserProfileResourceIT.createEntity();
        } else {
            users = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(users);
        em.flush();
        coupon.addUsers(users);
        couponRepository.saveAndFlush(coupon);
        Long usersId = users.getId();
        // Get all the couponList where users equals to usersId
        defaultCouponShouldBeFound("usersId.equals=" + usersId);

        // Get all the couponList where users equals to (usersId + 1)
        defaultCouponShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    private void defaultCouponFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCouponShouldBeFound(shouldBeFound);
        defaultCouponShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCouponShouldBeFound(String filter) throws Exception {
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(sameInstant(DEFAULT_VALID_FROM))))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(sameInstant(DEFAULT_VALID_UNTIL))))
            .andExpect(jsonPath("$.[*].maxUses").value(hasItem(DEFAULT_MAX_USES)))
            .andExpect(jsonPath("$.[*].currentUses").value(hasItem(DEFAULT_CURRENT_USES)))
            .andExpect(jsonPath("$.[*].minOrderValue").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_VALUE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCouponShouldNotBeFound(String filter) throws Exception {
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCoupon() throws Exception {
        // Get the coupon
        restCouponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon
        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoupon are not directly saved in db
        em.detach(updatedCoupon);
        updatedCoupon
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .maxUses(UPDATED_MAX_USES)
            .currentUses(UPDATED_CURRENT_USES)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .isActive(UPDATED_IS_ACTIVE);
        CouponDTO couponDTO = couponMapper.toDto(updatedCoupon);

        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCouponToMatchAllProperties(updatedCoupon);
    }

    @Test
    @Transactional
    void putNonExistingCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .maxUses(UPDATED_MAX_USES)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .isActive(UPDATED_IS_ACTIVE);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCoupon, coupon), getPersistedCoupon(coupon));
    }

    @Test
    @Transactional
    void fullUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .validFrom(UPDATED_VALID_FROM)
            .validUntil(UPDATED_VALID_UNTIL)
            .maxUses(UPDATED_MAX_USES)
            .currentUses(UPDATED_CURRENT_USES)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .isActive(UPDATED_IS_ACTIVE);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponUpdatableFieldsEquals(partialUpdatedCoupon, getPersistedCoupon(partialUpdatedCoupon));
    }

    @Test
    @Transactional
    void patchNonExistingCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couponDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coupon
        restCouponMockMvc
            .perform(delete(ENTITY_API_URL_ID, coupon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return couponRepository.count();
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

    protected Coupon getPersistedCoupon(Coupon coupon) {
        return couponRepository.findById(coupon.getId()).orElseThrow();
    }

    protected void assertPersistedCouponToMatchAllProperties(Coupon expectedCoupon) {
        assertCouponAllPropertiesEquals(expectedCoupon, getPersistedCoupon(expectedCoupon));
    }

    protected void assertPersistedCouponToMatchUpdatableProperties(Coupon expectedCoupon) {
        assertCouponAllUpdatablePropertiesEquals(expectedCoupon, getPersistedCoupon(expectedCoupon));
    }
}
