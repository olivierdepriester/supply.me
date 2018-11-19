package com.baosong.supplyme.web.rest;

import java.util.List;

import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.ElasticsearchService;
import com.baosong.supplyme.service.dto.ElasticsearchIndexResult;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/management")
public class ElasticsearchResource {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchResource.class);

    private static final String ENTITY_NAME = "elasticsearch";

    private final ElasticsearchService elasticsearchService;

    public ElasticsearchResource(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    /**
     * GET  /invoices : get all the invoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
     */
    @GetMapping("/indices")
    @Timed
    public ResponseEntity<List<ElasticsearchIndexResult>> getAllIndices(Pageable pageable) {
        log.debug("REST request to get a list of indices");
        try {
            return ResponseEntity.ok().body(elasticsearchService.getIndexableClasses());
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getKeyCode());
		}
    }

    /**
     * POST  /indices/:name/rebuild/ : rebuild all the name index
     *
     * @param name name of the index
     * @return the ResponseEntity with status 200 (OK).
     */
    @PostMapping("/indices/{name}/rebuild/")
    @Timed
    public ResponseEntity<ElasticsearchIndexResult> rebuildIndex(@PathVariable String name) {
        log.debug("REST request to rebuild index : {}", name);
        try {
            ElasticsearchIndexResult result = elasticsearchService.reindexOneClass(name);
            return ResponseEntity.ok().body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getKeyCode());
        }
    }

    /**
     * GET  /invoices/:id : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
     */
    @PutMapping("/indices/all/rebuild/")
    @Timed
    public ResponseEntity<List<ElasticsearchIndexResult>> rebuildAll() {
        log.debug("REST request to rebuild all indices");
        try {
            return ResponseEntity.ok().body(elasticsearchService.reindexAll());
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getKeyCode());
        }
    }
}
