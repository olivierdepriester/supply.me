package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.DemandCategory;
import com.baosong.supplyme.service.DemandCategoryService;
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
 * REST controller for managing DemandCategory.
 */
@RestController
@RequestMapping("/api")
public class DemandCategoryResource {

    private final Logger log = LoggerFactory.getLogger(DemandCategoryResource.class);

    private static final String ENTITY_NAME = "demandCategory";

    private final DemandCategoryService demandCategoryService;

    public DemandCategoryResource(DemandCategoryService demandCategoryService) {
        this.demandCategoryService = demandCategoryService;
    }

    /**
     * POST  /demand-categories : Create a new demandCategory.
     *
     * @param demandCategory the demandCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demandCategory, or with status 400 (Bad Request) if the demandCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demand-categories")
    @Timed
    public ResponseEntity<DemandCategory> createDemandCategory(@Valid @RequestBody DemandCategory demandCategory) throws URISyntaxException {
        log.debug("REST request to save DemandCategory : {}", demandCategory);
        if (demandCategory.getId() != null) {
            throw new BadRequestAlertException("A new demandCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandCategory result = demandCategoryService.save(demandCategory);
        return ResponseEntity.created(new URI("/api/demand-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demand-categories : Updates an existing demandCategory.
     *
     * @param demandCategory the demandCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demandCategory,
     * or with status 400 (Bad Request) if the demandCategory is not valid,
     * or with status 500 (Internal Server Error) if the demandCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demand-categories")
    @Timed
    public ResponseEntity<DemandCategory> updateDemandCategory(@Valid @RequestBody DemandCategory demandCategory) throws URISyntaxException {
        log.debug("REST request to update DemandCategory : {}", demandCategory);
        if (demandCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DemandCategory result = demandCategoryService.save(demandCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demandCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demand-categories : get all the demandCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demandCategories in body
     */
    @GetMapping("/demand-categories")
    @Timed
    public ResponseEntity<List<DemandCategory>> getAllDemandCategories(Pageable pageable) {
        log.debug("REST request to get a page of DemandCategories");
        Page<DemandCategory> page = demandCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demand-categories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /demand-categories/:id : get the "id" demandCategory.
     *
     * @param id the id of the demandCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demandCategory, or with status 404 (Not Found)
     */
    @GetMapping("/demand-categories/{id}")
    @Timed
    public ResponseEntity<DemandCategory> getDemandCategory(@PathVariable Long id) {
        log.debug("REST request to get DemandCategory : {}", id);
        Optional<DemandCategory> demandCategory = demandCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demandCategory);
    }

    /**
     * DELETE  /demand-categories/:id : delete the "id" demandCategory.
     *
     * @param id the id of the demandCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demand-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteDemandCategory(@PathVariable Long id) {
        log.debug("REST request to delete DemandCategory : {}", id);
        demandCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/demand-categories?query=:query : search for the demandCategory corresponding
     * to the query.
     *
     * @param query the query of the demandCategory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/demand-categories")
    @Timed
    public ResponseEntity<List<DemandCategory>> searchDemandCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DemandCategories for query {}", query);
        Page<DemandCategory> page = demandCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/demand-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
