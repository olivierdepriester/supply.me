package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.MaterialAvailability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing MaterialAvailability.
 */
public interface MaterialAvailabilityService {

    /**
     * Save a materialAvailability.
     *
     * @param materialAvailability the entity to save
     * @return the persisted entity
     */
    MaterialAvailability save(MaterialAvailability materialAvailability);

    /**
     * Get all the materialAvailabilities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialAvailability> findAll(Pageable pageable);


    /**
     * Get the "id" materialAvailability.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialAvailability> findOne(Long id);

    /**
     * Delete the "id" materialAvailability.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the materialAvailability corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialAvailability> search(String query, Pageable pageable);
}
