package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.UserAddressAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.UserAddress;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.domain.enumeration.AddressType;
import com.evaradrip.commerce.repository.UserAddressRepository;
import com.evaradrip.commerce.service.dto.UserAddressDTO;
import com.evaradrip.commerce.service.mapper.UserAddressMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UserAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAddressResourceIT {

    private static final AddressType DEFAULT_ADDRESS_TYPE = AddressType.SHIPPING;
    private static final AddressType UPDATED_ADDRESS_TYPE = AddressType.BILLING;

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/user-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAddressMockMvc;

    private UserAddress userAddress;

    private UserAddress insertedUserAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .addressType(DEFAULT_ADDRESS_TYPE)
            .fullName(DEFAULT_FULL_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .streetAddress2(DEFAULT_STREET_ADDRESS_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .landmark(DEFAULT_LANDMARK)
            .isDefault(DEFAULT_IS_DEFAULT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        userAddress.setUser(userProfile);
        return userAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createUpdatedEntity(EntityManager em) {
        UserAddress updatedUserAddress = new UserAddress()
            .addressType(UPDATED_ADDRESS_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .streetAddress2(UPDATED_STREET_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .isDefault(UPDATED_IS_DEFAULT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        updatedUserAddress.setUser(userProfile);
        return updatedUserAddress;
    }

    @BeforeEach
    void initTest() {
        userAddress = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserAddress != null) {
            userAddressRepository.delete(insertedUserAddress);
            insertedUserAddress = null;
        }
    }

    @Test
    @Transactional
    void createUserAddress() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);
        var returnedUserAddressDTO = om.readValue(
            restUserAddressMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAddressDTO.class
        );

        // Validate the UserAddress in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAddress = userAddressMapper.toEntity(returnedUserAddressDTO);
        assertUserAddressUpdatableFieldsEquals(returnedUserAddress, getPersistedUserAddress(returnedUserAddress));

        insertedUserAddress = returnedUserAddress;
    }

    @Test
    @Transactional
    void createUserAddressWithExistingId() throws Exception {
        // Create the UserAddress with an existing ID
        userAddress.setId(1L);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setAddressType(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setFullName(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setPhoneNumber(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setStreetAddress(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setCity(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setState(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZipCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setZipCode(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAddress.setCountry(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAddresses() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].streetAddress2").value(hasItem(DEFAULT_STREET_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)));
    }

    @Test
    @Transactional
    void getUserAddress() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get the userAddress
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, userAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressType").value(DEFAULT_ADDRESS_TYPE.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.streetAddress2").value(DEFAULT_STREET_ADDRESS_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT));
    }

    @Test
    @Transactional
    void getUserAddressesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        Long id = userAddress.getId();

        defaultUserAddressFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserAddressFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserAddressFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAddressTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressType equals to
        defaultUserAddressFiltering("addressType.equals=" + DEFAULT_ADDRESS_TYPE, "addressType.equals=" + UPDATED_ADDRESS_TYPE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAddressTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressType in
        defaultUserAddressFiltering(
            "addressType.in=" + DEFAULT_ADDRESS_TYPE + "," + UPDATED_ADDRESS_TYPE,
            "addressType.in=" + UPDATED_ADDRESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByAddressTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where addressType is not null
        defaultUserAddressFiltering("addressType.specified=true", "addressType.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where fullName equals to
        defaultUserAddressFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where fullName in
        defaultUserAddressFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where fullName is not null
        defaultUserAddressFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where fullName contains
        defaultUserAddressFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where fullName does not contain
        defaultUserAddressFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where phoneNumber equals to
        defaultUserAddressFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where phoneNumber in
        defaultUserAddressFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where phoneNumber is not null
        defaultUserAddressFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where phoneNumber contains
        defaultUserAddressFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where phoneNumber does not contain
        defaultUserAddressFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress equals to
        defaultUserAddressFiltering("streetAddress.equals=" + DEFAULT_STREET_ADDRESS, "streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress in
        defaultUserAddressFiltering(
            "streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS,
            "streetAddress.in=" + UPDATED_STREET_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress is not null
        defaultUserAddressFiltering("streetAddress.specified=true", "streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress contains
        defaultUserAddressFiltering("streetAddress.contains=" + DEFAULT_STREET_ADDRESS, "streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress does not contain
        defaultUserAddressFiltering(
            "streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS,
            "streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress2 equals to
        defaultUserAddressFiltering(
            "streetAddress2.equals=" + DEFAULT_STREET_ADDRESS_2,
            "streetAddress2.equals=" + UPDATED_STREET_ADDRESS_2
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress2 in
        defaultUserAddressFiltering(
            "streetAddress2.in=" + DEFAULT_STREET_ADDRESS_2 + "," + UPDATED_STREET_ADDRESS_2,
            "streetAddress2.in=" + UPDATED_STREET_ADDRESS_2
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress2 is not null
        defaultUserAddressFiltering("streetAddress2.specified=true", "streetAddress2.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddress2ContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress2 contains
        defaultUserAddressFiltering(
            "streetAddress2.contains=" + DEFAULT_STREET_ADDRESS_2,
            "streetAddress2.contains=" + UPDATED_STREET_ADDRESS_2
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByStreetAddress2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where streetAddress2 does not contain
        defaultUserAddressFiltering(
            "streetAddress2.doesNotContain=" + UPDATED_STREET_ADDRESS_2,
            "streetAddress2.doesNotContain=" + DEFAULT_STREET_ADDRESS_2
        );
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city equals to
        defaultUserAddressFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city in
        defaultUserAddressFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city is not null
        defaultUserAddressFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city contains
        defaultUserAddressFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city does not contain
        defaultUserAddressFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where state equals to
        defaultUserAddressFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where state in
        defaultUserAddressFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where state is not null
        defaultUserAddressFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByStateContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where state contains
        defaultUserAddressFiltering("state.contains=" + DEFAULT_STATE, "state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where state does not contain
        defaultUserAddressFiltering("state.doesNotContain=" + UPDATED_STATE, "state.doesNotContain=" + DEFAULT_STATE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode equals to
        defaultUserAddressFiltering("zipCode.equals=" + DEFAULT_ZIP_CODE, "zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode in
        defaultUserAddressFiltering("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE, "zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode is not null
        defaultUserAddressFiltering("zipCode.specified=true", "zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode contains
        defaultUserAddressFiltering("zipCode.contains=" + DEFAULT_ZIP_CODE, "zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where zipCode does not contain
        defaultUserAddressFiltering("zipCode.doesNotContain=" + UPDATED_ZIP_CODE, "zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country equals to
        defaultUserAddressFiltering("country.equals=" + DEFAULT_COUNTRY, "country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country in
        defaultUserAddressFiltering("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY, "country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country is not null
        defaultUserAddressFiltering("country.specified=true", "country.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByCountryContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country contains
        defaultUserAddressFiltering("country.contains=" + DEFAULT_COUNTRY, "country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where country does not contain
        defaultUserAddressFiltering("country.doesNotContain=" + UPDATED_COUNTRY, "country.doesNotContain=" + DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark equals to
        defaultUserAddressFiltering("landmark.equals=" + DEFAULT_LANDMARK, "landmark.equals=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark in
        defaultUserAddressFiltering("landmark.in=" + DEFAULT_LANDMARK + "," + UPDATED_LANDMARK, "landmark.in=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark is not null
        defaultUserAddressFiltering("landmark.specified=true", "landmark.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark contains
        defaultUserAddressFiltering("landmark.contains=" + DEFAULT_LANDMARK, "landmark.contains=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark does not contain
        defaultUserAddressFiltering("landmark.doesNotContain=" + UPDATED_LANDMARK, "landmark.doesNotContain=" + DEFAULT_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isDefault equals to
        defaultUserAddressFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isDefault in
        defaultUserAddressFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isDefault is not null
        defaultUserAddressFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userAddressRepository.saveAndFlush(userAddress);
            user = UserProfileResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        userAddress.setUser(user);
        userAddressRepository.saveAndFlush(userAddress);
        Long userId = user.getId();
        // Get all the userAddressList where user equals to userId
        defaultUserAddressShouldBeFound("userId.equals=" + userId);

        // Get all the userAddressList where user equals to (userId + 1)
        defaultUserAddressShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultUserAddressFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserAddressShouldBeFound(shouldBeFound);
        defaultUserAddressShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAddressShouldBeFound(String filter) throws Exception {
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].streetAddress2").value(hasItem(DEFAULT_STREET_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)));

        // Check, that the count call also returns 1
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAddressShouldNotBeFound(String filter) throws Exception {
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAddress() throws Exception {
        // Get the userAddress
        restUserAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAddress() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAddress
        UserAddress updatedUserAddress = userAddressRepository.findById(userAddress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAddress are not directly saved in db
        em.detach(updatedUserAddress);
        updatedUserAddress
            .addressType(UPDATED_ADDRESS_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .streetAddress2(UPDATED_STREET_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .isDefault(UPDATED_IS_DEFAULT);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(updatedUserAddress);

        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAddressToMatchAllProperties(updatedUserAddress);
    }

    @Test
    @Transactional
    void putNonExistingUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .addressType(UPDATED_ADDRESS_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .landmark(UPDATED_LANDMARK);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAddressUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAddress, userAddress),
            getPersistedUserAddress(userAddress)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .addressType(UPDATED_ADDRESS_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .streetAddress2(UPDATED_STREET_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .isDefault(UPDATED_IS_DEFAULT);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAddressUpdatableFieldsEquals(partialUpdatedUserAddress, getPersistedUserAddress(partialUpdatedUserAddress));
    }

    @Test
    @Transactional
    void patchNonExistingUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAddress() throws Exception {
        // Initialize the database
        insertedUserAddress = userAddressRepository.saveAndFlush(userAddress);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAddress
        restUserAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAddressRepository.count();
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

    protected UserAddress getPersistedUserAddress(UserAddress userAddress) {
        return userAddressRepository.findById(userAddress.getId()).orElseThrow();
    }

    protected void assertPersistedUserAddressToMatchAllProperties(UserAddress expectedUserAddress) {
        assertUserAddressAllPropertiesEquals(expectedUserAddress, getPersistedUserAddress(expectedUserAddress));
    }

    protected void assertPersistedUserAddressToMatchUpdatableProperties(UserAddress expectedUserAddress) {
        assertUserAddressAllUpdatablePropertiesEquals(expectedUserAddress, getPersistedUserAddress(expectedUserAddress));
    }
}
