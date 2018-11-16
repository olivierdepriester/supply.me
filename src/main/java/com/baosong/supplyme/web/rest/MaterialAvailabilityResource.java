package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.MaterialAvailability;
import com.baosong.supplyme.service.MaterialAvailabilityService;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.baosong.supplyme.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MaterialAvailability.
 */
@RestController
@RequestMapping("/api")
public class MaterialAvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(MaterialAvailabilityResource.class);

    private static final String ENTITY_NAME = "materialAvailability";

    private final MaterialAvailabilityService materialAvailabilityService;

    public MaterialAvailabilityResource(MaterialAvailabilityService materialAvailabilityService) {
        this.materialAvailabilityService = materialAvailabilityService;
    }

    /**
     * POST  /material-availabilities : Create a new materialAvailability.
     *
     * @param materialAvailability the materialAvailability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialAvailability, or with status 400 (Bad Request) if the materialAvailability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-availabilities")
    @Timed
    public ResponseEntity<MaterialAvailability> createMaterialAvailability(@Valid @RequestBody MaterialAvailability materialAvailability) throws URISyntaxException {
        log.debug("REST request to save MaterialAvailability : {}", materialAvailability);
        if (materialAvailability.getId() != null) {
            throw new BadRequestAlertException("A new materialAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialAvailability result = materialAvailabilityService.save(materialAvailability);
        return ResponseEntity.created(new URI("/api/material-availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-availabilities : Updates an existing materialAvailability.
     *
     * @param materialAvailability the materialAvailability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialAvailability,
     * or with status 400 (Bad Request) if the materialAvailability is not valid,
     * or with status 500 (Internal Server Error) if the materialAvailability couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-availabilities")
    @Timed
    public ResponseEntity<MaterialAvailability> updateMaterialAvailability(@Valid @RequestBody MaterialAvailability materialAvailability) throws URISyntaxException {
        log.debug("REST request to update MaterialAvailability : {}", materialAvailability);
        if (materialAvailability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialAvailability result = materialAvailabilityService.save(materialAvailability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialAvailability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-availabilities : get all the materialAvailabilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialAvailabilities in body
     */
    @GetMapping("/material-availabilities")
    @Timed
    public ResponseEntity<List<MaterialAvailability>> getAllMaterialAvailabilities(Pageable pageable) {
        log.debug("REST request to get a page of MaterialAvailabilities");
        Page<MaterialAvailability> page = materialAvailabilityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-availabilities");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /material-availabilities/:id : get the "id" materialAvailability.
     *
     * @param id the id of the materialAvailability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialAvailability, or with status 404 (Not Found)
     */
    @GetMapping("/material-availabilities/{id}")
    @Timed
    public ResponseEntity<MaterialAvailability> getMaterialAvailability(@PathVariable Long id) {
        log.debug("REST request to get MaterialAvailability : {}", id);
        Optional<MaterialAvailability> materialAvailability = materialAvailabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialAvailability);
    }

    /**
     * DELETE  /material-availabilities/:id : delete the "id" materialAvailability.
     *
     * @param id the id of the materialAvailability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-availabilities/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialAvailability(@PathVariable Long id) {
        log.debug("REST request to delete MaterialAvailability : {}", id);
        materialAvailabilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/material-availabilities?query=:query : search for the materialAvailability corresponding
     * to the query.
     *
     * @param query the query of the materialAvailability search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/material-availabilities")
    @Timed
    public ResponseEntity<List<MaterialAvailability>> searchMaterialAvailabilities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MaterialAvailabilities for query {}", query);
        Page<MaterialAvailability> page = materialAvailabilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/material-availabilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
