package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.InvoiceService;
import com.baosong.supplyme.domain.Invoice;
import com.baosong.supplyme.repository.InvoiceRepository;
import com.baosong.supplyme.repository.search.InvoiceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceSearchRepository invoiceSearchRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceSearchRepository invoiceSearchRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceSearchRepository = invoiceSearchRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save
     * @return the persisted entity
     */
    @Override
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);        Invoice result = invoiceRepository.save(invoice);
        invoiceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }


    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
        invoiceSearchRepository.deleteById(id);
    }

    /**
     * Search for the invoice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Invoices for query {}", query);
        return invoiceSearchRepository.search(queryStringQuery(query), pageable);    }
}
