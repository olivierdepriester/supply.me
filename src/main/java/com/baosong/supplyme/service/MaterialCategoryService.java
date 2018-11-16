package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.MaterialCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing MaterialCategory.
 */
public interface MaterialCategoryService {

    /**
     * Save a materialCategory.
     *
     * @param materialCategory the entity to save
     * @return the persisted entity
     */
    MaterialCategory save(MaterialCategory materialCategory);

    /**
     * Get all the materialCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialCategory> findAll(Pageable pageable);


    /**
     * Get the "id" materialCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialCategory> findOne(Long id);

    /**
     * Delete the "id" materialCategory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the materialCategory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialCategory> search(String query, Pageable pageable);
}
