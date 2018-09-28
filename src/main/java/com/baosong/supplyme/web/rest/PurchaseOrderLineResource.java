package com.baosong.supplyme.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.errors.InternalServerErrorException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.baosong.supplyme.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing PurchaseOrderLine.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderLineResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderLineResource.class);

    private static final String ENTITY_NAME = "purchaseOrderLine";

    private final PurchaseOrderLineService purchaseOrderLineService;

    public PurchaseOrderLineResource(PurchaseOrderLineService purchaseOrderLineService) {
        this.purchaseOrderLineService = purchaseOrderLineService;
    }

    /**
     * POST  /purchase-order-lines : Create a new purchaseOrderLine.
     *
     * @param purchaseOrderLine the purchaseOrderLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseOrderLine, or with status 400 (Bad Request) if the purchaseOrderLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-order-lines")
    @Timed
    public ResponseEntity<PurchaseOrderLine> createPurchaseOrderLine(@Valid @RequestBody PurchaseOrderLine purchaseOrderLine) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderLine : {}", purchaseOrderLine);
        if (purchaseOrderLine.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderLine result = purchaseOrderLineService.save(purchaseOrderLine);
        return ResponseEntity.created(new URI("/api/purchase-order-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-order-lines : Updates an existing purchaseOrderLine.
     *
     * @param purchaseOrderLine the purchaseOrderLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseOrderLine,
     * or with status 400 (Bad Request) if the purchaseOrderLine is not valid,
     * or with status 500 (Internal Server Error) if the purchaseOrderLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-order-lines")
    @Timed
    public ResponseEntity<PurchaseOrderLine> updatePurchaseOrderLine(@Valid @RequestBody PurchaseOrderLine purchaseOrderLine) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderLine : {}", purchaseOrderLine);
        if (purchaseOrderLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseOrderLine result = purchaseOrderLineService.save(purchaseOrderLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseOrderLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-order-lines : get all the purchaseOrderLines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseOrderLines in body
     */
    @GetMapping("/purchase-order-lines")
    @Timed
    public ResponseEntity<List<PurchaseOrderLine>> getAllPurchaseOrderLines(Pageable pageable) {
        log.debug("REST request to get a page of PurchaseOrderLines");
        Page<PurchaseOrderLine> page = purchaseOrderLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchase-order-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /purchase-order-lines/:id : get the "id" purchaseOrderLine.
     *
     * @param id the id of the purchaseOrderLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseOrderLine, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-order-lines/{id}")
    @Timed
    public ResponseEntity<PurchaseOrderLine> getPurchaseOrderLine(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderLine : {}", id);
        Optional<PurchaseOrderLine> purchaseOrderLine = purchaseOrderLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderLine);
    }

    /**
     * DELETE  /purchase-order-lines/:id : delete the "id" purchaseOrderLine.
     *
     * @param id the id of the purchaseOrderLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-order-lines/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchaseOrderLine(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderLine : {}", id);
        try {
            purchaseOrderLineService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
		}
    }

    /**
     * SEARCH  /purchase-order-lines/demand/{id} : search for the purchaseOrderLine corresponding
     * to the query.
     *
     * @param query the query of the purchaseOrderLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/purchase-order-lines/demand/{id}")
    @Timed
    public List<PurchaseOrderLine> getPurchaseOrderLinesByDemandId(@PathVariable Long id) {
        log.debug("REST request to search for a page of PurchaseOrderLines for query demandId {}", id);
        return purchaseOrderLineService.getByDemandId(id);
    }

    /**
     * SEARCH  /_search/purchase-order-lines?query=:query : search for the purchaseOrderLine corresponding
     * to the query.
     *
     * @param query the query of the purchaseOrderLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-order-lines")
    @Timed
    public ResponseEntity<List<PurchaseOrderLine>> searchPurchaseOrderLines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PurchaseOrderLines for query {}", query);
        Page<PurchaseOrderLine> page = purchaseOrderLineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purchase-order-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
