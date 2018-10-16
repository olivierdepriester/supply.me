package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.domain.errors.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Material.
 */
public interface MaterialService {

    /**
     * Save a material.
     *
     * @param material the entity to save
     * @return the persisted entity
     */
    Material save(Material material) throws ServiceException;

    /**
     * Save a material and propagate the modifications on indexed object using it.
     *
     * @param material The modified material.
     * @return
     */
    Material saveAndCascadeIndex(Material material);

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Material> findAll(Pageable pageable);


    /**
     * Get the "id" material.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Material> findOne(Long id);

    /**
     * Delete the "id" material.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the material corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Material> search(String query, Pageable pageable);

    public void rebuildIndex();
}
