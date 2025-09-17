package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.BrandAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Brand;
import com.evaradrip.commerce.repository.BrandRepository;
import com.evaradrip.commerce.service.dto.BrandDTO;
import com.evaradrip.commerce.service.mapper.BrandMapper;
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
 * Integration tests for the {@link BrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrandMockMvc;

    private Brand brand;

    private Brand insertedBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createEntity() {
        return new Brand().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).logoUrl(DEFAULT_LOGO_URL).isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createUpdatedEntity() {
        return new Brand().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).logoUrl(UPDATED_LOGO_URL).isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        brand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBrand != null) {
            brandRepository.delete(insertedBrand);
            insertedBrand = null;
        }
    }

    @Test
    @Transactional
    void createBrand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);
        var returnedBrandDTO = om.readValue(
            restBrandMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BrandDTO.class
        );

        // Validate the Brand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBrand = brandMapper.toEntity(returnedBrandDTO);
        assertBrandUpdatableFieldsEquals(returnedBrand, getPersistedBrand(returnedBrand));

        insertedBrand = returnedBrand;
    }

    @Test
    @Transactional
    void createBrandWithExistingId() throws Exception {
        // Create the Brand with an existing ID
        brand.setId(1L);
        BrandDTO brandDTO = brandMapper.toDto(brand);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        brand.setName(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBrands() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getBrand() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get the brand
        restBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, brand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(brand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getBrandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        Long id = brand.getId();

        defaultBrandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBrandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBrandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBrandsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where name equals to
        defaultBrandFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBrandsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where name in
        defaultBrandFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBrandsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where name is not null
        defaultBrandFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where name contains
        defaultBrandFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBrandsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where name does not contain
        defaultBrandFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBrandsByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where logoUrl equals to
        defaultBrandFiltering("logoUrl.equals=" + DEFAULT_LOGO_URL, "logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllBrandsByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where logoUrl in
        defaultBrandFiltering("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL, "logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllBrandsByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where logoUrl is not null
        defaultBrandFiltering("logoUrl.specified=true", "logoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByLogoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where logoUrl contains
        defaultBrandFiltering("logoUrl.contains=" + DEFAULT_LOGO_URL, "logoUrl.contains=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllBrandsByLogoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where logoUrl does not contain
        defaultBrandFiltering("logoUrl.doesNotContain=" + UPDATED_LOGO_URL, "logoUrl.doesNotContain=" + DEFAULT_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllBrandsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where isActive equals to
        defaultBrandFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBrandsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where isActive in
        defaultBrandFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBrandsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        // Get all the brandList where isActive is not null
        defaultBrandFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultBrandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBrandShouldBeFound(shouldBeFound);
        defaultBrandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBrandShouldBeFound(String filter) throws Exception {
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBrandShouldNotBeFound(String filter) throws Exception {
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBrand() throws Exception {
        // Get the brand
        restBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBrand() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brand
        Brand updatedBrand = brandRepository.findById(brand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBrand are not directly saved in db
        em.detach(updatedBrand);
        updatedBrand.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).logoUrl(UPDATED_LOGO_URL).isActive(UPDATED_IS_ACTIVE);
        BrandDTO brandDTO = brandMapper.toDto(updatedBrand);

        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brandDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBrandToMatchAllProperties(updatedBrand);
    }

    @Test
    @Transactional
    void putNonExistingBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brandDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(brandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.description(UPDATED_DESCRIPTION).logoUrl(UPDATED_LOGO_URL);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrandUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBrand, brand), getPersistedBrand(brand));
    }

    @Test
    @Transactional
    void fullUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).logoUrl(UPDATED_LOGO_URL).isActive(UPDATED_IS_ACTIVE);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBrandUpdatableFieldsEquals(partialUpdatedBrand, getPersistedBrand(partialUpdatedBrand));
    }

    @Test
    @Transactional
    void patchNonExistingBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, brandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        brand.setId(longCount.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(brandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBrand() throws Exception {
        // Initialize the database
        insertedBrand = brandRepository.saveAndFlush(brand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the brand
        restBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, brand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return brandRepository.count();
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

    protected Brand getPersistedBrand(Brand brand) {
        return brandRepository.findById(brand.getId()).orElseThrow();
    }

    protected void assertPersistedBrandToMatchAllProperties(Brand expectedBrand) {
        assertBrandAllPropertiesEquals(expectedBrand, getPersistedBrand(expectedBrand));
    }

    protected void assertPersistedBrandToMatchUpdatableProperties(Brand expectedBrand) {
        assertBrandAllUpdatablePropertiesEquals(expectedBrand, getPersistedBrand(expectedBrand));
    }
}
