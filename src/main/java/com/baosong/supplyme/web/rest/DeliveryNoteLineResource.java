package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.DeliveryNoteLine;
import com.baosong.supplyme.service.DeliveryNoteLineService;
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
 * REST controller for managing DeliveryNoteLine.
 */
@RestController
@RequestMapping("/api")
public class DeliveryNoteLineResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteLineResource.class);

    private static final String ENTITY_NAME = "deliveryNoteLine";

    private final DeliveryNoteLineService deliveryNoteLineService;

    public DeliveryNoteLineResource(DeliveryNoteLineService deliveryNoteLineService) {
        this.deliveryNoteLineService = deliveryNoteLineService;
    }

    /**
     * POST  /delivery-note-lines : Create a new deliveryNoteLine.
     *
     * @param deliveryNoteLine the deliveryNoteLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deliveryNoteLine, or with status 400 (Bad Request) if the deliveryNoteLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/delivery-note-lines")
    @Timed
    public ResponseEntity<DeliveryNoteLine> createDeliveryNoteLine(@Valid @RequestBody DeliveryNoteLine deliveryNoteLine) throws URISyntaxException {
        log.debug("REST request to save DeliveryNoteLine : {}", deliveryNoteLine);
        if (deliveryNoteLine.getId() != null) {
            throw new BadRequestAlertException("A new deliveryNoteLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryNoteLine result = deliveryNoteLineService.save(deliveryNoteLine);
        return ResponseEntity.created(new URI("/api/delivery-note-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /delivery-note-lines : Updates an existing deliveryNoteLine.
     *
     * @param deliveryNoteLine the deliveryNoteLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deliveryNoteLine,
     * or with status 400 (Bad Request) if the deliveryNoteLine is not valid,
     * or with status 500 (Internal Server Error) if the deliveryNoteLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/delivery-note-lines")
    @Timed
    public ResponseEntity<DeliveryNoteLine> updateDeliveryNoteLine(@Valid @RequestBody DeliveryNoteLine deliveryNoteLine) throws URISyntaxException {
        log.debug("REST request to update DeliveryNoteLine : {}", deliveryNoteLine);
        if (deliveryNoteLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeliveryNoteLine result = deliveryNoteLineService.save(deliveryNoteLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deliveryNoteLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /delivery-note-lines : get all the deliveryNoteLines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deliveryNoteLines in body
     */
    @GetMapping("/delivery-note-lines")
    @Timed
    public ResponseEntity<List<DeliveryNoteLine>> getAllDeliveryNoteLines(Pageable pageable) {
        log.debug("REST request to get a page of DeliveryNoteLines");
        Page<DeliveryNoteLine> page = deliveryNoteLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/delivery-note-lines");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /delivery-note-lines/:id : get the "id" deliveryNoteLine.
     *
     * @param id the id of the deliveryNoteLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deliveryNoteLine, or with status 404 (Not Found)
     */
    @GetMapping("/delivery-note-lines/{id}")
    @Timed
    public ResponseEntity<DeliveryNoteLine> getDeliveryNoteLine(@PathVariable Long id) {
        log.debug("REST request to get DeliveryNoteLine : {}", id);
        Optional<DeliveryNoteLine> deliveryNoteLine = deliveryNoteLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryNoteLine);
    }

    /**
     * DELETE  /delivery-note-lines/:id : delete the "id" deliveryNoteLine.
     *
     * @param id the id of the deliveryNoteLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/delivery-note-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeliveryNoteLine(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryNoteLine : {}", id);
        deliveryNoteLineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/delivery-note-lines?query=:query : search for the deliveryNoteLine corresponding
     * to the query.
     *
     * @param query the query of the deliveryNoteLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/delivery-note-lines")
    @Timed
    public ResponseEntity<List<DeliveryNoteLine>> searchDeliveryNoteLines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DeliveryNoteLines for query {}", query);
        Page<DeliveryNoteLine> page = deliveryNoteLineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/delivery-note-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
