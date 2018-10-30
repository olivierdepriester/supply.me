package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.DemandCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DemandCategory.
 */
public interface DemandCategoryService {

    /**
     * Save a demandCategory.
     *
     * @param demandCategory the entity to save
     * @return the persisted entity
     */
    DemandCategory save(DemandCategory demandCategory);

    /**
     * Get all the demandCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DemandCategory> findAll(Pageable pageable);


    /**
     * Get the "id" demandCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DemandCategory> findOne(Long id);

    /**
     * Delete the "id" demandCategory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the demandCategory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DemandCategory> search(String query, Pageable pageable);
}
