package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.PurchaseOrderLine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
    void delete(Long id);

    /**
     * Search for the purchaseOrderLine corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PurchaseOrderLine> search(String query, Pageable pageable);
}
