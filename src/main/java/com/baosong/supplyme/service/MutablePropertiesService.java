package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.MutableProperties;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing MutableProperties.
 */
public interface MutablePropertiesService {

    /**
     * Save a mutableProperties.
     *
     * @param mutableProperties the entity to save
     * @return the persisted entity
     */
    MutableProperties save(MutableProperties mutableProperties);

    /**
     * Get all the mutableProperties.
     *
     * @return the list of entities
     */
    List<MutableProperties> findAll();


    /**
     * Get the "id" mutableProperties.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MutableProperties> findOne(Long id);

    /**
     * Delete the "id" mutableProperties.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the mutableProperties corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<MutableProperties> search(String query);
}
