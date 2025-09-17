package com.evaradrip.commerce.web.rest;

import static com.evaradrip.commerce.domain.WishlistAsserts.*;
import static com.evaradrip.commerce.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evaradrip.commerce.IntegrationTest;
import com.evaradrip.commerce.domain.Wishlist;
import com.evaradrip.commerce.repository.WishlistRepository;
import com.evaradrip.commerce.service.dto.WishlistDTO;
import com.evaradrip.commerce.service.mapper.WishlistMapper;
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
 * Integration tests for the {@link WishlistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishlistResourceIT {

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wishlists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishlistMockMvc;

    private Wishlist wishlist;

    private Wishlist insertedWishlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wishlist createEntity() {
        return new Wishlist().priority(DEFAULT_PRIORITY).notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wishlist createUpdatedEntity() {
        return new Wishlist().priority(UPDATED_PRIORITY).notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        wishlist = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWishlist != null) {
            wishlistRepository.delete(insertedWishlist);
            insertedWishlist = null;
        }
    }

    @Test
    @Transactional
    void createWishlist() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);
        var returnedWishlistDTO = om.readValue(
            restWishlistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WishlistDTO.class
        );

        // Validate the Wishlist in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWishlist = wishlistMapper.toEntity(returnedWishlistDTO);
        assertWishlistUpdatableFieldsEquals(returnedWishlist, getPersistedWishlist(returnedWishlist));

        insertedWishlist = returnedWishlist;
    }

    @Test
    @Transactional
    void createWishlistWithExistingId() throws Exception {
        // Create the Wishlist with an existing ID
        wishlist.setId(1L);
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWishlists() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get the wishlist
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL_ID, wishlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishlist.getId().intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getWishlistsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        Long id = wishlist.getId();

        defaultWishlistFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWishlistFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWishlistFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority equals to
        defaultWishlistFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority in
        defaultWishlistFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority is not null
        defaultWishlistFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority is greater than or equal to
        defaultWishlistFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority is less than or equal to
        defaultWishlistFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority is less than
        defaultWishlistFiltering("priority.lessThan=" + (DEFAULT_PRIORITY + 1), "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWishlistsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where priority is greater than
        defaultWishlistFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWishlistsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where notes equals to
        defaultWishlistFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllWishlistsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where notes in
        defaultWishlistFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllWishlistsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where notes is not null
        defaultWishlistFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllWishlistsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where notes contains
        defaultWishlistFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllWishlistsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList where notes does not contain
        defaultWishlistFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    private void defaultWishlistFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWishlistShouldBeFound(shouldBeFound);
        defaultWishlistShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWishlistShouldBeFound(String filter) throws Exception {
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWishlistShouldNotBeFound(String filter) throws Exception {
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWishlist() throws Exception {
        // Get the wishlist
        restWishlistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist
        Wishlist updatedWishlist = wishlistRepository.findById(wishlist.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWishlist are not directly saved in db
        em.detach(updatedWishlist);
        updatedWishlist.priority(UPDATED_PRIORITY).notes(UPDATED_NOTES);
        WishlistDTO wishlistDTO = wishlistMapper.toDto(updatedWishlist);

        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWishlistToMatchAllProperties(updatedWishlist);
    }

    @Test
    @Transactional
    void putNonExistingWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishlistWithPatch() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist using partial update
        Wishlist partialUpdatedWishlist = new Wishlist();
        partialUpdatedWishlist.setId(wishlist.getId());

        partialUpdatedWishlist.notes(UPDATED_NOTES);

        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishlist))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishlistUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWishlist, wishlist), getPersistedWishlist(wishlist));
    }

    @Test
    @Transactional
    void fullUpdateWishlistWithPatch() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist using partial update
        Wishlist partialUpdatedWishlist = new Wishlist();
        partialUpdatedWishlist.setId(wishlist.getId());

        partialUpdatedWishlist.priority(UPDATED_PRIORITY).notes(UPDATED_NOTES);

        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishlist))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishlistUpdatableFieldsEquals(partialUpdatedWishlist, getPersistedWishlist(partialUpdatedWishlist));
    }

    @Test
    @Transactional
    void patchNonExistingWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wishlist
        restWishlistMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishlist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return wishlistRepository.count();
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

    protected Wishlist getPersistedWishlist(Wishlist wishlist) {
        return wishlistRepository.findById(wishlist.getId()).orElseThrow();
    }

    protected void assertPersistedWishlistToMatchAllProperties(Wishlist expectedWishlist) {
        assertWishlistAllPropertiesEquals(expectedWishlist, getPersistedWishlist(expectedWishlist));
    }

    protected void assertPersistedWishlistToMatchUpdatableProperties(Wishlist expectedWishlist) {
        assertWishlistAllUpdatablePropertiesEquals(expectedWishlist, getPersistedWishlist(expectedWishlist));
    }
}
