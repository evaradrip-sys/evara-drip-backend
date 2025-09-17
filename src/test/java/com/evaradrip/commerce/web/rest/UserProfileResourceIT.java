package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.UserProfileAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Coupon;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.User;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.domain.enumeration.Gender;
import com.evaradrip.commerce.domain.enumeration.MembershipLevel;
import com.evaradrip.commerce.repository.UserProfileRepository;
import com.evaradrip.commerce.repository.UserRepository;
import com.evaradrip.commerce.service.UserProfileService;
import com.evaradrip.commerce.service.dto.UserProfileDTO;
import com.evaradrip.commerce.service.mapper.UserProfileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOYALTY_POINTS = 0;
    private static final Integer UPDATED_LOYALTY_POINTS = 1;
    private static final Integer SMALLER_LOYALTY_POINTS = 0 - 1;

    private static final MembershipLevel DEFAULT_MEMBERSHIP_LEVEL = MembershipLevel.BRONZE;
    private static final MembershipLevel UPDATED_MEMBERSHIP_LEVEL = MembershipLevel.SILVER;

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NEWSLETTER_SUBSCRIBED = false;
    private static final Boolean UPDATED_NEWSLETTER_SUBSCRIBED = true;

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepositoryMock;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Mock
    private UserProfileService userProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    private UserProfile insertedUserProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity() {
        return new UserProfile()
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .avatarUrl(DEFAULT_AVATAR_URL)
            .loyaltyPoints(DEFAULT_LOYALTY_POINTS)
            .membershipLevel(DEFAULT_MEMBERSHIP_LEVEL)
            .preferences(DEFAULT_PREFERENCES)
            .newsletterSubscribed(DEFAULT_NEWSLETTER_SUBSCRIBED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity() {
        return new UserProfile()
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .avatarUrl(UPDATED_AVATAR_URL)
            .loyaltyPoints(UPDATED_LOYALTY_POINTS)
            .membershipLevel(UPDATED_MEMBERSHIP_LEVEL)
            .preferences(UPDATED_PREFERENCES)
            .newsletterSubscribed(UPDATED_NEWSLETTER_SUBSCRIBED);
    }

    @BeforeEach
    void initTest() {
        userProfile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserProfile != null) {
            userProfileRepository.delete(insertedUserProfile);
            insertedUserProfile = null;
        }
    }

    @Test
    @Transactional
    void createUserProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        var returnedUserProfileDTO = om.readValue(
            restUserProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserProfileDTO.class
        );

        // Validate the UserProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserProfile = userProfileMapper.toEntity(returnedUserProfileDTO);
        assertUserProfileUpdatableFieldsEquals(returnedUserProfile, getPersistedUserProfile(returnedUserProfile));

        insertedUserProfile = returnedUserProfile;
    }

    @Test
    @Transactional
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserProfiles() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].avatarUrl").value(hasItem(DEFAULT_AVATAR_URL)))
            .andExpect(jsonPath("$.[*].loyaltyPoints").value(hasItem(DEFAULT_LOYALTY_POINTS)))
            .andExpect(jsonPath("$.[*].membershipLevel").value(hasItem(DEFAULT_MEMBERSHIP_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)))
            .andExpect(jsonPath("$.[*].newsletterSubscribed").value(hasItem(DEFAULT_NEWSLETTER_SUBSCRIBED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.avatarUrl").value(DEFAULT_AVATAR_URL))
            .andExpect(jsonPath("$.loyaltyPoints").value(DEFAULT_LOYALTY_POINTS))
            .andExpect(jsonPath("$.membershipLevel").value(DEFAULT_MEMBERSHIP_LEVEL.toString()))
            .andExpect(jsonPath("$.preferences").value(DEFAULT_PREFERENCES))
            .andExpect(jsonPath("$.newsletterSubscribed").value(DEFAULT_NEWSLETTER_SUBSCRIBED));
    }

    @Test
    @Transactional
    void getUserProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        Long id = userProfile.getId();

        defaultUserProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber equals to
        defaultUserProfileFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber in
        defaultUserProfileFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber is not null
        defaultUserProfileFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber contains
        defaultUserProfileFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber does not contain
        defaultUserProfileFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth equals to
        defaultUserProfileFiltering("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH, "dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth in
        defaultUserProfileFiltering(
            "dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH,
            "dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth is not null
        defaultUserProfileFiltering("dateOfBirth.specified=true", "dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth is greater than or equal to
        defaultUserProfileFiltering(
            "dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth is less than or equal to
        defaultUserProfileFiltering(
            "dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth is less than
        defaultUserProfileFiltering("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH, "dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUserProfilesByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dateOfBirth is greater than
        defaultUserProfileFiltering("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH, "dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUserProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender equals to
        defaultUserProfileFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllUserProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender in
        defaultUserProfileFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllUserProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender is not null
        defaultUserProfileFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByAvatarUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avatarUrl equals to
        defaultUserProfileFiltering("avatarUrl.equals=" + DEFAULT_AVATAR_URL, "avatarUrl.equals=" + UPDATED_AVATAR_URL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAvatarUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avatarUrl in
        defaultUserProfileFiltering("avatarUrl.in=" + DEFAULT_AVATAR_URL + "," + UPDATED_AVATAR_URL, "avatarUrl.in=" + UPDATED_AVATAR_URL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAvatarUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avatarUrl is not null
        defaultUserProfileFiltering("avatarUrl.specified=true", "avatarUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByAvatarUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avatarUrl contains
        defaultUserProfileFiltering("avatarUrl.contains=" + DEFAULT_AVATAR_URL, "avatarUrl.contains=" + UPDATED_AVATAR_URL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAvatarUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avatarUrl does not contain
        defaultUserProfileFiltering("avatarUrl.doesNotContain=" + UPDATED_AVATAR_URL, "avatarUrl.doesNotContain=" + DEFAULT_AVATAR_URL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints equals to
        defaultUserProfileFiltering("loyaltyPoints.equals=" + DEFAULT_LOYALTY_POINTS, "loyaltyPoints.equals=" + UPDATED_LOYALTY_POINTS);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints in
        defaultUserProfileFiltering(
            "loyaltyPoints.in=" + DEFAULT_LOYALTY_POINTS + "," + UPDATED_LOYALTY_POINTS,
            "loyaltyPoints.in=" + UPDATED_LOYALTY_POINTS
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints is not null
        defaultUserProfileFiltering("loyaltyPoints.specified=true", "loyaltyPoints.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints is greater than or equal to
        defaultUserProfileFiltering(
            "loyaltyPoints.greaterThanOrEqual=" + DEFAULT_LOYALTY_POINTS,
            "loyaltyPoints.greaterThanOrEqual=" + UPDATED_LOYALTY_POINTS
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints is less than or equal to
        defaultUserProfileFiltering(
            "loyaltyPoints.lessThanOrEqual=" + DEFAULT_LOYALTY_POINTS,
            "loyaltyPoints.lessThanOrEqual=" + SMALLER_LOYALTY_POINTS
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints is less than
        defaultUserProfileFiltering("loyaltyPoints.lessThan=" + UPDATED_LOYALTY_POINTS, "loyaltyPoints.lessThan=" + DEFAULT_LOYALTY_POINTS);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLoyaltyPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where loyaltyPoints is greater than
        defaultUserProfileFiltering(
            "loyaltyPoints.greaterThan=" + SMALLER_LOYALTY_POINTS,
            "loyaltyPoints.greaterThan=" + DEFAULT_LOYALTY_POINTS
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMembershipLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where membershipLevel equals to
        defaultUserProfileFiltering(
            "membershipLevel.equals=" + DEFAULT_MEMBERSHIP_LEVEL,
            "membershipLevel.equals=" + UPDATED_MEMBERSHIP_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMembershipLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where membershipLevel in
        defaultUserProfileFiltering(
            "membershipLevel.in=" + DEFAULT_MEMBERSHIP_LEVEL + "," + UPDATED_MEMBERSHIP_LEVEL,
            "membershipLevel.in=" + UPDATED_MEMBERSHIP_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMembershipLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where membershipLevel is not null
        defaultUserProfileFiltering("membershipLevel.specified=true", "membershipLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByNewsletterSubscribedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where newsletterSubscribed equals to
        defaultUserProfileFiltering(
            "newsletterSubscribed.equals=" + DEFAULT_NEWSLETTER_SUBSCRIBED,
            "newsletterSubscribed.equals=" + UPDATED_NEWSLETTER_SUBSCRIBED
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByNewsletterSubscribedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where newsletterSubscribed in
        defaultUserProfileFiltering(
            "newsletterSubscribed.in=" + DEFAULT_NEWSLETTER_SUBSCRIBED + "," + UPDATED_NEWSLETTER_SUBSCRIBED,
            "newsletterSubscribed.in=" + UPDATED_NEWSLETTER_SUBSCRIBED
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByNewsletterSubscribedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where newsletterSubscribed is not null
        defaultUserProfileFiltering("newsletterSubscribed.specified=true", "newsletterSubscribed.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userProfileRepository.saveAndFlush(userProfile);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        userProfileRepository.saveAndFlush(userProfile);
        Long userId = user.getId();
        // Get all the userProfileList where user equals to userId
        defaultUserProfileShouldBeFound("userId.equals=" + userId);

        // Get all the userProfileList where user equals to (userId + 1)
        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserProfilesByWishlistIsEqualToSomething() throws Exception {
        Product wishlist;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            userProfileRepository.saveAndFlush(userProfile);
            wishlist = ProductResourceIT.createEntity(em);
        } else {
            wishlist = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(wishlist);
        em.flush();
        userProfile.addWishlist(wishlist);
        userProfileRepository.saveAndFlush(userProfile);
        Long wishlistId = wishlist.getId();
        // Get all the userProfileList where wishlist equals to wishlistId
        defaultUserProfileShouldBeFound("wishlistId.equals=" + wishlistId);

        // Get all the userProfileList where wishlist equals to (wishlistId + 1)
        defaultUserProfileShouldNotBeFound("wishlistId.equals=" + (wishlistId + 1));
    }

    @Test
    @Transactional
    void getAllUserProfilesByCouponsIsEqualToSomething() throws Exception {
        Coupon coupons;
        if (TestUtil.findAll(em, Coupon.class).isEmpty()) {
            userProfileRepository.saveAndFlush(userProfile);
            coupons = CouponResourceIT.createEntity();
        } else {
            coupons = TestUtil.findAll(em, Coupon.class).get(0);
        }
        em.persist(coupons);
        em.flush();
        userProfile.addCoupons(coupons);
        userProfileRepository.saveAndFlush(userProfile);
        Long couponsId = coupons.getId();
        // Get all the userProfileList where coupons equals to couponsId
        defaultUserProfileShouldBeFound("couponsId.equals=" + couponsId);

        // Get all the userProfileList where coupons equals to (couponsId + 1)
        defaultUserProfileShouldNotBeFound("couponsId.equals=" + (couponsId + 1));
    }

    private void defaultUserProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserProfileShouldBeFound(shouldBeFound);
        defaultUserProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].avatarUrl").value(hasItem(DEFAULT_AVATAR_URL)))
            .andExpect(jsonPath("$.[*].loyaltyPoints").value(hasItem(DEFAULT_LOYALTY_POINTS)))
            .andExpect(jsonPath("$.[*].membershipLevel").value(hasItem(DEFAULT_MEMBERSHIP_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)))
            .andExpect(jsonPath("$.[*].newsletterSubscribed").value(hasItem(DEFAULT_NEWSLETTER_SUBSCRIBED)));

        // Check, that the count call also returns 1
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .avatarUrl(UPDATED_AVATAR_URL)
            .loyaltyPoints(UPDATED_LOYALTY_POINTS)
            .membershipLevel(UPDATED_MEMBERSHIP_LEVEL)
            .preferences(UPDATED_PREFERENCES)
            .newsletterSubscribed(UPDATED_NEWSLETTER_SUBSCRIBED);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserProfileToMatchAllProperties(updatedUserProfile);
    }

    @Test
    @Transactional
    void putNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .loyaltyPoints(UPDATED_LOYALTY_POINTS)
            .membershipLevel(UPDATED_MEMBERSHIP_LEVEL)
            .preferences(UPDATED_PREFERENCES);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserProfile, userProfile),
            getPersistedUserProfile(userProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .avatarUrl(UPDATED_AVATAR_URL)
            .loyaltyPoints(UPDATED_LOYALTY_POINTS)
            .membershipLevel(UPDATED_MEMBERSHIP_LEVEL)
            .preferences(UPDATED_PREFERENCES)
            .newsletterSubscribed(UPDATED_NEWSLETTER_SUBSCRIBED);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(partialUpdatedUserProfile, getPersistedUserProfile(partialUpdatedUserProfile));
    }

    @Test
    @Transactional
    void patchNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userProfile
        restUserProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userProfileRepository.count();
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

    protected UserProfile getPersistedUserProfile(UserProfile userProfile) {
        return userProfileRepository.findById(userProfile.getId()).orElseThrow();
    }

    protected void assertPersistedUserProfileToMatchAllProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllPropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }

    protected void assertPersistedUserProfileToMatchUpdatableProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllUpdatablePropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }
}
