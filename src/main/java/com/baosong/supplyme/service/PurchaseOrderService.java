package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.PurchaseOrder;
import com.baosong.supplyme.domain.errors.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing PurchaseOrder.
 */
public interface PurchaseOrderService {

    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrder the entity to save
     * @return the persisted entity
     * @exception ServiceException
     */
    PurchaseOrder save(PurchaseOrder purchaseOrder) throws ServiceException;

    /**
     * Get all the purchaseOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseOrder> findAll(Pageable pageable);


    /**
     * Get the "id" purchaseOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PurchaseOrder> findOne(Long id);

    /**
     * Delete the "id" purchaseOrder.
     *
     * @param id the id of the entity
     * @exception ServiceException
     *
     */
    void delete(Long id) throws ServiceException;

    /**
     * Search for the purchaseOrder corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseOrder> search(String query, Pageable pageable);
}
