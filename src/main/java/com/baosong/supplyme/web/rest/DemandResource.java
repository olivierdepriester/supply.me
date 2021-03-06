package com.baosong.supplyme.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.errors.ParameterizedServiceException;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.AttachmentFileService;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.dto.AttachmentFileDTO;
import com.baosong.supplyme.service.util.DemandSearchCriteria;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.errors.BadRequestServiceException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.baosong.supplyme.web.rest.util.PaginationUtil;
import com.baosong.supplyme.web.rest.vm.DemandStatusChangeVM;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Demand.
 */
@RestController
@RequestMapping("/api")
public class DemandResource {

    private final Logger log = LoggerFactory.getLogger(DemandResource.class);

    private static final String ENTITY_NAME = "demand";

    private final DemandService demandService;

    private final AttachmentFileService attachmentFileService;

    public DemandResource(DemandService demandService, AttachmentFileService attachmentFileService) {
        this.demandService = demandService;
        this.attachmentFileService = attachmentFileService;
    }

    /**
     * POST /demands : Create a new demand.
     *
     * @param demand the demand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         demand, or with status 400 (Bad Request) if the demand has already an
     *         ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demands")
    @Timed
    public ResponseEntity<Demand> createDemand(@Valid @RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to save Demand : {}", demand);
        if (demand.getId() != null) {
            throw new BadRequestAlertException("A new demand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            Demand result = demandService.save(demand);
            return ResponseEntity.created(new URI("/api/demands/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "new");
        }
    }

    /**
     * PUT /demands : Updates an existing demand.
     *
     * @param demand the demand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         demand, or with status 400 (Bad Request) if the demand is not valid,
     *         or with status 500 (Internal Server Error) if the demand couldn't be
     *         updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demands")
    @Timed
    public ResponseEntity<Demand> updateDemand(@Valid @RequestBody Demand demand) throws URISyntaxException {
        log.debug("REST request to update Demand : {}", demand);
        if (demand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Demand result;
        try {
            result = demandService.save(demand);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "update")).body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "update");
        }
    }

    @PutMapping("/demands/changeStatus")
    @Timed
    public ResponseEntity<Demand> changeDemandStatus(@RequestBody DemandStatusChangeVM demandStatusChange)
            throws URISyntaxException {
        log.debug("REST request to update Demand : {}", demandStatusChange.getId());
        Demand result = null;
        try {
            result = demandService.changeStatus(demandStatusChange.getId(), demandStatusChange.getStatus(),
                    demandStatusChange.getComment());
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demandStatusChange.getId().toString()))
                    .body(result);
        } catch (ParameterizedServiceException e) {
            throw new BadRequestServiceException(e, "demand");
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "changeStatus");
        }
    }

    /**
     * GET /demands : get all the demands.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of demands in
     *         body
     */
    @GetMapping("/demands")
    @Timed
    public List<Demand> getAllDemands() {
        log.debug("REST request to get all Demands");
        return demandService.findAll();
    }

    /**
     * GET /demands/:id : get the "id" demand.
     *
     * @param id the id of the demand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demand, or
     *         with status 404 (Not Found)
     */
    @GetMapping("/demands/{id}")
    @Timed
    public ResponseEntity<Demand> getDemand(@PathVariable Long id, @RequestParam Optional<Boolean> loadChanges) {
        log.debug("REST request to get Demand : {}", id);
        Optional<Demand> demand = null;
        if (loadChanges.orElse(Boolean.FALSE)) {
            demand = demandService.findOneWithStatusChanges(id);
        } else {
            demand = demandService.findOne(id);
        }
        return ResponseUtil.wrapOrNotFound(demand);
    }

    /**
     * DELETE /demands/:id : delete the "id" demand.
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
     * SEARCH /_search/demands?query=:query : search for the demand corresponding to
     * the query.
     *
     * @param query the query of the demand search
     * @return the result of the search
     */
    @GetMapping("/_search/demands")
    @Timed
    public List<Demand> searchDemands(@RequestParam Optional<String> query, @RequestParam Optional<Long> materialId,
            @RequestParam Optional<Long> projectId, @RequestParam Optional<Long> creationUserId,
            @RequestParam Optional<Long> departmentId,
            @RequestParam Optional<Instant> creationDateFrom,
            @RequestParam Optional<Instant> creationDateTo,
            @RequestParam Optional<List<DemandStatus>> status, Pageable pageable) {
        log.debug(String.format("REST request to search Demands for query : [%s, %s, %s, %s, %s, %s, %s, %s]", query, materialId,
                departmentId, projectId, creationUserId, status, creationDateFrom, creationDateTo));
        return demandService.search(new DemandSearchCriteria().query(query.orElse(null))
                .materialId(materialId.orElse(null)).projectId(projectId.orElse(null)).departmentId(departmentId.orElse(null))
                .creationUserId(creationUserId.orElse(null))
                .creationDateFrom(creationDateFrom.orElse(null))
                .creationDateTo(creationDateTo.orElse(null))
                .demandStatus(status.orElse(null)), pageable);
    }

