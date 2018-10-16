package com.baosong.supplyme.service;

import java.util.List;
import java.util.Optional;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.errors.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PurchaseOrderLine.
 */
public interface PurchaseOrderLineService {

    /**
     * Save a purchaseOrderLine.
     *
     * @param purchaseOrderLine the entity to save
     * @return the persisted entity
     */
    PurchaseOrderLine save(PurchaseOrderLine purchaseOrderLine);

    /**
     * Save a purchaseOrderLine and update indices using it.
     *
     * @param purchaseOrderLine the entity to save
     * @return the persisted entity
     */
    PurchaseOrderLine saveAndCascaseIndex(PurchaseOrderLine purchaseOrderLine);

    /**
     * Get all the purchaseOrderLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseOrderLine> findAll(Pageable pageable);


    /**
     * Get the "id" purchaseOrderLine.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PurchaseOrderLine> findOne(Long id);

    /**
     * Delete the "id" purchaseOrderLine.
     *
     * @param id the id of the entity
     */
    void delete(Long id) throws ServiceException;

    /**
     * Search for the purchaseOrderLine corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @param supplierId purchase order supplier identifier
     * @return the list of entities
     */
    Page<PurchaseOrderLine> search(String query, Long supplierId, Pageable pageable);

    /**
     * Get the purchase order lines linked to a demand
     * @param demandId the demand identifier
     * @return
     */
    List<PurchaseOrderLine> getByDemandId(Long demandId);
}
