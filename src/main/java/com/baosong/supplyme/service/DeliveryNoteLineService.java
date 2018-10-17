package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.DeliveryNoteLine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing DeliveryNoteLine.
 */
public interface DeliveryNoteLineService {

    /**
     * Save a deliveryNoteLine.
     *
     * @param deliveryNoteLine the entity to save
     * @return the persisted entity
     */
    DeliveryNoteLine save(DeliveryNoteLine deliveryNoteLine);

    /**
     * Get all the deliveryNoteLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryNoteLine> findAll(Pageable pageable);


    /**
     * Get the "id" deliveryNoteLine.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DeliveryNoteLine> findOne(Long id);

    /**
     * Delete the "id" deliveryNoteLine.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the deliveryNoteLine corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryNoteLine> search(String query, Pageable pageable);

    /**
     * Get the delivery note lines related to a purchase order line
     * @param purchaseOrderLineId The purchase order line identifier
     * @return
     */
    List<DeliveryNoteLine> getByPurchaseOrderLineId(Long purchaseOrderLineId);
}
