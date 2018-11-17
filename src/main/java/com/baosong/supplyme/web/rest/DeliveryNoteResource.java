package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.DeliveryNote;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.DeliveryNoteService;
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

/**
 * REST controller for managing DeliveryNote.
 */
@RestController
@RequestMapping("/api")
public class DeliveryNoteResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteResource.class);

    private static final String ENTITY_NAME = "deliveryNote";

    private final DeliveryNoteService deliveryNoteService;

    public DeliveryNoteResource(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    /**
     * POST  /delivery-notes : Create a new deliveryNote.
     *
     * @param deliveryNote the deliveryNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deliveryNote, or with status 400 (Bad Request) if the deliveryNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/delivery-notes")
    @Timed
    public ResponseEntity<DeliveryNote> createDeliveryNote(@Valid @RequestBody DeliveryNote deliveryNote) throws URISyntaxException {
        log.debug("REST request to save DeliveryNote : {}", deliveryNote);
        if (deliveryNote.getId() != null) {
            throw new BadRequestAlertException("A new deliveryNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            DeliveryNote result = deliveryNoteService.save(deliveryNote);
            return ResponseEntity.created(new URI("/api/delivery-notes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException("The delivery note can not be created", ENTITY_NAME, e.getMessage());
        }
    }

    /**
     * PUT  /delivery-notes : Updates an existing deliveryNote.
     *
     * @param deliveryNote the deliveryNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deliveryNote,
     * or with status 400 (Bad Request) if the deliveryNote is not valid,
     * or with status 500 (Internal Server Error) if the deliveryNote couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/delivery-notes")
    @Timed
    public ResponseEntity<DeliveryNote> updateDeliveryNote(@Valid @RequestBody DeliveryNote deliveryNote) throws URISyntaxException {
        log.debug("REST request to update DeliveryNote : {}", deliveryNote);
        if (deliveryNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        try {
            DeliveryNote result = deliveryNoteService.save(deliveryNote);
            return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deliveryNote.getId().toString()))
            .body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException("The delivery note can not be updated", ENTITY_NAME, e.getMessage());
        }
    }

    /**
     * GET  /delivery-notes : get all the deliveryNotes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deliveryNotes in body
     */
    @GetMapping("/delivery-notes")
    @Timed
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotes(Pageable pageable) {
        log.debug("REST request to get a page of DeliveryNotes");
        Page<DeliveryNote> page = deliveryNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/delivery-notes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /delivery-notes/:id : get the "id" deliveryNote.
     *
     * @param id the id of the deliveryNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deliveryNote, or with status 404 (Not Found)
     */
    @GetMapping("/delivery-notes/{id}")
    @Timed
    public ResponseEntity<DeliveryNote> getDeliveryNote(@PathVariable Long id) {
        log.debug("REST request to get DeliveryNote : {}", id);
        Optional<DeliveryNote> deliveryNote = deliveryNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryNote);
    }

    /**
     * DELETE  /delivery-notes/:id : delete the "id" deliveryNote.
     *
     * @param id the id of the deliveryNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/delivery-notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeliveryNote(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryNote : {}", id);
        deliveryNoteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/delivery-notes?query=:query : search for the deliveryNote corresponding
     * to the query.
     *
     * @param query the query of the deliveryNote search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/delivery-notes")
    @Timed
    public ResponseEntity<List<DeliveryNote>> searchDeliveryNotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DeliveryNotes for query {}", query);
        Page<DeliveryNote> page = deliveryNoteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/delivery-notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
