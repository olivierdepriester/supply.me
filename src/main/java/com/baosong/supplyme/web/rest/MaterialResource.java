package com.baosong.supplyme.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.MaterialService;
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
 * REST controller for managing Material.
 */
@RestController
@RequestMapping("/api")
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);

    private static final String ENTITY_NAME = "material";

    private final MaterialService materialService;

    public MaterialResource(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * POST  /materials : Create a new material.
     *
     * @param material the material to create
     * @return the ResponseEntity with status 201 (Created) and with body the new material, or with status 400 (Bad Request) if the material has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/materials")
    @Timed
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to save Material : {}", material);
        if (material.getId() != null) {
            throw new BadRequestAlertException("A new material cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            Material result = materialService.save(material);
            return ResponseEntity.created(new URI("/api/materials/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getMessage()) ;
        }
    }

    /**
     * PUT  /materials : Updates an existing material.
     *
     * @param material the material to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated material,
     * or with status 400 (Bad Request) if the material is not valid,
     * or with status 500 (Internal Server Error) if the material couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/materials")
    @Timed
    public ResponseEntity<Material> updateMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to update Material : {}", material);
        if (material.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        try {
            Material result = materialService.save(material);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, material.getId().toString()))
                .body(result);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getMessage()) ;
        }
    }

    /**
     * GET /materials : get all the materials.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materials in
     *         body
     */
    @GetMapping("/materials")
    @Timed
    public ResponseEntity<List<Material>> getAllMaterials(Pageable pageable) {
        log.debug("REST request to get a page of Materials");
        Page<Material> page = materialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/materials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /materials/:id : get the "id" material.
     *
     * @param id the id of the material to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the material,
     *         or with status 404 (Not Found)
     */
    @GetMapping("/materials/{id}")
    @Timed
    public ResponseEntity<Material> getMaterial(@PathVariable Long id) {
        log.debug("REST request to get Material : {}", id);
        Optional<Material> material = materialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(material);
    }

    /**
     * DELETE /materials/:id : delete the "id" material.
     *
     * @param id the id of the material to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/materials/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        log.debug("REST request to delete Material : {}", id);
        materialService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/materials?query=:query : search for the material
     * corresponding to the query.
     *
     * @param query    the query of the material search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/materials")
    @Timed
    public ResponseEntity<List<Material>> searchMaterials(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Materials for query {}", query);
        Page<Material> page = materialService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/materials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/_search/materials/rebuild")
    @Timed
    public ResponseEntity<Void> rebuildIndex() {
        materialService.rebuildIndex();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "0")).build();
    }
}
