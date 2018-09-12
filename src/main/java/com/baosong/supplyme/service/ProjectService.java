package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.Project;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {

    /**
     * Save a project.
     *
     * @param project the entity to save
     * @return the persisted entity
     */
    Project save(Project project);

    /**
     * Get all the projects.
     *
     * @return the list of entities
     */
    List<Project> findAll();


    /**
     * Get the "id" project.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Project> findOne(Long id);

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the project corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Project> search(String query);
}