    @PostMapping("/_search/demands/rebuild")
    @Timed
    public ResponseEntity<Void> rebuildIndex() {
        demandService.rebuildIndex();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "0")).build();
    }

    /**
     * SEARCH /_search/purchase-order-lines?query=:query : search for the
     * purchaseOrderLine corresponding to the query.
     *
     * @param query    the query of the purchaseOrderLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/demands/purchasable")
    @Timed
    public ResponseEntity<List<Demand>> searchPurchaseOrderLines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PurchaseOrderLines for query {}", query);
        Page<Demand> page = demandService.searchDemandsToPurchase(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/demands/purchasable");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/demands/draftAttachment")
    @Timed
    public ResponseEntity<List<AttachmentFileDTO>> uploadDraftAttachment(@RequestParam List<MultipartFile> files) {
        log.debug("REST request to upload %d files", files.size());
        List<AttachmentFileDTO> attachmentFiles = this.attachmentFileService.saveDraftAttachmentFiles(files);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Attachment", "0"))
                .body(attachmentFiles);
    }

    @DeleteMapping("/demands/draftAttachment/{token}")
    @Timed
    public ResponseEntity<Void> removeDraftAttachment(@PathVariable String token) {
        log.debug("REST request to remove draft attachment %s", token);
        BodyBuilder responseBuilder = null;
        try {
            if (this.attachmentFileService.removeDraftAttachmentFile(token)) {
                responseBuilder = ResponseEntity.ok();
            } else {
                responseBuilder = ResponseEntity.badRequest();
            }
            return responseBuilder.headers(HeaderUtil.createEntityDeletionAlert("Attachment", token)).build();
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), "Attachment", "delete");
        }
    }

    @PutMapping("/demands/attachments/{id}")
    public ResponseEntity<List<AttachmentFileDTO>> saveAttachments(@PathVariable Long id,
            @RequestBody List<AttachmentFileDTO> files) {
        try {
            List<AttachmentFileDTO> attachmentFiles = this.attachmentFileService.saveAttachmentFiles(id, files);
            return ResponseEntity.ok().body(attachmentFiles);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), "Attachment", "update");
        }
    }

    @GetMapping("/demands/attachments/{id}")
    public List<AttachmentFileDTO> getAttachments(@PathVariable Long id) {
        return this.attachmentFileService.getAttachmentFiles(id);
    }

    @RequestMapping(value = "/demands/attachment/{id}", method = RequestMethod.GET)
    public void downloadAttachmentFile(HttpServletResponse response, @PathVariable Long id) {
        try {
            AttachmentFileDTO dto = this.attachmentFileService.getAttachmentFile(id);
            // Set the content type and attachment header.
            response.addHeader("Content-disposition", "attachment;filename=" + dto.getName());
            response.setContentType(dto.getType());
            // Write the content into the response
            response.getOutputStream().write(dto.getContent());
            // Close for the stream to be saved
            response.getOutputStream().close();
            response.flushBuffer();
        } catch (ServiceException | IOException e) {
            throw new BadRequestAlertException(e.getMessage(), "Attachment", "getFile");
        }
    }

    @GetMapping(value = "/demands/draftAttachment/{token}")
    public void downloadDraftAttachmentFile(HttpServletResponse response, @PathVariable String token) {
        try {
            // Set the content type and attachment header.
            // Write the content into the response
            response.getOutputStream().write(this.attachmentFileService.getDraftAttachmentFile(token));
            // Close for the stream to be saved
            response.getOutputStream().close();
            response.flushBuffer();
        } catch (ServiceException | IOException e) {
            throw new BadRequestAlertException(e.getMessage(), "Attachment", "getFile");
        }
    }
}
