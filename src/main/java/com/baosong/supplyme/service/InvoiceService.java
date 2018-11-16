package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.Invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Invoice.
 */
public interface InvoiceService {

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save
     * @return the persisted entity
     */
    Invoice save(Invoice invoice);

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Invoice> findAll(Pageable pageable);


    /**
     * Get the "id" invoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Invoice> findOne(Long id);

    /**
     * Delete the "id" invoice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the invoice corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Invoice> search(String query, Pageable pageable);
}
