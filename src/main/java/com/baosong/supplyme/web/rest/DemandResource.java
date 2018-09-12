package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Demand.
 */
@RestController
@RequestMapping("/api")
public class DemandResource {

    private final Logger log = LoggerFactory.getLogger(DemandResource.class);

    private static final String ENTITY_NAME = "demand";

    private final DemandService demandService;

    public DemandResource(DemandService demandService) {
        this.demandService = demandService;
    }

    /**
     * POST  /demands : Create a new demand.
     *
     * @param demand the demand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demand, or with status 400 (Bad Request) if the demand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demands")
    @Timed
    public ResponseEntity<Demand> createDemand(@Valid @RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to save Demand : {}", demand);
        if (demand.getId() != null) {
            throw new BadRequestAlertException("A new demand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Demand result = demandService.save(demand);
        return ResponseEntity.created(new URI("/api/demands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demands : Updates an existing demand.
     *
     * @param demand the demand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demand,
     * or with status 400 (Bad Request) if the demand is not valid,
     * or with status 500 (Internal Server Error) if the demand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demands")
    @Timed
    public ResponseEntity<Demand> updateDemand(@Valid @RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to update Demand : {}", demand);
        if (demand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Demand result = demandService.save(demand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demands : get all the demands.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of demands in body
     */
    @GetMapping("/demands")
    @Timed
    public List<Demand> getAllDemands() {
        log.debug("REST request to get all Demands");
        return demandService.findAll();
    }

    /**
     * GET  /demands/:id : get the "id" demand.
     *
     * @param id the id of the demand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demand, or with status 404 (Not Found)
     */
    @GetMapping("/demands/{id}")
    @Timed
    public ResponseEntity<Demand> getDemand(@PathVariable Long id) {
        log.debug("REST request to get Demand : {}", id);
        Optional<Demand> demand = demandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demand);
    }

    /**
     * DELETE  /demands/:id : delete the "id" demand.
     *
     * @param id the id of the demand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demands/{id}")
    @Timed
    public ResponseEntity<Void> deleteDemand(@PathVariable Long id) {
        log.debug("REST request to delete Demand : {}", id);
        demandService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/demands?query=:query : search for the demand corresponding
     * to the query.
     *
     * @param query the query of the demand search
     * @return the result of the search
     */
    @GetMapping("/_search/demands")
    @Timed
    public List<Demand> searchDemands(@RequestParam String query) {
        log.debug("REST request to search Demands for query {}", query);
        return demandService.search(query);
    }

}
