package com.evaradrip.commerce.web.rest;

import com.evaradrip.commerce.repository.WishlistRepository;
import com.evaradrip.commerce.service.WishlistQueryService;
import com.evaradrip.commerce.service.WishlistService;
import com.evaradrip.commerce.service.criteria.WishlistCriteria;
import com.evaradrip.commerce.service.dto.WishlistDTO;
import com.evaradrip.commerce.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.evaradrip.commerce.domain.Wishlist}.
 */
@RestController
@RequestMapping("/api/wishlists")
public class WishlistResource {

    private static final Logger LOG = LoggerFactory.getLogger(WishlistResource.class);

    private static final String ENTITY_NAME = "wishlist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WishlistService wishlistService;

    private final WishlistRepository wishlistRepository;

    private final WishlistQueryService wishlistQueryService;

    public WishlistResource(
        WishlistService wishlistService,
        WishlistRepository wishlistRepository,
        WishlistQueryService wishlistQueryService
    ) {
        this.wishlistService = wishlistService;
        this.wishlistRepository = wishlistRepository;
        this.wishlistQueryService = wishlistQueryService;
    }

    /**
     * {@code POST  /wishlists} : Create a new wishlist.
     *
     * @param wishlistDTO the wishlistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wishlistDTO, or with status {@code 400 (Bad Request)} if the wishlist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WishlistDTO> createWishlist(@Valid @RequestBody WishlistDTO wishlistDTO) throws URISyntaxException {
        LOG.debug("REST request to save Wishlist : {}", wishlistDTO);
        if (wishlistDTO.getId() != null) {
            throw new BadRequestAlertException("A new wishlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wishlistDTO = wishlistService.save(wishlistDTO);
        return ResponseEntity.created(new URI("/api/wishlists/" + wishlistDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, wishlistDTO.getId().toString()))
            .body(wishlistDTO);
    }

    /**
     * {@code PUT  /wishlists/:id} : Updates an existing wishlist.
     *
     * @param id the id of the wishlistDTO to save.
     * @param wishlistDTO the wishlistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishlistDTO,
     * or with status {@code 400 (Bad Request)} if the wishlistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wishlistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WishlistDTO> updateWishlist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WishlistDTO wishlistDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Wishlist : {}, {}", id, wishlistDTO);
        if (wishlistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishlistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        wishlistDTO = wishlistService.update(wishlistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishlistDTO.getId().toString()))
            .body(wishlistDTO);
    }

    /**
     * {@code PATCH  /wishlists/:id} : Partial updates given fields of an existing wishlist, field will ignore if it is null
     *
     * @param id the id of the wishlistDTO to save.
     * @param wishlistDTO the wishlistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishlistDTO,
     * or with status {@code 400 (Bad Request)} if the wishlistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wishlistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wishlistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WishlistDTO> partialUpdateWishlist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WishlistDTO wishlistDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Wishlist partially : {}, {}", id, wishlistDTO);
        if (wishlistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishlistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WishlistDTO> result = wishlistService.partialUpdate(wishlistDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishlistDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wishlists} : get all the wishlists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wishlists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WishlistDTO>> getAllWishlists(
        WishlistCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Wishlists by criteria: {}", criteria);

        Page<WishlistDTO> page = wishlistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wishlists/count} : count all the wishlists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWishlists(WishlistCriteria criteria) {
        LOG.debug("REST request to count Wishlists by criteria: {}", criteria);
        return ResponseEntity.ok().body(wishlistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wishlists/:id} : get the "id" wishlist.
     *
     * @param id the id of the wishlistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wishlistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WishlistDTO> getWishlist(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Wishlist : {}", id);
        Optional<WishlistDTO> wishlistDTO = wishlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wishlistDTO);
    }

    /**
     * {@code DELETE  /wishlists/:id} : delete the "id" wishlist.
     *
     * @param id the id of the wishlistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Wishlist : {}", id);
        wishlistService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
