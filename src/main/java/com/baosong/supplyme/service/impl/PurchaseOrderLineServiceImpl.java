package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.enumeration.PurchaseOrderStatus;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.PurchaseOrderLineRepository;
import com.baosong.supplyme.repository.search.PurchaseOrderLineSearchRepository;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.PurchaseOrderLineService;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing PurchaseOrderLine.
 */
@Service
@Transactional
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderLineServiceImpl.class);

    private final PurchaseOrderLineRepository purchaseOrderLineRepository;

    private final PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository;

    @Autowired
    private DemandService demandService;

    public PurchaseOrderLineServiceImpl(PurchaseOrderLineRepository purchaseOrderLineRepository,
            PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository) {
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
        return this.saveAndCascaseIndex(purchaseOrderLine);
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
    public void delete(Long id) throws ServiceException {
        log.debug("Request to delete PurchaseOrderLine : {}", id);
        try {
            PurchaseOrderLine purchaseOrderLine = findOne(id).get();
            if (!PurchaseOrderStatus.NEW.equals(purchaseOrderLine.getPurchaseOrder().getStatus())) {
                throw new ServiceException("Purchase order status does not allow you to delete a line");
            }
            Demand demand = purchaseOrderLine.getDemand();
            demand.setQuantityOrdered(demand.getQuantityOrdered() - purchaseOrderLine.getQuantity());
            demandService.save(demand);
            purchaseOrderLineRepository.deleteById(id);
            purchaseOrderLineSearchRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ServiceException(new StringBuilder("PurchaseOrderLine not found for id ").append(id).toString());
        }
    }

    /**
     * Search for the purchaseOrderLine corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderLine> search(String query, Long supplierId, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseOrderLines for query {}", query);
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("purchaseOrder.status", "SENT"));
        if (supplierId != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("purchaseOrder.supplier.id", supplierId));
        }
        if (!StringUtils.isEmpty(query)) {
            booleanQueryBuilder.must(queryStringQuery(query)
            .field("demand.material.partNumber").field("demand.material.name").field("demand.material.description")
            .field("purchaseOrder.code").field("demand.project.*"));
        }
        return purchaseOrderLineSearchRepository.search(booleanQueryBuilder, pageable);
    }

    /**
     * Get the purchase order lines related to a demand id
     */
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderLine> getByDemandId(Long demandId) {
        return purchaseOrderLineRepository.findByDemandId(demandId);
    }

    @Override
    public PurchaseOrderLine saveAndCascaseIndex(PurchaseOrderLine purchaseOrderLine) {
        boolean isNew = purchaseOrderLine.getId() == null;
        PurchaseOrderLine result = purchaseOrderLineRepository.save(purchaseOrderLine);
        purchaseOrderLineSearchRepository.save(result);
        if (!isNew) {
            // TODO Cascade on delivery note lines
        }
        return result;
    }
}
