package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.errors.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Supplier.
 */
public interface SupplierService {

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save
     * @return the persisted entity
     * @throws ServiceException
     */
    Supplier save(Supplier supplier) throws ServiceException;

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Supplier> findAll(Pageable pageable);


    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Supplier> findOne(Long id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity
     * @throws ServiceException
     */
    void delete(Long id) throws ServiceException;

    /**
     * Search for the supplier corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Supplier> search(String query, Pageable pageable);
}
