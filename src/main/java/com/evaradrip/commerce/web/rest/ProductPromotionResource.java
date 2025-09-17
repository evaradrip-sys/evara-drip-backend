package com.evaradrip.commerce.web.rest;

import com.evaradrip.commerce.repository.ProductPromotionRepository;
import com.evaradrip.commerce.service.ProductPromotionQueryService;
import com.evaradrip.commerce.service.ProductPromotionService;
import com.evaradrip.commerce.service.criteria.ProductPromotionCriteria;
import com.evaradrip.commerce.service.dto.ProductPromotionDTO;
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
 * REST controller for managing {@link com.evaradrip.commerce.domain.ProductPromotion}.
 */
@RestController
@RequestMapping("/api/product-promotions")
public class ProductPromotionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPromotionResource.class);

    private static final String ENTITY_NAME = "productPromotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductPromotionService productPromotionService;

    private final ProductPromotionRepository productPromotionRepository;

    private final ProductPromotionQueryService productPromotionQueryService;

    public ProductPromotionResource(
        ProductPromotionService productPromotionService,
        ProductPromotionRepository productPromotionRepository,
        ProductPromotionQueryService productPromotionQueryService
    ) {
        this.productPromotionService = productPromotionService;
        this.productPromotionRepository = productPromotionRepository;
        this.productPromotionQueryService = productPromotionQueryService;
    }

    /**
     * {@code POST  /product-promotions} : Create a new productPromotion.
     *
     * @param productPromotionDTO the productPromotionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPromotionDTO, or with status {@code 400 (Bad Request)} if the productPromotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductPromotionDTO> createProductPromotion(@Valid @RequestBody ProductPromotionDTO productPromotionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductPromotion : {}", productPromotionDTO);
        if (productPromotionDTO.getId() != null) {
            throw new BadRequestAlertException("A new productPromotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productPromotionDTO = productPromotionService.save(productPromotionDTO);
        return ResponseEntity.created(new URI("/api/product-promotions/" + productPromotionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productPromotionDTO.getId().toString()))
            .body(productPromotionDTO);
    }

    /**
     * {@code PUT  /product-promotions/:id} : Updates an existing productPromotion.
     *
     * @param id the id of the productPromotionDTO to save.
     * @param productPromotionDTO the productPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the productPromotionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductPromotionDTO> updateProductPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductPromotionDTO productPromotionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductPromotion : {}, {}", id, productPromotionDTO);
        if (productPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPromotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productPromotionDTO = productPromotionService.update(productPromotionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPromotionDTO.getId().toString()))
            .body(productPromotionDTO);
    }

    /**
     * {@code PATCH  /product-promotions/:id} : Partial updates given fields of an existing productPromotion, field will ignore if it is null
     *
     * @param id the id of the productPromotionDTO to save.
     * @param productPromotionDTO the productPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the productPromotionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productPromotionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPromotionDTO> partialUpdateProductPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductPromotionDTO productPromotionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductPromotion partially : {}, {}", id, productPromotionDTO);
        if (productPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPromotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPromotionDTO> result = productPromotionService.partialUpdate(productPromotionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPromotionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-promotions} : get all the productPromotions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productPromotions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductPromotionDTO>> getAllProductPromotions(
        ProductPromotionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductPromotions by criteria: {}", criteria);

        Page<ProductPromotionDTO> page = productPromotionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-promotions/count} : count all the productPromotions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductPromotions(ProductPromotionCriteria criteria) {
        LOG.debug("REST request to count ProductPromotions by criteria: {}", criteria);
        return ResponseEntity.ok().body(productPromotionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-promotions/:id} : get the "id" productPromotion.
     *
     * @param id the id of the productPromotionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPromotionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductPromotionDTO> getProductPromotion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductPromotion : {}", id);
        Optional<ProductPromotionDTO> productPromotionDTO = productPromotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPromotionDTO);
    }

    /**
     * {@code DELETE  /product-promotions/:id} : delete the "id" productPromotion.
     *
     * @param id the id of the productPromotionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPromotion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductPromotion : {}", id);
        productPromotionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
