package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.PromotionAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.domain.enumeration.DiscountType;
import com.evaradrip.commerce.repository.PromotionRepository;
import com.evaradrip.commerce.service.PromotionService;
import com.evaradrip.commerce.service.dto.PromotionDTO;
import com.evaradrip.commerce.service.mapper.PromotionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PromotionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromotionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROMO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROMO_CODE = "BBBBBBBBBB";

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.FIXED_AMOUNT;

    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISCOUNT_VALUE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_MIN_PURCHASE_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MIN_PURCHASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_MIN_PURCHASE_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_MAX_DISCOUNT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MAX_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_MAX_DISCOUNT_AMOUNT = new BigDecimal(0 - 1);

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_USAGE_LIMIT = 0;
    private static final Integer UPDATED_USAGE_LIMIT = 1;
    private static final Integer SMALLER_USAGE_LIMIT = 0 - 1;

    private static final Integer DEFAULT_USAGE_COUNT = 0;
    private static final Integer UPDATED_USAGE_COUNT = 1;
    private static final Integer SMALLER_USAGE_COUNT = 0 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_APPLICABLE_CATEGORIES = "AAAAAAAAAA";
    private static final String UPDATED_APPLICABLE_CATEGORIES = "BBBBBBBBBB";

    private static final String DEFAULT_EXCLUDED_PRODUCTS = "AAAAAAAAAA";
    private static final String UPDATED_EXCLUDED_PRODUCTS = "BBBBBBBBBB";

    private static final String DEFAULT_TERMS_AND_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_TERMS_AND_CONDITIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionRepository promotionRepositoryMock;

    @Autowired
    private PromotionMapper promotionMapper;

    @Mock
    private PromotionService promotionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotionMockMvc;

    private Promotion promotion;

    private Promotion insertedPromotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createEntity() {
        return new Promotion()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .promoCode(DEFAULT_PROMO_CODE)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .minPurchaseAmount(DEFAULT_MIN_PURCHASE_AMOUNT)
            .maxDiscountAmount(DEFAULT_MAX_DISCOUNT_AMOUNT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .usageLimit(DEFAULT_USAGE_LIMIT)
            .usageCount(DEFAULT_USAGE_COUNT)
            .isActive(DEFAULT_IS_ACTIVE)
            .applicableCategories(DEFAULT_APPLICABLE_CATEGORIES)
            .excludedProducts(DEFAULT_EXCLUDED_PRODUCTS)
            .termsAndConditions(DEFAULT_TERMS_AND_CONDITIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createUpdatedEntity() {
        return new Promotion()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .promoCode(UPDATED_PROMO_CODE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .minPurchaseAmount(UPDATED_MIN_PURCHASE_AMOUNT)
            .maxDiscountAmount(UPDATED_MAX_DISCOUNT_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .usageLimit(UPDATED_USAGE_LIMIT)
            .usageCount(UPDATED_USAGE_COUNT)
            .isActive(UPDATED_IS_ACTIVE)
            .applicableCategories(UPDATED_APPLICABLE_CATEGORIES)
            .excludedProducts(UPDATED_EXCLUDED_PRODUCTS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);
    }

    @BeforeEach
    void initTest() {
        promotion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPromotion != null) {
            promotionRepository.delete(insertedPromotion);
            insertedPromotion = null;
        }
    }

    @Test
    @Transactional
    void createPromotion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);
        var returnedPromotionDTO = om.readValue(
            restPromotionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PromotionDTO.class
        );

        // Validate the Promotion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPromotion = promotionMapper.toEntity(returnedPromotionDTO);
        assertPromotionUpdatableFieldsEquals(returnedPromotion, getPersistedPromotion(returnedPromotion));

        insertedPromotion = returnedPromotion;
    }

    @Test
    @Transactional
    void createPromotionWithExistingId() throws Exception {
        // Create the Promotion with an existing ID
        promotion.setId(1L);
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        promotion.setName(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        promotion.setDiscountType(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        promotion.setDiscountValue(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        promotion.setStartDate(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        promotion.setEndDate(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromotions() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].promoCode").value(hasItem(DEFAULT_PROMO_CODE)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].minPurchaseAmount").value(hasItem(sameNumber(DEFAULT_MIN_PURCHASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxDiscountAmount").value(hasItem(sameNumber(DEFAULT_MAX_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].usageLimit").value(hasItem(DEFAULT_USAGE_LIMIT)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].applicableCategories").value(hasItem(DEFAULT_APPLICABLE_CATEGORIES)))
            .andExpect(jsonPath("$.[*].excludedProducts").value(hasItem(DEFAULT_EXCLUDED_PRODUCTS)))
            .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromotionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(promotionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromotionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promotionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromotionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(promotionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromotionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(promotionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPromotion() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get the promotion
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL_ID, promotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.promoCode").value(DEFAULT_PROMO_CODE))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountValue").value(sameNumber(DEFAULT_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.minPurchaseAmount").value(sameNumber(DEFAULT_MIN_PURCHASE_AMOUNT)))
            .andExpect(jsonPath("$.maxDiscountAmount").value(sameNumber(DEFAULT_MAX_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.usageLimit").value(DEFAULT_USAGE_LIMIT))
            .andExpect(jsonPath("$.usageCount").value(DEFAULT_USAGE_COUNT))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.applicableCategories").value(DEFAULT_APPLICABLE_CATEGORIES))
            .andExpect(jsonPath("$.excludedProducts").value(DEFAULT_EXCLUDED_PRODUCTS))
            .andExpect(jsonPath("$.termsAndConditions").value(DEFAULT_TERMS_AND_CONDITIONS));
    }

    @Test
    @Transactional
    void getPromotionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        Long id = promotion.getId();

        defaultPromotionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPromotionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPromotionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name equals to
        defaultPromotionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name in
        defaultPromotionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name is not null
        defaultPromotionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name contains
        defaultPromotionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name does not contain
        defaultPromotionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByPromoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where promoCode equals to
        defaultPromotionFiltering("promoCode.equals=" + DEFAULT_PROMO_CODE, "promoCode.equals=" + UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPromoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where promoCode in
        defaultPromotionFiltering("promoCode.in=" + DEFAULT_PROMO_CODE + "," + UPDATED_PROMO_CODE, "promoCode.in=" + UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPromoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where promoCode is not null
        defaultPromotionFiltering("promoCode.specified=true", "promoCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByPromoCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where promoCode contains
        defaultPromotionFiltering("promoCode.contains=" + DEFAULT_PROMO_CODE, "promoCode.contains=" + UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPromoCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where promoCode does not contain
        defaultPromotionFiltering("promoCode.doesNotContain=" + UPDATED_PROMO_CODE, "promoCode.doesNotContain=" + DEFAULT_PROMO_CODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountType equals to
        defaultPromotionFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountType in
        defaultPromotionFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountType is not null
        defaultPromotionFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue equals to
        defaultPromotionFiltering("discountValue.equals=" + DEFAULT_DISCOUNT_VALUE, "discountValue.equals=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue in
        defaultPromotionFiltering(
            "discountValue.in=" + DEFAULT_DISCOUNT_VALUE + "," + UPDATED_DISCOUNT_VALUE,
            "discountValue.in=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue is not null
        defaultPromotionFiltering("discountValue.specified=true", "discountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue is greater than or equal to
        defaultPromotionFiltering(
            "discountValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue is less than or equal to
        defaultPromotionFiltering(
            "discountValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.lessThanOrEqual=" + SMALLER_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue is less than
        defaultPromotionFiltering("discountValue.lessThan=" + UPDATED_DISCOUNT_VALUE, "discountValue.lessThan=" + DEFAULT_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllPromotionsByDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where discountValue is greater than
        defaultPromotionFiltering(
            "discountValue.greaterThan=" + SMALLER_DISCOUNT_VALUE,
            "discountValue.greaterThan=" + DEFAULT_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount equals to
        defaultPromotionFiltering(
            "minPurchaseAmount.equals=" + DEFAULT_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.equals=" + UPDATED_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount in
        defaultPromotionFiltering(
            "minPurchaseAmount.in=" + DEFAULT_MIN_PURCHASE_AMOUNT + "," + UPDATED_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.in=" + UPDATED_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount is not null
        defaultPromotionFiltering("minPurchaseAmount.specified=true", "minPurchaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount is greater than or equal to
        defaultPromotionFiltering(
            "minPurchaseAmount.greaterThanOrEqual=" + DEFAULT_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.greaterThanOrEqual=" + UPDATED_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount is less than or equal to
        defaultPromotionFiltering(
            "minPurchaseAmount.lessThanOrEqual=" + DEFAULT_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.lessThanOrEqual=" + SMALLER_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount is less than
        defaultPromotionFiltering(
            "minPurchaseAmount.lessThan=" + UPDATED_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.lessThan=" + DEFAULT_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMinPurchaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where minPurchaseAmount is greater than
        defaultPromotionFiltering(
            "minPurchaseAmount.greaterThan=" + SMALLER_MIN_PURCHASE_AMOUNT,
            "minPurchaseAmount.greaterThan=" + DEFAULT_MIN_PURCHASE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount equals to
        defaultPromotionFiltering(
            "maxDiscountAmount.equals=" + DEFAULT_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.equals=" + UPDATED_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount in
        defaultPromotionFiltering(
            "maxDiscountAmount.in=" + DEFAULT_MAX_DISCOUNT_AMOUNT + "," + UPDATED_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.in=" + UPDATED_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount is not null
        defaultPromotionFiltering("maxDiscountAmount.specified=true", "maxDiscountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount is greater than or equal to
        defaultPromotionFiltering(
            "maxDiscountAmount.greaterThanOrEqual=" + DEFAULT_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.greaterThanOrEqual=" + UPDATED_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount is less than or equal to
        defaultPromotionFiltering(
            "maxDiscountAmount.lessThanOrEqual=" + DEFAULT_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.lessThanOrEqual=" + SMALLER_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount is less than
        defaultPromotionFiltering(
            "maxDiscountAmount.lessThan=" + UPDATED_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.lessThan=" + DEFAULT_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByMaxDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where maxDiscountAmount is greater than
        defaultPromotionFiltering(
            "maxDiscountAmount.greaterThan=" + SMALLER_MAX_DISCOUNT_AMOUNT,
            "maxDiscountAmount.greaterThan=" + DEFAULT_MAX_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate equals to
        defaultPromotionFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate in
        defaultPromotionFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate is not null
        defaultPromotionFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate is greater than or equal to
        defaultPromotionFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate is less than or equal to
        defaultPromotionFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate is less than
        defaultPromotionFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startDate is greater than
        defaultPromotionFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate equals to
        defaultPromotionFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate in
        defaultPromotionFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate is not null
        defaultPromotionFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate is greater than or equal to
        defaultPromotionFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate is less than or equal to
        defaultPromotionFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate is less than
        defaultPromotionFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endDate is greater than
        defaultPromotionFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit equals to
        defaultPromotionFiltering("usageLimit.equals=" + DEFAULT_USAGE_LIMIT, "usageLimit.equals=" + UPDATED_USAGE_LIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit in
        defaultPromotionFiltering(
            "usageLimit.in=" + DEFAULT_USAGE_LIMIT + "," + UPDATED_USAGE_LIMIT,
            "usageLimit.in=" + UPDATED_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit is not null
        defaultPromotionFiltering("usageLimit.specified=true", "usageLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit is greater than or equal to
        defaultPromotionFiltering(
            "usageLimit.greaterThanOrEqual=" + DEFAULT_USAGE_LIMIT,
            "usageLimit.greaterThanOrEqual=" + UPDATED_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit is less than or equal to
        defaultPromotionFiltering("usageLimit.lessThanOrEqual=" + DEFAULT_USAGE_LIMIT, "usageLimit.lessThanOrEqual=" + SMALLER_USAGE_LIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit is less than
        defaultPromotionFiltering("usageLimit.lessThan=" + UPDATED_USAGE_LIMIT, "usageLimit.lessThan=" + DEFAULT_USAGE_LIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageLimit is greater than
        defaultPromotionFiltering("usageLimit.greaterThan=" + SMALLER_USAGE_LIMIT, "usageLimit.greaterThan=" + DEFAULT_USAGE_LIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount equals to
        defaultPromotionFiltering("usageCount.equals=" + DEFAULT_USAGE_COUNT, "usageCount.equals=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount in
        defaultPromotionFiltering(
            "usageCount.in=" + DEFAULT_USAGE_COUNT + "," + UPDATED_USAGE_COUNT,
            "usageCount.in=" + UPDATED_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount is not null
        defaultPromotionFiltering("usageCount.specified=true", "usageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount is greater than or equal to
        defaultPromotionFiltering(
            "usageCount.greaterThanOrEqual=" + DEFAULT_USAGE_COUNT,
            "usageCount.greaterThanOrEqual=" + UPDATED_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount is less than or equal to
        defaultPromotionFiltering("usageCount.lessThanOrEqual=" + DEFAULT_USAGE_COUNT, "usageCount.lessThanOrEqual=" + SMALLER_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount is less than
        defaultPromotionFiltering("usageCount.lessThan=" + UPDATED_USAGE_COUNT, "usageCount.lessThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUsageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where usageCount is greater than
        defaultPromotionFiltering("usageCount.greaterThan=" + SMALLER_USAGE_COUNT, "usageCount.greaterThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllPromotionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where isActive equals to
        defaultPromotionFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPromotionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where isActive in
        defaultPromotionFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPromotionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where isActive is not null
        defaultPromotionFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByApplicableProductsIsEqualToSomething() throws Exception {
        Product applicableProducts;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            promotionRepository.saveAndFlush(promotion);
            applicableProducts = ProductResourceIT.createEntity(em);
        } else {
            applicableProducts = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(applicableProducts);
        em.flush();
        promotion.addApplicableProducts(applicableProducts);
        promotionRepository.saveAndFlush(promotion);
        Long applicableProductsId = applicableProducts.getId();
        // Get all the promotionList where applicableProducts equals to applicableProductsId
        defaultPromotionShouldBeFound("applicableProductsId.equals=" + applicableProductsId);

        // Get all the promotionList where applicableProducts equals to (applicableProductsId + 1)
        defaultPromotionShouldNotBeFound("applicableProductsId.equals=" + (applicableProductsId + 1));
    }

    @Test
    @Transactional
    void getAllPromotionsByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            promotionRepository.saveAndFlush(promotion);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        promotion.addProducts(products);
        promotionRepository.saveAndFlush(promotion);
        Long productsId = products.getId();
        // Get all the promotionList where products equals to productsId
        defaultPromotionShouldBeFound("productsId.equals=" + productsId);

        // Get all the promotionList where products equals to (productsId + 1)
        defaultPromotionShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    private void defaultPromotionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPromotionShouldBeFound(shouldBeFound);
        defaultPromotionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPromotionShouldBeFound(String filter) throws Exception {
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].promoCode").value(hasItem(DEFAULT_PROMO_CODE)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].minPurchaseAmount").value(hasItem(sameNumber(DEFAULT_MIN_PURCHASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].maxDiscountAmount").value(hasItem(sameNumber(DEFAULT_MAX_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].usageLimit").value(hasItem(DEFAULT_USAGE_LIMIT)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].applicableCategories").value(hasItem(DEFAULT_APPLICABLE_CATEGORIES)))
            .andExpect(jsonPath("$.[*].excludedProducts").value(hasItem(DEFAULT_EXCLUDED_PRODUCTS)))
            .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS)));

        // Check, that the count call also returns 1
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPromotionShouldNotBeFound(String filter) throws Exception {
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPromotion() throws Exception {
        // Get the promotion
        restPromotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPromotion() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the promotion
        Promotion updatedPromotion = promotionRepository.findById(promotion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPromotion are not directly saved in db
        em.detach(updatedPromotion);
        updatedPromotion
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .promoCode(UPDATED_PROMO_CODE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .minPurchaseAmount(UPDATED_MIN_PURCHASE_AMOUNT)
            .maxDiscountAmount(UPDATED_MAX_DISCOUNT_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .usageLimit(UPDATED_USAGE_LIMIT)
            .usageCount(UPDATED_USAGE_COUNT)
            .isActive(UPDATED_IS_ACTIVE)
            .applicableCategories(UPDATED_APPLICABLE_CATEGORIES)
            .excludedProducts(UPDATED_EXCLUDED_PRODUCTS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);
        PromotionDTO promotionDTO = promotionMapper.toDto(updatedPromotion);

        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(promotionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPromotionToMatchAllProperties(updatedPromotion);
    }

    @Test
    @Transactional
    void putNonExistingPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .promoCode(UPDATED_PROMO_CODE)
            .excludedProducts(UPDATED_EXCLUDED_PRODUCTS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPromotion))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPromotionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPromotion, promotion),
            getPersistedPromotion(promotion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .promoCode(UPDATED_PROMO_CODE)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .minPurchaseAmount(UPDATED_MIN_PURCHASE_AMOUNT)
            .maxDiscountAmount(UPDATED_MAX_DISCOUNT_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .usageLimit(UPDATED_USAGE_LIMIT)
            .usageCount(UPDATED_USAGE_COUNT)
            .isActive(UPDATED_IS_ACTIVE)
            .applicableCategories(UPDATED_APPLICABLE_CATEGORIES)
            .excludedProducts(UPDATED_EXCLUDED_PRODUCTS)
            .termsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPromotion))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPromotionUpdatableFieldsEquals(partialUpdatedPromotion, getPersistedPromotion(partialUpdatedPromotion));
    }

    @Test
    @Transactional
    void patchNonExistingPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        promotion.setId(longCount.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(promotionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromotion() throws Exception {
        // Initialize the database
        insertedPromotion = promotionRepository.saveAndFlush(promotion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the promotion
        restPromotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, promotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return promotionRepository.count();
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

    protected Promotion getPersistedPromotion(Promotion promotion) {
        return promotionRepository.findById(promotion.getId()).orElseThrow();
    }

    protected void assertPersistedPromotionToMatchAllProperties(Promotion expectedPromotion) {
        assertPromotionAllPropertiesEquals(expectedPromotion, getPersistedPromotion(expectedPromotion));
    }

    protected void assertPersistedPromotionToMatchUpdatableProperties(Promotion expectedPromotion) {
        assertPromotionAllUpdatablePropertiesEquals(expectedPromotion, getPersistedPromotion(expectedPromotion));
    }
}
