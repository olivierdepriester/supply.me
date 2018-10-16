package com.baosong.supplyme.service;

import java.util.Optional;

import com.baosong.supplyme.domain.DeliveryNote;
import com.baosong.supplyme.domain.errors.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DeliveryNote.
 */
public interface DeliveryNoteService {

    /**
     * Save a delivery note.
     *
     * @param deliveryNote the entity to save
     * @return the persisted entity
     * @exception ServiceException
     */
    DeliveryNote save(DeliveryNote deliveryNote) throws ServiceException;

    /**
     * Save a delivery note and update indices using it.
     *
     * @param deliveryNote
     * @return
     */
    DeliveryNote saveAndCascadeIndex(DeliveryNote deliveryNote);

    /**
     * Get all the deliveryNotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryNote> findAll(Pageable pageable);


    /**
     * Get the "id" deliveryNote.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DeliveryNote> findOne(Long id);

    /**
     * Delete the "id" deliveryNote.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the deliveryNote corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryNote> search(String query, Pageable pageable);
}
