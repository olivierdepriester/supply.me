package com.baosong.supplyme.service;

import com.baosong.supplyme.domain.InvoiceLine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing InvoiceLine.
 */
public interface InvoiceLineService {

    /**
     * Save a invoiceLine.
     *
     * @param invoiceLine the entity to save
     * @return the persisted entity
     */
    InvoiceLine save(InvoiceLine invoiceLine);

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InvoiceLine> findAll(Pageable pageable);


    /**
     * Get the "id" invoiceLine.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InvoiceLine> findOne(Long id);

    /**
     * Delete the "id" invoiceLine.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the invoiceLine corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InvoiceLine> search(String query, Pageable pageable);
}
