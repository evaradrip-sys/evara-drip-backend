package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.ReviewAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Review;
import com.evaradrip.commerce.domain.User;
import com.evaradrip.commerce.domain.enumeration.ReviewStatus;
import com.evaradrip.commerce.repository.ReviewRepository;
import com.evaradrip.commerce.repository.UserRepository;
import com.evaradrip.commerce.service.ReviewService;
import com.evaradrip.commerce.service.dto.ReviewDTO;
import com.evaradrip.commerce.service.mapper.ReviewMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_HELPFUL_COUNT = 0;
    private static final Integer UPDATED_HELPFUL_COUNT = 1;
    private static final Integer SMALLER_HELPFUL_COUNT = 0 - 1;

    private static final Integer DEFAULT_NOT_HELPFUL_COUNT = 0;
    private static final Integer UPDATED_NOT_HELPFUL_COUNT = 1;
    private static final Integer SMALLER_NOT_HELPFUL_COUNT = 0 - 1;

    private static final Boolean DEFAULT_VERIFIED_PURCHASE = false;
    private static final Boolean UPDATED_VERIFIED_PURCHASE = true;

    private static final ReviewStatus DEFAULT_STATUS = ReviewStatus.PENDING;
    private static final ReviewStatus UPDATED_STATUS = ReviewStatus.APPROVED;

    private static final String DEFAULT_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RESPONSE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESPONSE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESPONSE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepositoryMock;

    @Autowired
    private ReviewMapper reviewMapper;

    @Mock
    private ReviewService reviewServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    private Review insertedReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .rating(DEFAULT_RATING)
            .title(DEFAULT_TITLE)
            .comment(DEFAULT_COMMENT)
            .helpfulCount(DEFAULT_HELPFUL_COUNT)
            .notHelpfulCount(DEFAULT_NOT_HELPFUL_COUNT)
            .verifiedPurchase(DEFAULT_VERIFIED_PURCHASE)
            .status(DEFAULT_STATUS)
            .response(DEFAULT_RESPONSE)
            .responseDate(DEFAULT_RESPONSE_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        review.setUser(user);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        review.setProduct(product);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity(EntityManager em) {
        Review updatedReview = new Review()
            .rating(UPDATED_RATING)
            .title(UPDATED_TITLE)
            .comment(UPDATED_COMMENT)
            .helpfulCount(UPDATED_HELPFUL_COUNT)
            .notHelpfulCount(UPDATED_NOT_HELPFUL_COUNT)
            .verifiedPurchase(UPDATED_VERIFIED_PURCHASE)
            .status(UPDATED_STATUS)
            .response(UPDATED_RESPONSE)
            .responseDate(UPDATED_RESPONSE_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedReview.setUser(user);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedReview.setProduct(product);
        return updatedReview;
    }

    @BeforeEach
    void initTest() {
        review = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReview != null) {
            reviewRepository.delete(insertedReview);
            insertedReview = null;
        }
    }

    @Test
    @Transactional
    void createReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        var returnedReviewDTO = om.readValue(
            restReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewDTO.class
        );

        // Validate the Review in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReview = reviewMapper.toEntity(returnedReviewDTO);
        assertReviewUpdatableFieldsEquals(returnedReview, getPersistedReview(returnedReview));

        insertedReview = returnedReview;
    }

    @Test
    @Transactional
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        review.setId(1L);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        review.setRating(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviews() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].helpfulCount").value(hasItem(DEFAULT_HELPFUL_COUNT)))
            .andExpect(jsonPath("$.[*].notHelpfulCount").value(hasItem(DEFAULT_NOT_HELPFUL_COUNT)))
            .andExpect(jsonPath("$.[*].verifiedPurchase").value(hasItem(DEFAULT_VERIFIED_PURCHASE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE)))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.helpfulCount").value(DEFAULT_HELPFUL_COUNT))
            .andExpect(jsonPath("$.notHelpfulCount").value(DEFAULT_NOT_HELPFUL_COUNT))
            .andExpect(jsonPath("$.verifiedPurchase").value(DEFAULT_VERIFIED_PURCHASE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE))
            .andExpect(jsonPath("$.responseDate").value(sameInstant(DEFAULT_RESPONSE_DATE)));
    }

    @Test
    @Transactional
    void getReviewsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        Long id = review.getId();

        defaultReviewFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating equals to
        defaultReviewFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating in
        defaultReviewFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is not null
        defaultReviewFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than or equal to
        defaultReviewFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than or equal to
        defaultReviewFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than
        defaultReviewFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than
        defaultReviewFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title equals to
        defaultReviewFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title in
        defaultReviewFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title is not null
        defaultReviewFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title contains
        defaultReviewFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title does not contain
        defaultReviewFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount equals to
        defaultReviewFiltering("helpfulCount.equals=" + DEFAULT_HELPFUL_COUNT, "helpfulCount.equals=" + UPDATED_HELPFUL_COUNT);
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount in
        defaultReviewFiltering(
            "helpfulCount.in=" + DEFAULT_HELPFUL_COUNT + "," + UPDATED_HELPFUL_COUNT,
            "helpfulCount.in=" + UPDATED_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount is not null
        defaultReviewFiltering("helpfulCount.specified=true", "helpfulCount.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount is greater than or equal to
        defaultReviewFiltering(
            "helpfulCount.greaterThanOrEqual=" + DEFAULT_HELPFUL_COUNT,
            "helpfulCount.greaterThanOrEqual=" + UPDATED_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount is less than or equal to
        defaultReviewFiltering(
            "helpfulCount.lessThanOrEqual=" + DEFAULT_HELPFUL_COUNT,
            "helpfulCount.lessThanOrEqual=" + SMALLER_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount is less than
        defaultReviewFiltering("helpfulCount.lessThan=" + UPDATED_HELPFUL_COUNT, "helpfulCount.lessThan=" + DEFAULT_HELPFUL_COUNT);
    }

    @Test
    @Transactional
    void getAllReviewsByHelpfulCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulCount is greater than
        defaultReviewFiltering("helpfulCount.greaterThan=" + SMALLER_HELPFUL_COUNT, "helpfulCount.greaterThan=" + DEFAULT_HELPFUL_COUNT);
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount equals to
        defaultReviewFiltering(
            "notHelpfulCount.equals=" + DEFAULT_NOT_HELPFUL_COUNT,
            "notHelpfulCount.equals=" + UPDATED_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount in
        defaultReviewFiltering(
            "notHelpfulCount.in=" + DEFAULT_NOT_HELPFUL_COUNT + "," + UPDATED_NOT_HELPFUL_COUNT,
            "notHelpfulCount.in=" + UPDATED_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount is not null
        defaultReviewFiltering("notHelpfulCount.specified=true", "notHelpfulCount.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount is greater than or equal to
        defaultReviewFiltering(
            "notHelpfulCount.greaterThanOrEqual=" + DEFAULT_NOT_HELPFUL_COUNT,
            "notHelpfulCount.greaterThanOrEqual=" + UPDATED_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount is less than or equal to
        defaultReviewFiltering(
            "notHelpfulCount.lessThanOrEqual=" + DEFAULT_NOT_HELPFUL_COUNT,
            "notHelpfulCount.lessThanOrEqual=" + SMALLER_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount is less than
        defaultReviewFiltering(
            "notHelpfulCount.lessThan=" + UPDATED_NOT_HELPFUL_COUNT,
            "notHelpfulCount.lessThan=" + DEFAULT_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByNotHelpfulCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where notHelpfulCount is greater than
        defaultReviewFiltering(
            "notHelpfulCount.greaterThan=" + SMALLER_NOT_HELPFUL_COUNT,
            "notHelpfulCount.greaterThan=" + DEFAULT_NOT_HELPFUL_COUNT
        );
    }

    @Test
    @Transactional
    void getAllReviewsByVerifiedPurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase equals to
        defaultReviewFiltering(
            "verifiedPurchase.equals=" + DEFAULT_VERIFIED_PURCHASE,
            "verifiedPurchase.equals=" + UPDATED_VERIFIED_PURCHASE
        );
    }

    @Test
    @Transactional
    void getAllReviewsByVerifiedPurchaseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase in
        defaultReviewFiltering(
            "verifiedPurchase.in=" + DEFAULT_VERIFIED_PURCHASE + "," + UPDATED_VERIFIED_PURCHASE,
            "verifiedPurchase.in=" + UPDATED_VERIFIED_PURCHASE
        );
    }

    @Test
    @Transactional
    void getAllReviewsByVerifiedPurchaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase is not null
        defaultReviewFiltering("verifiedPurchase.specified=true", "verifiedPurchase.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where status equals to
        defaultReviewFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReviewsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where status in
        defaultReviewFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReviewsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where status is not null
        defaultReviewFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate equals to
        defaultReviewFiltering("responseDate.equals=" + DEFAULT_RESPONSE_DATE, "responseDate.equals=" + UPDATED_RESPONSE_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate in
        defaultReviewFiltering(
            "responseDate.in=" + DEFAULT_RESPONSE_DATE + "," + UPDATED_RESPONSE_DATE,
            "responseDate.in=" + UPDATED_RESPONSE_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate is not null
        defaultReviewFiltering("responseDate.specified=true", "responseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate is greater than or equal to
        defaultReviewFiltering(
            "responseDate.greaterThanOrEqual=" + DEFAULT_RESPONSE_DATE,
            "responseDate.greaterThanOrEqual=" + UPDATED_RESPONSE_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate is less than or equal to
        defaultReviewFiltering(
            "responseDate.lessThanOrEqual=" + DEFAULT_RESPONSE_DATE,
            "responseDate.lessThanOrEqual=" + SMALLER_RESPONSE_DATE
        );
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate is less than
        defaultReviewFiltering("responseDate.lessThan=" + UPDATED_RESPONSE_DATE, "responseDate.lessThan=" + DEFAULT_RESPONSE_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByResponseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where responseDate is greater than
        defaultReviewFiltering("responseDate.greaterThan=" + SMALLER_RESPONSE_DATE, "responseDate.greaterThan=" + DEFAULT_RESPONSE_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        review.setUser(user);
        reviewRepository.saveAndFlush(review);
        Long userId = user.getId();
        // Get all the reviewList where user equals to userId
        defaultReviewShouldBeFound("userId.equals=" + userId);

        // Get all the reviewList where user equals to (userId + 1)
        defaultReviewShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        review.setProduct(product);
        reviewRepository.saveAndFlush(review);
        Long productId = product.getId();
        // Get all the reviewList where product equals to productId
        defaultReviewShouldBeFound("productId.equals=" + productId);

        // Get all the reviewList where product equals to (productId + 1)
        defaultReviewShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultReviewFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewShouldBeFound(shouldBeFound);
        defaultReviewShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].helpfulCount").value(hasItem(DEFAULT_HELPFUL_COUNT)))
            .andExpect(jsonPath("$.[*].notHelpfulCount").value(hasItem(DEFAULT_NOT_HELPFUL_COUNT)))
            .andExpect(jsonPath("$.[*].verifiedPurchase").value(hasItem(DEFAULT_VERIFIED_PURCHASE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE)))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATE))));

        // Check, that the count call also returns 1
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview
            .rating(UPDATED_RATING)
            .title(UPDATED_TITLE)
            .comment(UPDATED_COMMENT)
            .helpfulCount(UPDATED_HELPFUL_COUNT)
            .notHelpfulCount(UPDATED_NOT_HELPFUL_COUNT)
            .verifiedPurchase(UPDATED_VERIFIED_PURCHASE)
            .status(UPDATED_STATUS)
            .response(UPDATED_RESPONSE)
            .responseDate(UPDATED_RESPONSE_DATE);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewToMatchAllProperties(updatedReview);
    }

    @Test
    @Transactional
    void putNonExistingReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .notHelpfulCount(UPDATED_NOT_HELPFUL_COUNT)
            .status(UPDATED_STATUS);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReview, review), getPersistedReview(review));
    }

    @Test
    @Transactional
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .rating(UPDATED_RATING)
            .title(UPDATED_TITLE)
            .comment(UPDATED_COMMENT)
            .helpfulCount(UPDATED_HELPFUL_COUNT)
            .notHelpfulCount(UPDATED_NOT_HELPFUL_COUNT)
            .verifiedPurchase(UPDATED_VERIFIED_PURCHASE)
            .status(UPDATED_STATUS)
            .response(UPDATED_RESPONSE)
            .responseDate(UPDATED_RESPONSE_DATE);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewUpdatableFieldsEquals(partialUpdatedReview, getPersistedReview(partialUpdatedReview));
    }

    @Test
    @Transactional
    void patchNonExistingReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the review
        restReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, review.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewRepository.count();
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

    protected Review getPersistedReview(Review review) {
        return reviewRepository.findById(review.getId()).orElseThrow();
    }

    protected void assertPersistedReviewToMatchAllProperties(Review expectedReview) {
        assertReviewAllPropertiesEquals(expectedReview, getPersistedReview(expectedReview));
    }

    protected void assertPersistedReviewToMatchUpdatableProperties(Review expectedReview) {
        assertReviewAllUpdatablePropertiesEquals(expectedReview, getPersistedReview(expectedReview));
    }
}
