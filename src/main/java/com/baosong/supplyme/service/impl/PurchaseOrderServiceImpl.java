package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.PurchaseOrderService;
import com.baosong.supplyme.domain.PurchaseOrder;
import com.baosong.supplyme.repository.PurchaseOrderRepository;
import com.baosong.supplyme.repository.search.PurchaseOrderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PurchaseOrder.
 */
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderSearchRepository purchaseOrderSearchRepository;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderSearchRepository purchaseOrderSearchRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderSearchRepository = purchaseOrderSearchRepository;
    }

    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        log.debug("Request to save PurchaseOrder : {}", purchaseOrder);        PurchaseOrder result = purchaseOrderRepository.save(purchaseOrder);
        purchaseOrderSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the purchaseOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrders");
        return purchaseOrderRepository.findAll(pageable);
    }


    /**
     * Get one purchaseOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findOne(Long id) {
        log.debug("Request to get PurchaseOrder : {}", id);
        return purchaseOrderRepository.findById(id);
    }

    /**
     * Delete the purchaseOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrder : {}", id);
        purchaseOrderRepository.deleteById(id);
        purchaseOrderSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchaseOrder corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseOrders for query {}", query);
        return purchaseOrderSearchRepository.search(queryStringQuery(query), pageable);    }
}
