package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.CartItemAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Cart;
import com.evaradrip.commerce.domain.CartItem;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.domain.ProductVariant;
import com.evaradrip.commerce.repository.CartItemRepository;
import com.evaradrip.commerce.service.CartItemService;
import com.evaradrip.commerce.service.dto.CartItemDTO;
import com.evaradrip.commerce.service.mapper.CartItemMapper;
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
 * Integration tests for the {@link CartItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CartItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_ADDED_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_ADDED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_ADDED_PRICE = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/cart-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemRepository cartItemRepositoryMock;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Mock
    private CartItemService cartItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartItemMockMvc;

    private CartItem cartItem;

    private CartItem insertedCartItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createEntity(EntityManager em) {
        CartItem cartItem = new CartItem().quantity(DEFAULT_QUANTITY).addedPrice(DEFAULT_ADDED_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        cartItem.setProduct(product);
        // Add required entity
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            cart = CartResourceIT.createEntity();
            em.persist(cart);
            em.flush();
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        cartItem.setCart(cart);
        return cartItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createUpdatedEntity(EntityManager em) {
        CartItem updatedCartItem = new CartItem().quantity(UPDATED_QUANTITY).addedPrice(UPDATED_ADDED_PRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedCartItem.setProduct(product);
        // Add required entity
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            cart = CartResourceIT.createUpdatedEntity();
            em.persist(cart);
            em.flush();
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        updatedCartItem.setCart(cart);
        return updatedCartItem;
    }

    @BeforeEach
    void initTest() {
        cartItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCartItem != null) {
            cartItemRepository.delete(insertedCartItem);
            insertedCartItem = null;
        }
    }

    @Test
    @Transactional
    void createCartItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);
        var returnedCartItemDTO = om.readValue(
            restCartItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CartItemDTO.class
        );

        // Validate the CartItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCartItem = cartItemMapper.toEntity(returnedCartItemDTO);
        assertCartItemUpdatableFieldsEquals(returnedCartItem, getPersistedCartItem(returnedCartItem));

        insertedCartItem = returnedCartItem;
    }

    @Test
    @Transactional
    void createCartItemWithExistingId() throws Exception {
        // Create the CartItem with an existing ID
        cartItem.setId(1L);
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setQuantity(null);

        // Create the CartItem, which fails.
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setAddedPrice(null);

        // Create the CartItem, which fails.
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCartItems() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].addedPrice").value(hasItem(sameNumber(DEFAULT_ADDED_PRICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCartItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cartItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCartItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cartItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCartItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cartItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCartItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cartItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get the cartItem
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL_ID, cartItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.addedPrice").value(sameNumber(DEFAULT_ADDED_PRICE)));
    }

    @Test
    @Transactional
    void getCartItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        Long id = cartItem.getId();

        defaultCartItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCartItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCartItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity equals to
        defaultCartItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity in
        defaultCartItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is not null
        defaultCartItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is greater than or equal to
        defaultCartItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is less than or equal to
        defaultCartItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is less than
        defaultCartItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is greater than
        defaultCartItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice equals to
        defaultCartItemFiltering("addedPrice.equals=" + DEFAULT_ADDED_PRICE, "addedPrice.equals=" + UPDATED_ADDED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice in
        defaultCartItemFiltering(
            "addedPrice.in=" + DEFAULT_ADDED_PRICE + "," + UPDATED_ADDED_PRICE,
            "addedPrice.in=" + UPDATED_ADDED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice is not null
        defaultCartItemFiltering("addedPrice.specified=true", "addedPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice is greater than or equal to
        defaultCartItemFiltering(
            "addedPrice.greaterThanOrEqual=" + DEFAULT_ADDED_PRICE,
            "addedPrice.greaterThanOrEqual=" + UPDATED_ADDED_PRICE
        );
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice is less than or equal to
        defaultCartItemFiltering("addedPrice.lessThanOrEqual=" + DEFAULT_ADDED_PRICE, "addedPrice.lessThanOrEqual=" + SMALLER_ADDED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice is less than
        defaultCartItemFiltering("addedPrice.lessThan=" + UPDATED_ADDED_PRICE, "addedPrice.lessThan=" + DEFAULT_ADDED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByAddedPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where addedPrice is greater than
        defaultCartItemFiltering("addedPrice.greaterThan=" + SMALLER_ADDED_PRICE, "addedPrice.greaterThan=" + DEFAULT_ADDED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            cartItemRepository.saveAndFlush(cartItem);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        cartItem.setProduct(product);
        cartItemRepository.saveAndFlush(cartItem);
        Long productId = product.getId();
        // Get all the cartItemList where product equals to productId
        defaultCartItemShouldBeFound("productId.equals=" + productId);

        // Get all the cartItemList where product equals to (productId + 1)
        defaultCartItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllCartItemsByVariantIsEqualToSomething() throws Exception {
        ProductVariant variant;
        if (TestUtil.findAll(em, ProductVariant.class).isEmpty()) {
            cartItemRepository.saveAndFlush(cartItem);
            variant = ProductVariantResourceIT.createEntity(em);
        } else {
            variant = TestUtil.findAll(em, ProductVariant.class).get(0);
        }
        em.persist(variant);
        em.flush();
        cartItem.setVariant(variant);
        cartItemRepository.saveAndFlush(cartItem);
        Long variantId = variant.getId();
        // Get all the cartItemList where variant equals to variantId
        defaultCartItemShouldBeFound("variantId.equals=" + variantId);

        // Get all the cartItemList where variant equals to (variantId + 1)
        defaultCartItemShouldNotBeFound("variantId.equals=" + (variantId + 1));
    }

    @Test
    @Transactional
    void getAllCartItemsByCartIsEqualToSomething() throws Exception {
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            cartItemRepository.saveAndFlush(cartItem);
            cart = CartResourceIT.createEntity();
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        em.persist(cart);
        em.flush();
        cartItem.setCart(cart);
        cartItemRepository.saveAndFlush(cartItem);
        Long cartId = cart.getId();
        // Get all the cartItemList where cart equals to cartId
        defaultCartItemShouldBeFound("cartId.equals=" + cartId);

        // Get all the cartItemList where cart equals to (cartId + 1)
        defaultCartItemShouldNotBeFound("cartId.equals=" + (cartId + 1));
    }

    private void defaultCartItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCartItemShouldBeFound(shouldBeFound);
        defaultCartItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartItemShouldBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].addedPrice").value(hasItem(sameNumber(DEFAULT_ADDED_PRICE))));

        // Check, that the count call also returns 1
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartItemShouldNotBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCartItem() throws Exception {
        // Get the cartItem
        restCartItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem
        CartItem updatedCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCartItem are not directly saved in db
        em.detach(updatedCartItem);
        updatedCartItem.quantity(UPDATED_QUANTITY).addedPrice(UPDATED_ADDED_PRICE);
        CartItemDTO cartItemDTO = cartItemMapper.toDto(updatedCartItem);

        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cartItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCartItemToMatchAllProperties(updatedCartItem);
    }

    @Test
    @Transactional
    void putNonExistingCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem.quantity(UPDATED_QUANTITY).addedPrice(UPDATED_ADDED_PRICE);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCartItem, cartItem), getPersistedCartItem(cartItem));
    }

    @Test
    @Transactional
    void fullUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem.quantity(UPDATED_QUANTITY).addedPrice(UPDATED_ADDED_PRICE);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartItemUpdatableFieldsEquals(partialUpdatedCartItem, getPersistedCartItem(partialUpdatedCartItem));
    }

    @Test
    @Transactional
    void patchNonExistingCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cartItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cartItem
        restCartItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cartItemRepository.count();
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

    protected CartItem getPersistedCartItem(CartItem cartItem) {
        return cartItemRepository.findById(cartItem.getId()).orElseThrow();
    }

    protected void assertPersistedCartItemToMatchAllProperties(CartItem expectedCartItem) {
        assertCartItemAllPropertiesEquals(expectedCartItem, getPersistedCartItem(expectedCartItem));
    }

    protected void assertPersistedCartItemToMatchUpdatableProperties(CartItem expectedCartItem) {
        assertCartItemAllUpdatablePropertiesEquals(expectedCartItem, getPersistedCartItem(expectedCartItem));
    }
}
