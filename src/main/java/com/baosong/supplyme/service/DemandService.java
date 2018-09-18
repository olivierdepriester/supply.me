package com.baosong.supplyme.service;

import java.util.List;
import java.util.Optional;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.errors.ServiceException;

/**
 * Service Interface for managing Demand.
 */
public interface DemandService {

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     * @throws ServiceException 
     */
    Demand save(Demand demand) throws ServiceException;

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

	/**
	 * Search for demands based on the parameters below
	 * @param query query of the full text search
	 * @param materialId material filter
	 * @param projectId project filter
	 * @param demandStatus status filter
	 * @return
	 */
    List<Demand> search(String query, Long materialId, Long projectId, DemandStatus demandStatus);
    
    /**
     * Change the status of a demand
     * @param id Demand identifier
     * @param status Status to set
     * @return The modified demand
     * @exception ServiceException In case of business rules violation
     */
    Demand changeStatus(Long id, DemandStatus status) throws ServiceException;

	void rebuildIndex();
}
