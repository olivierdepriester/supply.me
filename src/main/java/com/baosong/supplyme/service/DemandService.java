package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.Demand;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Demand.
 */
public interface DemandService {

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     */
    Demand save(Demand demand);

    /**
     * Get all the demands.
     *
     * @return the list of entities
     */
    List<Demand> findAll();


    /**
     * Get the "id" demand.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Demand> findOne(Long id);

    /**
     * Delete the "id" demand.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the demand corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Demand> search(String query);
}
