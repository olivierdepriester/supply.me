package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.InvoiceLineService;
import com.baosong.supplyme.domain.InvoiceLine;
import com.baosong.supplyme.repository.InvoiceLineRepository;
import com.baosong.supplyme.repository.search.InvoiceLineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InvoiceLine.
 */
@Service
@Transactional
public class InvoiceLineServiceImpl implements InvoiceLineService {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineServiceImpl.class);

    private final InvoiceLineRepository invoiceLineRepository;

    private final InvoiceLineSearchRepository invoiceLineSearchRepository;

    public InvoiceLineServiceImpl(InvoiceLineRepository invoiceLineRepository, InvoiceLineSearchRepository invoiceLineSearchRepository) {
        this.invoiceLineRepository = invoiceLineRepository;
        this.invoiceLineSearchRepository = invoiceLineSearchRepository;
    }

    /**
     * Save a invoiceLine.
     *
     * @param invoiceLine the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceLine save(InvoiceLine invoiceLine) {
        log.debug("Request to save InvoiceLine : {}", invoiceLine);
        InvoiceLine result = invoiceLineRepository.save(invoiceLine);
        invoiceLineSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceLine> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceLines");
        return invoiceLineRepository.findAll(pageable);
    }


    /**
     * Get one invoiceLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceLine> findOne(Long id) {
        log.debug("Request to get InvoiceLine : {}", id);
        return invoiceLineRepository.findById(id);
    }

    /**
     * Delete the invoiceLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceLine : {}", id);
        invoiceLineRepository.deleteById(id);
        invoiceLineSearchRepository.deleteById(id);
    }

    /**
     * Search for the invoiceLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceLine> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InvoiceLines for query {}", query);
        return invoiceLineSearchRepository.search(queryStringQuery(query), pageable);    }
}
