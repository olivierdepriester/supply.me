package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.repository.PurchaseOrderLineRepository;
import com.baosong.supplyme.repository.search.PurchaseOrderLineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PurchaseOrderLine.
 */
@Service
@Transactional
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderLineServiceImpl.class);

    private final PurchaseOrderLineRepository purchaseOrderLineRepository;

    private final PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository;

    public PurchaseOrderLineServiceImpl(PurchaseOrderLineRepository purchaseOrderLineRepository, PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository) {
        this.purchaseOrderLineRepository = purchaseOrderLineRepository;
        this.purchaseOrderLineSearchRepository = purchaseOrderLineSearchRepository;
    }

    /**
     * Save a purchaseOrderLine.
     *
     * @param purchaseOrderLine the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseOrderLine save(PurchaseOrderLine purchaseOrderLine) {
        log.debug("Request to save PurchaseOrderLine : {}", purchaseOrderLine);
        PurchaseOrderLine result = purchaseOrderLineRepository.save(purchaseOrderLine);
        purchaseOrderLineSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the purchaseOrderLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderLine> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrderLines");
        return purchaseOrderLineRepository.findAll(pageable);
    }


    /**
     * Get one purchaseOrderLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderLine> findOne(Long id) {
        log.debug("Request to get PurchaseOrderLine : {}", id);
        return purchaseOrderLineRepository.findById(id);
    }

    /**
     * Delete the purchaseOrderLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrderLine : {}", id);
        purchaseOrderLineRepository.deleteById(id);
        purchaseOrderLineSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchaseOrderLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderLine> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseOrderLines for query {}", query);
        return purchaseOrderLineSearchRepository.search(queryStringQuery(query), pageable);    }
}
