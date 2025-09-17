package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.ProductPromotionAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.ProductPromotion;
import com.evaradrip.commerce.repository.ProductPromotionRepository;
import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
import com.evaradrip.commerce.service.mapper.ProductPromotionMapper;
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
 * Integration tests for the {@link ProductPromotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductPromotionResourceIT {

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Boolean DEFAULT_IS_EXCLUSIVE = false;
    private static final Boolean UPDATED_IS_EXCLUSIVE = true;

    private static final String ENTITY_API_URL = "/api/product-promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductPromotionRepository productPromotionRepository;

    @Autowired
    private ProductPromotionMapper productPromotionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPromotionMockMvc;

    private ProductPromotion productPromotion;

    private ProductPromotion insertedProductPromotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPromotion createEntity() {
        return new ProductPromotion().priority(DEFAULT_PRIORITY).isExclusive(DEFAULT_IS_EXCLUSIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPromotion createUpdatedEntity() {
        return new ProductPromotion().priority(UPDATED_PRIORITY).isExclusive(UPDATED_IS_EXCLUSIVE);
    }

    @BeforeEach
    void initTest() {
        productPromotion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProductPromotion != null) {
            productPromotionRepository.delete(insertedProductPromotion);
            insertedProductPromotion = null;
        }
    }

    @Test
    @Transactional
    void createProductPromotion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);
        var returnedProductPromotionDTO = om.readValue(
            restProductPromotionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPromotionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductPromotionDTO.class
        );

        // Validate the ProductPromotion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductPromotion = productPromotionMapper.toEntity(returnedProductPromotionDTO);
        assertProductPromotionUpdatableFieldsEquals(returnedProductPromotion, getPersistedProductPromotion(returnedProductPromotion));

        insertedProductPromotion = returnedProductPromotion;
    }

    @Test
    @Transactional
    void createProductPromotionWithExistingId() throws Exception {
        // Create the ProductPromotion with an existing ID
        productPromotion.setId(1L);
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPromotionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductPromotions() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPromotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isExclusive").value(hasItem(DEFAULT_IS_EXCLUSIVE)));
    }

    @Test
    @Transactional
    void getProductPromotion() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get the productPromotion
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL_ID, productPromotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPromotion.getId().intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.isExclusive").value(DEFAULT_IS_EXCLUSIVE));
    }

    @Test
    @Transactional
    void getProductPromotionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        Long id = productPromotion.getId();

        defaultProductPromotionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductPromotionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductPromotionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority equals to
        defaultProductPromotionFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority in
        defaultProductPromotionFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority is not null
        defaultProductPromotionFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority is greater than or equal to
        defaultProductPromotionFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority is less than or equal to
        defaultProductPromotionFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority is less than
        defaultProductPromotionFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where priority is greater than
        defaultProductPromotionFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByIsExclusiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where isExclusive equals to
        defaultProductPromotionFiltering("isExclusive.equals=" + DEFAULT_IS_EXCLUSIVE, "isExclusive.equals=" + UPDATED_IS_EXCLUSIVE);
    }

    @Test
    @Transactional
    void getAllProductPromotionsByIsExclusiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where isExclusive in
        defaultProductPromotionFiltering(
            "isExclusive.in=" + DEFAULT_IS_EXCLUSIVE + "," + UPDATED_IS_EXCLUSIVE,
            "isExclusive.in=" + UPDATED_IS_EXCLUSIVE
        );
    }

    @Test
    @Transactional
    void getAllProductPromotionsByIsExclusiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        // Get all the productPromotionList where isExclusive is not null
        defaultProductPromotionFiltering("isExclusive.specified=true", "isExclusive.specified=false");
    }

    private void defaultProductPromotionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductPromotionShouldBeFound(shouldBeFound);
        defaultProductPromotionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductPromotionShouldBeFound(String filter) throws Exception {
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPromotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isExclusive").value(hasItem(DEFAULT_IS_EXCLUSIVE)));

        // Check, that the count call also returns 1
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductPromotionShouldNotBeFound(String filter) throws Exception {
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductPromotion() throws Exception {
        // Get the productPromotion
        restProductPromotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPromotion() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPromotion
        ProductPromotion updatedProductPromotion = productPromotionRepository.findById(productPromotion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductPromotion are not directly saved in db
        em.detach(updatedProductPromotion);
        updatedProductPromotion.priority(UPDATED_PRIORITY).isExclusive(UPDATED_IS_EXCLUSIVE);
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(updatedProductPromotion);

        restProductPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPromotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPromotionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductPromotionToMatchAllProperties(updatedProductPromotion);
    }

    @Test
    @Transactional
    void putNonExistingProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPromotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productPromotionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPromotionWithPatch() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPromotion using partial update
        ProductPromotion partialUpdatedProductPromotion = new ProductPromotion();
        partialUpdatedProductPromotion.setId(productPromotion.getId());

        partialUpdatedProductPromotion.isExclusive(UPDATED_IS_EXCLUSIVE);

        restProductPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPromotion))
            )
            .andExpect(status().isOk());

        // Validate the ProductPromotion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPromotionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductPromotion, productPromotion),
            getPersistedProductPromotion(productPromotion)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductPromotionWithPatch() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productPromotion using partial update
        ProductPromotion partialUpdatedProductPromotion = new ProductPromotion();
        partialUpdatedProductPromotion.setId(productPromotion.getId());

        partialUpdatedProductPromotion.priority(UPDATED_PRIORITY).isExclusive(UPDATED_IS_EXCLUSIVE);

        restProductPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductPromotion))
            )
            .andExpect(status().isOk());

        // Validate the ProductPromotion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductPromotionUpdatableFieldsEquals(
            partialUpdatedProductPromotion,
            getPersistedProductPromotion(partialUpdatedProductPromotion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPromotionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPromotion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productPromotion.setId(longCount.incrementAndGet());

        // Create the ProductPromotion
        ProductPromotionDTO productPromotionDTO = productPromotionMapper.toDto(productPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPromotionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productPromotionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPromotion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPromotion() throws Exception {
        // Initialize the database
        insertedProductPromotion = productPromotionRepository.saveAndFlush(productPromotion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productPromotion
        restProductPromotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPromotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productPromotionRepository.count();
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

    protected ProductPromotion getPersistedProductPromotion(ProductPromotion productPromotion) {
        return productPromotionRepository.findById(productPromotion.getId()).orElseThrow();
    }

    protected void assertPersistedProductPromotionToMatchAllProperties(ProductPromotion expectedProductPromotion) {
        assertProductPromotionAllPropertiesEquals(expectedProductPromotion, getPersistedProductPromotion(expectedProductPromotion));
    }

    protected void assertPersistedProductPromotionToMatchUpdatableProperties(ProductPromotion expectedProductPromotion) {
        assertProductPromotionAllUpdatablePropertiesEquals(
            expectedProductPromotion,
            getPersistedProductPromotion(expectedProductPromotion)
        );
    }
}
