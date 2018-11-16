package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.MaterialCategory;
import com.baosong.supplyme.service.MaterialCategoryService;
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
 * REST controller for managing MaterialCategory.
 */
@RestController
@RequestMapping("/api")
public class MaterialCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MaterialCategoryResource.class);

    private static final String ENTITY_NAME = "materialCategory";

    private final MaterialCategoryService materialCategoryService;

    public MaterialCategoryResource(MaterialCategoryService materialCategoryService) {
        this.materialCategoryService = materialCategoryService;
    }

    /**
     * POST  /material-categories : Create a new materialCategory.
     *
     * @param materialCategory the materialCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialCategory, or with status 400 (Bad Request) if the materialCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-categories")
    @Timed
    public ResponseEntity<MaterialCategory> createMaterialCategory(@Valid @RequestBody MaterialCategory materialCategory) throws URISyntaxException {
        log.debug("REST request to save MaterialCategory : {}", materialCategory);
        if (materialCategory.getId() != null) {
            throw new BadRequestAlertException("A new materialCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialCategory result = materialCategoryService.save(materialCategory);
        return ResponseEntity.created(new URI("/api/material-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-categories : Updates an existing materialCategory.
     *
     * @param materialCategory the materialCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialCategory,
     * or with status 400 (Bad Request) if the materialCategory is not valid,
     * or with status 500 (Internal Server Error) if the materialCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-categories")
    @Timed
    public ResponseEntity<MaterialCategory> updateMaterialCategory(@Valid @RequestBody MaterialCategory materialCategory) throws URISyntaxException {
        log.debug("REST request to update MaterialCategory : {}", materialCategory);
        if (materialCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialCategory result = materialCategoryService.save(materialCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-categories : get all the materialCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialCategories in body
     */
    @GetMapping("/material-categories")
    @Timed
    public ResponseEntity<List<MaterialCategory>> getAllMaterialCategories(Pageable pageable) {
        log.debug("REST request to get a page of MaterialCategories");
        Page<MaterialCategory> page = materialCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-categories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /material-categories/:id : get the "id" materialCategory.
     *
     * @param id the id of the materialCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialCategory, or with status 404 (Not Found)
     */
    @GetMapping("/material-categories/{id}")
    @Timed
    public ResponseEntity<MaterialCategory> getMaterialCategory(@PathVariable Long id) {
        log.debug("REST request to get MaterialCategory : {}", id);
        Optional<MaterialCategory> materialCategory = materialCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialCategory);
    }

    /**
     * DELETE  /material-categories/:id : delete the "id" materialCategory.
     *
     * @param id the id of the materialCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialCategory(@PathVariable Long id) {
        log.debug("REST request to delete MaterialCategory : {}", id);
        materialCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/material-categories?query=:query : search for the materialCategory corresponding
     * to the query.
     *
     * @param query the query of the materialCategory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/material-categories")
    @Timed
    public ResponseEntity<List<MaterialCategory>> searchMaterialCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MaterialCategories for query {}", query);
        Page<MaterialCategory> page = materialCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/material-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
