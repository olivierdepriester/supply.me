package com.baosong.supplyme.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing MutableProperties.
 */
@RestController
@RequestMapping("/api")
public class MutablePropertiesResource {

    private final Logger log = LoggerFactory.getLogger(MutablePropertiesResource.class);

    private static final String ENTITY_NAME = "mutableProperties";

    private final MutablePropertiesService mutablePropertiesService;

    public MutablePropertiesResource(MutablePropertiesService mutablePropertiesService) {
        this.mutablePropertiesService = mutablePropertiesService;
    }

    /**
     * POST  /mutable-properties : Create a new mutableProperties.
     *
     * @param mutableProperties the mutableProperties to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mutableProperties, or with status 400 (Bad Request) if the mutableProperties has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mutable-properties")
    @Timed
    public ResponseEntity<MutableProperties> createMutableProperties(@Valid @RequestBody MutableProperties mutableProperties) throws URISyntaxException {
        log.debug("REST request to save MutableProperties : {}", mutableProperties);
        if (mutableProperties.getId() != null) {
            throw new BadRequestAlertException("A new mutableProperties cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MutableProperties result = mutablePropertiesService.save(mutableProperties);
        return ResponseEntity.created(new URI("/api/mutable-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mutable-properties : Updates an existing mutableProperties.
     *
     * @param mutableProperties the mutableProperties to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mutableProperties,
     * or with status 400 (Bad Request) if the mutableProperties is not valid,
     * or with status 500 (Internal Server Error) if the mutableProperties couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mutable-properties")
    @Timed
    public ResponseEntity<MutableProperties> updateMutableProperties(@Valid @RequestBody MutableProperties mutableProperties) throws URISyntaxException {
        log.debug("REST request to update MutableProperties : {}", mutableProperties);
        if (mutableProperties.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MutableProperties result = mutablePropertiesService.save(mutableProperties);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mutableProperties.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mutable-properties : get all the mutableProperties.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mutableProperties in body
     */
    @GetMapping("/mutable-properties")
    @Timed
    public List<MutableProperties> getAllMutableProperties() {
        log.debug("REST request to get all MutableProperties");
        return mutablePropertiesService.findAll();
    }

    /**
     * GET  /mutable-properties/:id : get the "id" mutableProperties.
     *
     * @param id the id of the mutableProperties to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mutableProperties, or with status 404 (Not Found)
     */
    @GetMapping("/mutable-properties/{id}")
    @Timed
    public ResponseEntity<MutableProperties> getMutableProperties(@PathVariable Long id) {
        log.debug("REST request to get MutableProperties : {}", id);
        Optional<MutableProperties> mutableProperties = mutablePropertiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mutableProperties);
    }

    /**
     * DELETE  /mutable-properties/:id : delete the "id" mutableProperties.
     *
     * @param id the id of the mutableProperties to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mutable-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteMutableProperties(@PathVariable Long id) {
        log.debug("REST request to delete MutableProperties : {}", id);
        mutablePropertiesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mutable-properties?query=:query : search for the mutableProperties corresponding
     * to the query.
     *
     * @param query the query of the mutableProperties search
     * @return the result of the search
     */
    @GetMapping("/_search/mutable-properties")
    @Timed
    public List<MutableProperties> searchMutableProperties(@RequestParam String query) {
        log.debug("REST request to search MutableProperties for query {}", query);
        return mutablePropertiesService.search(query);
    }

}
