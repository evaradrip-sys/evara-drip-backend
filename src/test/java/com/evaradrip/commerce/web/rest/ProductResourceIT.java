package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.ProductAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Brand;
import com.evaradrip.commerce.domain.Category;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.Promotion;
import com.evaradrip.commerce.domain.UserProfile;
import com.evaradrip.commerce.domain.enumeration.ProductStatus;
import com.evaradrip.commerce.repository.ProductRepository;
import com.evaradrip.commerce.service.ProductService;
import com.evaradrip.commerce.service.dto.ProductDTO;
import com.evaradrip.commerce.service.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_ORIGINAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_ORIGINAL_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_ORIGINAL_PRICE = new BigDecimal(0 - 1);

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_NEW = false;
    private static final Boolean UPDATED_IS_NEW = true;

    private static final Boolean DEFAULT_IS_ON_SALE = false;
    private static final Boolean UPDATED_IS_ON_SALE = true;

    private static final BigDecimal DEFAULT_RATING = new BigDecimal(0);
    private static final BigDecimal UPDATED_RATING = new BigDecimal(1);
    private static final BigDecimal SMALLER_RATING = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_REVIEWS_COUNT = 0;
    private static final Integer UPDATED_REVIEWS_COUNT = 1;
    private static final Integer SMALLER_REVIEWS_COUNT = 0 - 1;

    private static final Integer DEFAULT_STOCK_COUNT = 0;
    private static final Integer UPDATED_STOCK_COUNT = 1;
    private static final Integer SMALLER_STOCK_COUNT = 0 - 1;

    private static final Boolean DEFAULT_IN_STOCK = false;
    private static final Boolean UPDATED_IN_STOCK = true;

    private static final String DEFAULT_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_META_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_META_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_META_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_META_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_META_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_META_KEYWORDS = "BBBBBBBBBB";

    private static final ProductStatus DEFAULT_STATUS = ProductStatus.ACTIVE;
    private static final ProductStatus UPDATED_STATUS = ProductStatus.INACTIVE;

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(0);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(1);
    private static final BigDecimal SMALLER_WEIGHT = new BigDecimal(0 - 1);

    private static final String DEFAULT_DIMENSIONS = "AAAAAAAAAA";
    private static final String UPDATED_DIMENSIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .originalPrice(DEFAULT_ORIGINAL_PRICE)
            .sku(DEFAULT_SKU)
            .isNew(DEFAULT_IS_NEW)
            .isOnSale(DEFAULT_IS_ON_SALE)
            .rating(DEFAULT_RATING)
            .reviewsCount(DEFAULT_REVIEWS_COUNT)
            .stockCount(DEFAULT_STOCK_COUNT)
            .inStock(DEFAULT_IN_STOCK)
            .features(DEFAULT_FEATURES)
            .metaTitle(DEFAULT_META_TITLE)
            .metaDescription(DEFAULT_META_DESCRIPTION)
            .metaKeywords(DEFAULT_META_KEYWORDS)
            .status(DEFAULT_STATUS)
            .weight(DEFAULT_WEIGHT)
            .dimensions(DEFAULT_DIMENSIONS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        product.setCategory(category);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product updatedProduct = new Product()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .sku(UPDATED_SKU)
            .isNew(UPDATED_IS_NEW)
            .isOnSale(UPDATED_IS_ON_SALE)
            .rating(UPDATED_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .stockCount(UPDATED_STOCK_COUNT)
            .inStock(UPDATED_IN_STOCK)
            .features(UPDATED_FEATURES)
            .metaTitle(UPDATED_META_TITLE)
            .metaDescription(UPDATED_META_DESCRIPTION)
            .metaKeywords(UPDATED_META_KEYWORDS)
            .status(UPDATED_STATUS)
            .weight(UPDATED_WEIGHT)
            .dimensions(UPDATED_DIMENSIONS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        updatedProduct.setCategory(category);
        return updatedProduct;
    }

    @BeforeEach
    void initTest() {
        product = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDTO.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setStockCount(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].originalPrice").value(hasItem(sameNumber(DEFAULT_ORIGINAL_PRICE))))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW)))
            .andExpect(jsonPath("$.[*].isOnSale").value(hasItem(DEFAULT_IS_ON_SALE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(sameNumber(DEFAULT_RATING))))
            .andExpect(jsonPath("$.[*].reviewsCount").value(hasItem(DEFAULT_REVIEWS_COUNT)))
            .andExpect(jsonPath("$.[*].stockCount").value(hasItem(DEFAULT_STOCK_COUNT)))
            .andExpect(jsonPath("$.[*].inStock").value(hasItem(DEFAULT_IN_STOCK)))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES)))
            .andExpect(jsonPath("$.[*].metaTitle").value(hasItem(DEFAULT_META_TITLE)))
            .andExpect(jsonPath("$.[*].metaDescription").value(hasItem(DEFAULT_META_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].metaKeywords").value(hasItem(DEFAULT_META_KEYWORDS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].dimensions").value(hasItem(DEFAULT_DIMENSIONS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.originalPrice").value(sameNumber(DEFAULT_ORIGINAL_PRICE)))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.isNew").value(DEFAULT_IS_NEW))
            .andExpect(jsonPath("$.isOnSale").value(DEFAULT_IS_ON_SALE))
            .andExpect(jsonPath("$.rating").value(sameNumber(DEFAULT_RATING)))
            .andExpect(jsonPath("$.reviewsCount").value(DEFAULT_REVIEWS_COUNT))
            .andExpect(jsonPath("$.stockCount").value(DEFAULT_STOCK_COUNT))
            .andExpect(jsonPath("$.inStock").value(DEFAULT_IN_STOCK))
            .andExpect(jsonPath("$.features").value(DEFAULT_FEATURES))
            .andExpect(jsonPath("$.metaTitle").value(DEFAULT_META_TITLE))
            .andExpect(jsonPath("$.metaDescription").value(DEFAULT_META_DESCRIPTION))
            .andExpect(jsonPath("$.metaKeywords").value(DEFAULT_META_KEYWORDS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.weight").value(sameNumber(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.dimensions").value(DEFAULT_DIMENSIONS));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name equals to
        defaultProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name in
        defaultProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name contains
        defaultProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain
        defaultProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price equals to
        defaultProductFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price in
        defaultProductFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to
        defaultProductFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to
        defaultProductFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than
        defaultProductFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than
        defaultProductFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice equals to
        defaultProductFiltering("originalPrice.equals=" + DEFAULT_ORIGINAL_PRICE, "originalPrice.equals=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice in
        defaultProductFiltering(
            "originalPrice.in=" + DEFAULT_ORIGINAL_PRICE + "," + UPDATED_ORIGINAL_PRICE,
            "originalPrice.in=" + UPDATED_ORIGINAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice is not null
        defaultProductFiltering("originalPrice.specified=true", "originalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice is greater than or equal to
        defaultProductFiltering(
            "originalPrice.greaterThanOrEqual=" + DEFAULT_ORIGINAL_PRICE,
            "originalPrice.greaterThanOrEqual=" + UPDATED_ORIGINAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice is less than or equal to
        defaultProductFiltering(
            "originalPrice.lessThanOrEqual=" + DEFAULT_ORIGINAL_PRICE,
            "originalPrice.lessThanOrEqual=" + SMALLER_ORIGINAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice is less than
        defaultProductFiltering("originalPrice.lessThan=" + UPDATED_ORIGINAL_PRICE, "originalPrice.lessThan=" + DEFAULT_ORIGINAL_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByOriginalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where originalPrice is greater than
        defaultProductFiltering(
            "originalPrice.greaterThan=" + SMALLER_ORIGINAL_PRICE,
            "originalPrice.greaterThan=" + DEFAULT_ORIGINAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to
        defaultProductFiltering("sku.equals=" + DEFAULT_SKU, "sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku in
        defaultProductFiltering("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU, "sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductFiltering("sku.specified=true", "sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku contains
        defaultProductFiltering("sku.contains=" + DEFAULT_SKU, "sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain
        defaultProductFiltering("sku.doesNotContain=" + UPDATED_SKU, "sku.doesNotContain=" + DEFAULT_SKU);
    }

    @Test
    @Transactional
    void getAllProductsByIsNewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isNew equals to
        defaultProductFiltering("isNew.equals=" + DEFAULT_IS_NEW, "isNew.equals=" + UPDATED_IS_NEW);
    }

    @Test
    @Transactional
    void getAllProductsByIsNewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isNew in
        defaultProductFiltering("isNew.in=" + DEFAULT_IS_NEW + "," + UPDATED_IS_NEW, "isNew.in=" + UPDATED_IS_NEW);
    }

    @Test
    @Transactional
    void getAllProductsByIsNewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isNew is not null
        defaultProductFiltering("isNew.specified=true", "isNew.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIsOnSaleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isOnSale equals to
        defaultProductFiltering("isOnSale.equals=" + DEFAULT_IS_ON_SALE, "isOnSale.equals=" + UPDATED_IS_ON_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByIsOnSaleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isOnSale in
        defaultProductFiltering("isOnSale.in=" + DEFAULT_IS_ON_SALE + "," + UPDATED_IS_ON_SALE, "isOnSale.in=" + UPDATED_IS_ON_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByIsOnSaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where isOnSale is not null
        defaultProductFiltering("isOnSale.specified=true", "isOnSale.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating equals to
        defaultProductFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating in
        defaultProductFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating is not null
        defaultProductFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating is greater than or equal to
        defaultProductFiltering(
            "rating.greaterThanOrEqual=" + DEFAULT_RATING,
            "rating.greaterThanOrEqual=" + (DEFAULT_RATING.add(BigDecimal.ONE))
        );
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating is less than or equal to
        defaultProductFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating is less than
        defaultProductFiltering("rating.lessThan=" + (DEFAULT_RATING.add(BigDecimal.ONE)), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where rating is greater than
        defaultProductFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount equals to
        defaultProductFiltering("reviewsCount.equals=" + DEFAULT_REVIEWS_COUNT, "reviewsCount.equals=" + UPDATED_REVIEWS_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount in
        defaultProductFiltering(
            "reviewsCount.in=" + DEFAULT_REVIEWS_COUNT + "," + UPDATED_REVIEWS_COUNT,
            "reviewsCount.in=" + UPDATED_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount is not null
        defaultProductFiltering("reviewsCount.specified=true", "reviewsCount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount is greater than or equal to
        defaultProductFiltering(
            "reviewsCount.greaterThanOrEqual=" + DEFAULT_REVIEWS_COUNT,
            "reviewsCount.greaterThanOrEqual=" + UPDATED_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount is less than or equal to
        defaultProductFiltering(
            "reviewsCount.lessThanOrEqual=" + DEFAULT_REVIEWS_COUNT,
            "reviewsCount.lessThanOrEqual=" + SMALLER_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount is less than
        defaultProductFiltering("reviewsCount.lessThan=" + UPDATED_REVIEWS_COUNT, "reviewsCount.lessThan=" + DEFAULT_REVIEWS_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByReviewsCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where reviewsCount is greater than
        defaultProductFiltering("reviewsCount.greaterThan=" + SMALLER_REVIEWS_COUNT, "reviewsCount.greaterThan=" + DEFAULT_REVIEWS_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount equals to
        defaultProductFiltering("stockCount.equals=" + DEFAULT_STOCK_COUNT, "stockCount.equals=" + UPDATED_STOCK_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount in
        defaultProductFiltering("stockCount.in=" + DEFAULT_STOCK_COUNT + "," + UPDATED_STOCK_COUNT, "stockCount.in=" + UPDATED_STOCK_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount is not null
        defaultProductFiltering("stockCount.specified=true", "stockCount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount is greater than or equal to
        defaultProductFiltering(
            "stockCount.greaterThanOrEqual=" + DEFAULT_STOCK_COUNT,
            "stockCount.greaterThanOrEqual=" + UPDATED_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount is less than or equal to
        defaultProductFiltering("stockCount.lessThanOrEqual=" + DEFAULT_STOCK_COUNT, "stockCount.lessThanOrEqual=" + SMALLER_STOCK_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount is less than
        defaultProductFiltering("stockCount.lessThan=" + UPDATED_STOCK_COUNT, "stockCount.lessThan=" + DEFAULT_STOCK_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByStockCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where stockCount is greater than
        defaultProductFiltering("stockCount.greaterThan=" + SMALLER_STOCK_COUNT, "stockCount.greaterThan=" + DEFAULT_STOCK_COUNT);
    }

    @Test
    @Transactional
    void getAllProductsByInStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inStock equals to
        defaultProductFiltering("inStock.equals=" + DEFAULT_IN_STOCK, "inStock.equals=" + UPDATED_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByInStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inStock in
        defaultProductFiltering("inStock.in=" + DEFAULT_IN_STOCK + "," + UPDATED_IN_STOCK, "inStock.in=" + UPDATED_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByInStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inStock is not null
        defaultProductFiltering("inStock.specified=true", "inStock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMetaTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaTitle equals to
        defaultProductFiltering("metaTitle.equals=" + DEFAULT_META_TITLE, "metaTitle.equals=" + UPDATED_META_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByMetaTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaTitle in
        defaultProductFiltering("metaTitle.in=" + DEFAULT_META_TITLE + "," + UPDATED_META_TITLE, "metaTitle.in=" + UPDATED_META_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByMetaTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaTitle is not null
        defaultProductFiltering("metaTitle.specified=true", "metaTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMetaTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaTitle contains
        defaultProductFiltering("metaTitle.contains=" + DEFAULT_META_TITLE, "metaTitle.contains=" + UPDATED_META_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByMetaTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaTitle does not contain
        defaultProductFiltering("metaTitle.doesNotContain=" + UPDATED_META_TITLE, "metaTitle.doesNotContain=" + DEFAULT_META_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByMetaDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaDescription equals to
        defaultProductFiltering("metaDescription.equals=" + DEFAULT_META_DESCRIPTION, "metaDescription.equals=" + UPDATED_META_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByMetaDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaDescription in
        defaultProductFiltering(
            "metaDescription.in=" + DEFAULT_META_DESCRIPTION + "," + UPDATED_META_DESCRIPTION,
            "metaDescription.in=" + UPDATED_META_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByMetaDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaDescription is not null
        defaultProductFiltering("metaDescription.specified=true", "metaDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMetaDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaDescription contains
        defaultProductFiltering(
            "metaDescription.contains=" + DEFAULT_META_DESCRIPTION,
            "metaDescription.contains=" + UPDATED_META_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByMetaDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaDescription does not contain
        defaultProductFiltering(
            "metaDescription.doesNotContain=" + UPDATED_META_DESCRIPTION,
            "metaDescription.doesNotContain=" + DEFAULT_META_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByMetaKeywordsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaKeywords equals to
        defaultProductFiltering("metaKeywords.equals=" + DEFAULT_META_KEYWORDS, "metaKeywords.equals=" + UPDATED_META_KEYWORDS);
    }

    @Test
    @Transactional
    void getAllProductsByMetaKeywordsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaKeywords in
        defaultProductFiltering(
            "metaKeywords.in=" + DEFAULT_META_KEYWORDS + "," + UPDATED_META_KEYWORDS,
            "metaKeywords.in=" + UPDATED_META_KEYWORDS
        );
    }

    @Test
    @Transactional
    void getAllProductsByMetaKeywordsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaKeywords is not null
        defaultProductFiltering("metaKeywords.specified=true", "metaKeywords.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMetaKeywordsContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaKeywords contains
        defaultProductFiltering("metaKeywords.contains=" + DEFAULT_META_KEYWORDS, "metaKeywords.contains=" + UPDATED_META_KEYWORDS);
    }

    @Test
    @Transactional
    void getAllProductsByMetaKeywordsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where metaKeywords does not contain
        defaultProductFiltering(
            "metaKeywords.doesNotContain=" + UPDATED_META_KEYWORDS,
            "metaKeywords.doesNotContain=" + DEFAULT_META_KEYWORDS
        );
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status equals to
        defaultProductFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status in
        defaultProductFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status is not null
        defaultProductFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight equals to
        defaultProductFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight in
        defaultProductFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is not null
        defaultProductFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than or equal to
        defaultProductFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than or equal to
        defaultProductFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than
        defaultProductFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than
        defaultProductFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByDimensionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where dimensions equals to
        defaultProductFiltering("dimensions.equals=" + DEFAULT_DIMENSIONS, "dimensions.equals=" + UPDATED_DIMENSIONS);
    }

    @Test
    @Transactional
    void getAllProductsByDimensionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where dimensions in
        defaultProductFiltering("dimensions.in=" + DEFAULT_DIMENSIONS + "," + UPDATED_DIMENSIONS, "dimensions.in=" + UPDATED_DIMENSIONS);
    }

    @Test
    @Transactional
    void getAllProductsByDimensionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where dimensions is not null
        defaultProductFiltering("dimensions.specified=true", "dimensions.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDimensionsContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where dimensions contains
        defaultProductFiltering("dimensions.contains=" + DEFAULT_DIMENSIONS, "dimensions.contains=" + UPDATED_DIMENSIONS);
    }

    @Test
    @Transactional
    void getAllProductsByDimensionsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where dimensions does not contain
        defaultProductFiltering("dimensions.doesNotContain=" + UPDATED_DIMENSIONS, "dimensions.doesNotContain=" + DEFAULT_DIMENSIONS);
    }

    @Test
    @Transactional
    void getAllProductsByPromotionsIsEqualToSomething() throws Exception {
        Promotion promotions;
        if (TestUtil.findAll(em, Promotion.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            promotions = PromotionResourceIT.createEntity();
        } else {
            promotions = TestUtil.findAll(em, Promotion.class).get(0);
        }
        em.persist(promotions);
        em.flush();
        product.addPromotions(promotions);
        productRepository.saveAndFlush(product);
        Long promotionsId = promotions.getId();
        // Get all the productList where promotions equals to promotionsId
        defaultProductShouldBeFound("promotionsId.equals=" + promotionsId);

        // Get all the productList where promotions equals to (promotionsId + 1)
        defaultProductShouldNotBeFound("promotionsId.equals=" + (promotionsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            brand = BrandResourceIT.createEntity();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        product.setBrand(brand);
        productRepository.saveAndFlush(product);
        Long brandId = brand.getId();
        // Get all the productList where brand equals to brandId
        defaultProductShouldBeFound("brandId.equals=" + brandId);

        // Get all the productList where brand equals to (brandId + 1)
        defaultProductShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();
        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to (categoryId + 1)
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByWishlistedIsEqualToSomething() throws Exception {
        UserProfile wishlisted;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            wishlisted = UserProfileResourceIT.createEntity();
        } else {
            wishlisted = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(wishlisted);
        em.flush();
        product.addWishlisted(wishlisted);
        productRepository.saveAndFlush(product);
        Long wishlistedId = wishlisted.getId();
        // Get all the productList where wishlisted equals to wishlistedId
        defaultProductShouldBeFound("wishlistedId.equals=" + wishlistedId);

        // Get all the productList where wishlisted equals to (wishlistedId + 1)
        defaultProductShouldNotBeFound("wishlistedId.equals=" + (wishlistedId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByApplicablePromotionsIsEqualToSomething() throws Exception {
        Promotion applicablePromotions;
        if (TestUtil.findAll(em, Promotion.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            applicablePromotions = PromotionResourceIT.createEntity();
        } else {
            applicablePromotions = TestUtil.findAll(em, Promotion.class).get(0);
        }
        em.persist(applicablePromotions);
        em.flush();
        product.addApplicablePromotions(applicablePromotions);
        productRepository.saveAndFlush(product);
        Long applicablePromotionsId = applicablePromotions.getId();
        // Get all the productList where applicablePromotions equals to applicablePromotionsId
        defaultProductShouldBeFound("applicablePromotionsId.equals=" + applicablePromotionsId);

        // Get all the productList where applicablePromotions equals to (applicablePromotionsId + 1)
        defaultProductShouldNotBeFound("applicablePromotionsId.equals=" + (applicablePromotionsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByFeaturedInCategoriesIsEqualToSomething() throws Exception {
        Category featuredInCategories;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            featuredInCategories = CategoryResourceIT.createEntity();
        } else {
            featuredInCategories = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(featuredInCategories);
        em.flush();
        product.addFeaturedInCategories(featuredInCategories);
        productRepository.saveAndFlush(product);
        Long featuredInCategoriesId = featuredInCategories.getId();
        // Get all the productList where featuredInCategories equals to featuredInCategoriesId
        defaultProductShouldBeFound("featuredInCategoriesId.equals=" + featuredInCategoriesId);

        // Get all the productList where featuredInCategories equals to (featuredInCategoriesId + 1)
        defaultProductShouldNotBeFound("featuredInCategoriesId.equals=" + (featuredInCategoriesId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].originalPrice").value(hasItem(sameNumber(DEFAULT_ORIGINAL_PRICE))))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW)))
            .andExpect(jsonPath("$.[*].isOnSale").value(hasItem(DEFAULT_IS_ON_SALE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(sameNumber(DEFAULT_RATING))))
            .andExpect(jsonPath("$.[*].reviewsCount").value(hasItem(DEFAULT_REVIEWS_COUNT)))
            .andExpect(jsonPath("$.[*].stockCount").value(hasItem(DEFAULT_STOCK_COUNT)))
            .andExpect(jsonPath("$.[*].inStock").value(hasItem(DEFAULT_IN_STOCK)))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES)))
            .andExpect(jsonPath("$.[*].metaTitle").value(hasItem(DEFAULT_META_TITLE)))
            .andExpect(jsonPath("$.[*].metaDescription").value(hasItem(DEFAULT_META_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].metaKeywords").value(hasItem(DEFAULT_META_KEYWORDS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].dimensions").value(hasItem(DEFAULT_DIMENSIONS)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .sku(UPDATED_SKU)
            .isNew(UPDATED_IS_NEW)
            .isOnSale(UPDATED_IS_ON_SALE)
            .rating(UPDATED_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .stockCount(UPDATED_STOCK_COUNT)
            .inStock(UPDATED_IN_STOCK)
            .features(UPDATED_FEATURES)
            .metaTitle(UPDATED_META_TITLE)
            .metaDescription(UPDATED_META_DESCRIPTION)
            .metaKeywords(UPDATED_META_KEYWORDS)
            .status(UPDATED_STATUS)
            .weight(UPDATED_WEIGHT)
            .dimensions(UPDATED_DIMENSIONS);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .isNew(UPDATED_IS_NEW)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .stockCount(UPDATED_STOCK_COUNT)
            .features(UPDATED_FEATURES)
            .weight(UPDATED_WEIGHT);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .sku(UPDATED_SKU)
            .isNew(UPDATED_IS_NEW)
            .isOnSale(UPDATED_IS_ON_SALE)
            .rating(UPDATED_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .stockCount(UPDATED_STOCK_COUNT)
            .inStock(UPDATED_IN_STOCK)
            .features(UPDATED_FEATURES)
            .metaTitle(UPDATED_META_TITLE)
            .metaDescription(UPDATED_META_DESCRIPTION)
            .metaKeywords(UPDATED_META_KEYWORDS)
            .status(UPDATED_STATUS)
            .weight(UPDATED_WEIGHT)
            .dimensions(UPDATED_DIMENSIONS);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
