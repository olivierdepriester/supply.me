package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;

import com.baosong.supplyme.domain.MaterialAvailability;
import com.baosong.supplyme.repository.MaterialAvailabilityRepository;
import com.baosong.supplyme.repository.search.MaterialAvailabilitySearchRepository;
import com.baosong.supplyme.service.MaterialAvailabilityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MaterialAvailability.
 */
@Service
@Transactional
public class MaterialAvailabilityServiceImpl implements MaterialAvailabilityService {

    private final Logger log = LoggerFactory.getLogger(MaterialAvailabilityServiceImpl.class);

    private final MaterialAvailabilityRepository materialAvailabilityRepository;

    private final MaterialAvailabilitySearchRepository materialAvailabilitySearchRepository;

    public MaterialAvailabilityServiceImpl(MaterialAvailabilityRepository materialAvailabilityRepository, MaterialAvailabilitySearchRepository materialAvailabilitySearchRepository) {
        this.materialAvailabilityRepository = materialAvailabilityRepository;
        this.materialAvailabilitySearchRepository = materialAvailabilitySearchRepository;
    }

    /**
     * Save a materialAvailability.
     *
     * @param materialAvailability the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialAvailability save(MaterialAvailability materialAvailability) {
        log.debug("Request to save MaterialAvailability : {}", materialAvailability);
        MaterialAvailability result = materialAvailabilityRepository.save(materialAvailability);
        materialAvailabilitySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the materialAvailabilities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialAvailability> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialAvailabilities");
        return materialAvailabilityRepository.findAll(pageable);
    }


    /**
     * Get one materialAvailability by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialAvailability> findOne(Long id) {
        log.debug("Request to get MaterialAvailability : {}", id);
        return materialAvailabilityRepository.findById(id);
    }

    /**
     * Delete the materialAvailability by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialAvailability : {}", id);
        materialAvailabilityRepository.deleteById(id);
        materialAvailabilitySearchRepository.deleteById(id);
    }

    /**
     * Search for the materialAvailability corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialAvailability> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MaterialAvailabilities for query {}", query);
        return materialAvailabilitySearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<MaterialAvailability> getAllForMaterial(Long materialId) {
        return this.materialAvailabilityRepository.getByMaterialIdOrderByUpdateDateDesc(materialId);
	}
}
