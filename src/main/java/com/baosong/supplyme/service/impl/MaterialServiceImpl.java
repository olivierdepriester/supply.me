package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.MaterialService;
import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.repository.MaterialRepository;
import com.baosong.supplyme.repository.search.MaterialSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Material.
 */
@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final Logger log = LoggerFactory.getLogger(MaterialServiceImpl.class);

    private final MaterialRepository materialRepository;

    private final MaterialSearchRepository materialSearchRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialSearchRepository materialSearchRepository) {
        this.materialRepository = materialRepository;
        this.materialSearchRepository = materialSearchRepository;
    }

    /**
     * Save a material.
     *
     * @param material the entity to save
     * @return the persisted entity
     */
    @Override
    public Material save(Material material) {
        log.debug("Request to save Material : {}", material);
        Material result = materialRepository.save(material);
        materialSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Material> findAll(Pageable pageable) {
        log.debug("Request to get all Materials");
        return materialRepository.findAll(pageable);
    }


    /**
     * Get one material by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Material> findOne(Long id) {
        log.debug("Request to get Material : {}", id);
        return materialRepository.findById(id);
    }

    /**
     * Delete the material by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Material : {}", id);
        materialRepository.deleteById(id);
        materialSearchRepository.deleteById(id);
    }

    /**
     * Search for the material corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Material> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Materials for query {}", query);
        return materialSearchRepository.search(queryStringQuery(query), pageable);    }
}
