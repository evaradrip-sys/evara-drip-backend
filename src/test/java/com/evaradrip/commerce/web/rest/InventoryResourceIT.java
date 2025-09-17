package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.InventoryAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evaradrip.commerce.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Inventory;
import com.evaradrip.commerce.domain.Product;
import com.evaradrip.commerce.repository.InventoryRepository;
import com.evaradrip.commerce.service.InventoryService;
import com.evaradrip.commerce.service.dto.InventoryDTO;
import com.evaradrip.commerce.service.mapper.InventoryMapper;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventoryResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_RESERVED_QUANTITY = 0;
    private static final Integer UPDATED_RESERVED_QUANTITY = 1;
    private static final Integer SMALLER_RESERVED_QUANTITY = 0 - 1;

    private static final String DEFAULT_WAREHOUSE = "AAAAAAAAAA";
    private static final String UPDATED_WAREHOUSE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_RESTOCKED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_RESTOCKED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_RESTOCKED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_REORDER_LEVEL = 0;
    private static final Integer UPDATED_REORDER_LEVEL = 1;
    private static final Integer SMALLER_REORDER_LEVEL = 0 - 1;

    private static final Integer DEFAULT_REORDER_QUANTITY = 0;
    private static final Integer UPDATED_REORDER_QUANTITY = 1;
    private static final Integer SMALLER_REORDER_QUANTITY = 0 - 1;

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryRepository inventoryRepositoryMock;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Mock
    private InventoryService inventoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    private Inventory insertedInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .quantity(DEFAULT_QUANTITY)
            .reservedQuantity(DEFAULT_RESERVED_QUANTITY)
            .warehouse(DEFAULT_WAREHOUSE)
            .lastRestocked(DEFAULT_LAST_RESTOCKED)
            .reorderLevel(DEFAULT_REORDER_LEVEL)
            .reorderQuantity(DEFAULT_REORDER_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        inventory.setProduct(product);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory updatedInventory = new Inventory()
            .quantity(UPDATED_QUANTITY)
            .reservedQuantity(UPDATED_RESERVED_QUANTITY)
            .warehouse(UPDATED_WAREHOUSE)
            .lastRestocked(UPDATED_LAST_RESTOCKED)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .reorderQuantity(UPDATED_REORDER_QUANTITY);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedInventory.setProduct(product);
        return updatedInventory;
    }

    @BeforeEach
    void initTest() {
        inventory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventory != null) {
            inventoryRepository.delete(insertedInventory);
            insertedInventory = null;
        }
    }

    @Test
    @Transactional
    void createInventory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        var returnedInventoryDTO = om.readValue(
            restInventoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryDTO.class
        );

        // Validate the Inventory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventory = inventoryMapper.toEntity(returnedInventoryDTO);
        assertInventoryUpdatableFieldsEquals(returnedInventory, getPersistedInventory(returnedInventory));

        insertedInventory = returnedInventory;
    }

    @Test
    @Transactional
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventory.setId(1L);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventory.setQuantity(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventories() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].reservedQuantity").value(hasItem(DEFAULT_RESERVED_QUANTITY)))
            .andExpect(jsonPath("$.[*].warehouse").value(hasItem(DEFAULT_WAREHOUSE)))
            .andExpect(jsonPath("$.[*].lastRestocked").value(hasItem(sameInstant(DEFAULT_LAST_RESTOCKED))))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].reorderQuantity").value(hasItem(DEFAULT_REORDER_QUANTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInventoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.reservedQuantity").value(DEFAULT_RESERVED_QUANTITY))
            .andExpect(jsonPath("$.warehouse").value(DEFAULT_WAREHOUSE))
            .andExpect(jsonPath("$.lastRestocked").value(sameInstant(DEFAULT_LAST_RESTOCKED)))
            .andExpect(jsonPath("$.reorderLevel").value(DEFAULT_REORDER_LEVEL))
            .andExpect(jsonPath("$.reorderQuantity").value(DEFAULT_REORDER_QUANTITY));
    }

    @Test
    @Transactional
    void getInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        Long id = inventory.getId();

        defaultInventoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity equals to
        defaultInventoryFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity in
        defaultInventoryFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity is not null
        defaultInventoryFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity is greater than or equal to
        defaultInventoryFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity is less than or equal to
        defaultInventoryFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity is less than
        defaultInventoryFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where quantity is greater than
        defaultInventoryFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity equals to
        defaultInventoryFiltering(
            "reservedQuantity.equals=" + DEFAULT_RESERVED_QUANTITY,
            "reservedQuantity.equals=" + UPDATED_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity in
        defaultInventoryFiltering(
            "reservedQuantity.in=" + DEFAULT_RESERVED_QUANTITY + "," + UPDATED_RESERVED_QUANTITY,
            "reservedQuantity.in=" + UPDATED_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity is not null
        defaultInventoryFiltering("reservedQuantity.specified=true", "reservedQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity is greater than or equal to
        defaultInventoryFiltering(
            "reservedQuantity.greaterThanOrEqual=" + DEFAULT_RESERVED_QUANTITY,
            "reservedQuantity.greaterThanOrEqual=" + UPDATED_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity is less than or equal to
        defaultInventoryFiltering(
            "reservedQuantity.lessThanOrEqual=" + DEFAULT_RESERVED_QUANTITY,
            "reservedQuantity.lessThanOrEqual=" + SMALLER_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity is less than
        defaultInventoryFiltering(
            "reservedQuantity.lessThan=" + UPDATED_RESERVED_QUANTITY,
            "reservedQuantity.lessThan=" + DEFAULT_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReservedQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reservedQuantity is greater than
        defaultInventoryFiltering(
            "reservedQuantity.greaterThan=" + SMALLER_RESERVED_QUANTITY,
            "reservedQuantity.greaterThan=" + DEFAULT_RESERVED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByWarehouseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where warehouse equals to
        defaultInventoryFiltering("warehouse.equals=" + DEFAULT_WAREHOUSE, "warehouse.equals=" + UPDATED_WAREHOUSE);
    }

    @Test
    @Transactional
    void getAllInventoriesByWarehouseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where warehouse in
        defaultInventoryFiltering("warehouse.in=" + DEFAULT_WAREHOUSE + "," + UPDATED_WAREHOUSE, "warehouse.in=" + UPDATED_WAREHOUSE);
    }

    @Test
    @Transactional
    void getAllInventoriesByWarehouseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where warehouse is not null
        defaultInventoryFiltering("warehouse.specified=true", "warehouse.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByWarehouseContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where warehouse contains
        defaultInventoryFiltering("warehouse.contains=" + DEFAULT_WAREHOUSE, "warehouse.contains=" + UPDATED_WAREHOUSE);
    }

    @Test
    @Transactional
    void getAllInventoriesByWarehouseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where warehouse does not contain
        defaultInventoryFiltering("warehouse.doesNotContain=" + UPDATED_WAREHOUSE, "warehouse.doesNotContain=" + DEFAULT_WAREHOUSE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked equals to
        defaultInventoryFiltering("lastRestocked.equals=" + DEFAULT_LAST_RESTOCKED, "lastRestocked.equals=" + UPDATED_LAST_RESTOCKED);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked in
        defaultInventoryFiltering(
            "lastRestocked.in=" + DEFAULT_LAST_RESTOCKED + "," + UPDATED_LAST_RESTOCKED,
            "lastRestocked.in=" + UPDATED_LAST_RESTOCKED
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked is not null
        defaultInventoryFiltering("lastRestocked.specified=true", "lastRestocked.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked is greater than or equal to
        defaultInventoryFiltering(
            "lastRestocked.greaterThanOrEqual=" + DEFAULT_LAST_RESTOCKED,
            "lastRestocked.greaterThanOrEqual=" + UPDATED_LAST_RESTOCKED
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked is less than or equal to
        defaultInventoryFiltering(
            "lastRestocked.lessThanOrEqual=" + DEFAULT_LAST_RESTOCKED,
            "lastRestocked.lessThanOrEqual=" + SMALLER_LAST_RESTOCKED
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked is less than
        defaultInventoryFiltering("lastRestocked.lessThan=" + UPDATED_LAST_RESTOCKED, "lastRestocked.lessThan=" + DEFAULT_LAST_RESTOCKED);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastRestockedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastRestocked is greater than
        defaultInventoryFiltering(
            "lastRestocked.greaterThan=" + SMALLER_LAST_RESTOCKED,
            "lastRestocked.greaterThan=" + DEFAULT_LAST_RESTOCKED
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel equals to
        defaultInventoryFiltering("reorderLevel.equals=" + DEFAULT_REORDER_LEVEL, "reorderLevel.equals=" + UPDATED_REORDER_LEVEL);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel in
        defaultInventoryFiltering(
            "reorderLevel.in=" + DEFAULT_REORDER_LEVEL + "," + UPDATED_REORDER_LEVEL,
            "reorderLevel.in=" + UPDATED_REORDER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel is not null
        defaultInventoryFiltering("reorderLevel.specified=true", "reorderLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel is greater than or equal to
        defaultInventoryFiltering(
            "reorderLevel.greaterThanOrEqual=" + DEFAULT_REORDER_LEVEL,
            "reorderLevel.greaterThanOrEqual=" + UPDATED_REORDER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel is less than or equal to
        defaultInventoryFiltering(
            "reorderLevel.lessThanOrEqual=" + DEFAULT_REORDER_LEVEL,
            "reorderLevel.lessThanOrEqual=" + SMALLER_REORDER_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel is less than
        defaultInventoryFiltering("reorderLevel.lessThan=" + UPDATED_REORDER_LEVEL, "reorderLevel.lessThan=" + DEFAULT_REORDER_LEVEL);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderLevel is greater than
        defaultInventoryFiltering("reorderLevel.greaterThan=" + SMALLER_REORDER_LEVEL, "reorderLevel.greaterThan=" + DEFAULT_REORDER_LEVEL);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity equals to
        defaultInventoryFiltering(
            "reorderQuantity.equals=" + DEFAULT_REORDER_QUANTITY,
            "reorderQuantity.equals=" + UPDATED_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity in
        defaultInventoryFiltering(
            "reorderQuantity.in=" + DEFAULT_REORDER_QUANTITY + "," + UPDATED_REORDER_QUANTITY,
            "reorderQuantity.in=" + UPDATED_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity is not null
        defaultInventoryFiltering("reorderQuantity.specified=true", "reorderQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity is greater than or equal to
        defaultInventoryFiltering(
            "reorderQuantity.greaterThanOrEqual=" + DEFAULT_REORDER_QUANTITY,
            "reorderQuantity.greaterThanOrEqual=" + UPDATED_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity is less than or equal to
        defaultInventoryFiltering(
            "reorderQuantity.lessThanOrEqual=" + DEFAULT_REORDER_QUANTITY,
            "reorderQuantity.lessThanOrEqual=" + SMALLER_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity is less than
        defaultInventoryFiltering(
            "reorderQuantity.lessThan=" + UPDATED_REORDER_QUANTITY,
            "reorderQuantity.lessThan=" + DEFAULT_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderQuantity is greater than
        defaultInventoryFiltering(
            "reorderQuantity.greaterThan=" + SMALLER_REORDER_QUANTITY,
            "reorderQuantity.greaterThan=" + DEFAULT_REORDER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllInventoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inventoryRepository.saveAndFlush(inventory);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        inventory.setProduct(product);
        inventoryRepository.saveAndFlush(inventory);
        Long productId = product.getId();
        // Get all the inventoryList where product equals to productId
        defaultInventoryShouldBeFound("productId.equals=" + productId);

        // Get all the inventoryList where product equals to (productId + 1)
        defaultInventoryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultInventoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryShouldBeFound(shouldBeFound);
        defaultInventoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryShouldBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].reservedQuantity").value(hasItem(DEFAULT_RESERVED_QUANTITY)))
            .andExpect(jsonPath("$.[*].warehouse").value(hasItem(DEFAULT_WAREHOUSE)))
            .andExpect(jsonPath("$.[*].lastRestocked").value(hasItem(sameInstant(DEFAULT_LAST_RESTOCKED))))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].reorderQuantity").value(hasItem(DEFAULT_REORDER_QUANTITY)));

        // Check, that the count call also returns 1
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryShouldNotBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory
            .quantity(UPDATED_QUANTITY)
            .reservedQuantity(UPDATED_RESERVED_QUANTITY)
            .warehouse(UPDATED_WAREHOUSE)
            .lastRestocked(UPDATED_LAST_RESTOCKED)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .reorderQuantity(UPDATED_REORDER_QUANTITY);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(updatedInventory);

        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryToMatchAllProperties(updatedInventory);
    }

    @Test
    @Transactional
    void putNonExistingInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory
            .reservedQuantity(UPDATED_RESERVED_QUANTITY)
            .warehouse(UPDATED_WAREHOUSE)
            .reorderQuantity(UPDATED_REORDER_QUANTITY);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventory, inventory),
            getPersistedInventory(inventory)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory
            .quantity(UPDATED_QUANTITY)
            .reservedQuantity(UPDATED_RESERVED_QUANTITY)
            .warehouse(UPDATED_WAREHOUSE)
            .lastRestocked(UPDATED_LAST_RESTOCKED)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .reorderQuantity(UPDATED_REORDER_QUANTITY);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryUpdatableFieldsEquals(partialUpdatedInventory, getPersistedInventory(partialUpdatedInventory));
    }

    @Test
    @Transactional
    void patchNonExistingInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventory.setId(longCount.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventory() throws Exception {
        // Initialize the database
        insertedInventory = inventoryRepository.saveAndFlush(inventory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventory
        restInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryRepository.count();
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

    protected Inventory getPersistedInventory(Inventory inventory) {
        return inventoryRepository.findById(inventory.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryToMatchAllProperties(Inventory expectedInventory) {
        assertInventoryAllPropertiesEquals(expectedInventory, getPersistedInventory(expectedInventory));
    }

    protected void assertPersistedInventoryToMatchUpdatableProperties(Inventory expectedInventory) {
        assertInventoryAllUpdatablePropertiesEquals(expectedInventory, getPersistedInventory(expectedInventory));
    }
}
