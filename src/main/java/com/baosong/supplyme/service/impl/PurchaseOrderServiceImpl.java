package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.PurchaseOrder;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.enumeration.PurchaseOrderStatus;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.PurchaseOrderRepository;
import com.baosong.supplyme.repository.search.PurchaseOrderSearchRepository;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.service.PurchaseOrderService;
import com.baosong.supplyme.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing PurchaseOrder.
 */
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderSearchRepository purchaseOrderSearchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DemandService demandService;

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderSearchRepository purchaseOrderSearchRepository) {
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
    public PurchaseOrder save(PurchaseOrder purchaseOrder) throws ServiceException {
        log.debug("Request to save PurchaseOrder : {}", purchaseOrder);
        PurchaseOrder persistedPurchaseOrder = null;
        if (purchaseOrder.getId() == null) {
            // New purchase order --> Generate a new code
            purchaseOrder.status(PurchaseOrderStatus.NEW).code(mutablePropertiesService.getNewPurchaseCode())
                    .creationDate(Instant.now()).creationUser(userService.getCurrentUser().get());
            persistedPurchaseOrder = purchaseOrder;
        } else {
            // Update -> Retrieve entity from database
            persistedPurchaseOrder = findOne(purchaseOrder.getId()).get()
                .expectedDate(purchaseOrder.getExpectedDate())
                .supplier(purchaseOrder.getSupplier());
            // Gets the POLs ids remaining in the PO to save
            final Set<Long> purchaseOrderLineIdsPresent = purchaseOrder.getPurchaseOrderLines().stream()
                    .filter(pol -> pol.getId() != null).map(PurchaseOrderLine::getId).collect(Collectors.toSet());
            // Identify the lines to remove from the PO
            Set<PurchaseOrderLine> linesToDelete = persistedPurchaseOrder.getPurchaseOrderLines().stream()
                    .filter(pol -> !purchaseOrderLineIdsPresent.contains(pol.getId())).collect(Collectors.toSet());
            // Remove these lines
            persistedPurchaseOrder.getPurchaseOrderLines().removeIf(pol -> linesToDelete.contains(pol));

            // Clean the lines and their demand
            for (PurchaseOrderLine line : linesToDelete) {
                // Get demand
                Demand demand = line.getDemand();
                // Delete line
                purchaseOrderLineService.delete(line.getId());
                // Recalculation of the effective ordered quantity
                double quantityOrdered = demandService.getQuantityOrderedFromPO(line.getDemand().getId());
                if (quantityOrdered <= 0) {
                    // If no quantity remaining --> status decrease
                    demand.setStatus(DemandStatus.APPROVED);
                }
                // Update line
                demandService.save(demand.quantityOrdered(quantityOrdered));
            }
        }

        for (PurchaseOrderLine line : purchaseOrder.getPurchaseOrderLines()) {
            if (line.getId() == null) {
                persistedPurchaseOrder.getPurchaseOrderLines().add(line);
                line.purchaseOrder(persistedPurchaseOrder);
            } else {
                PurchaseOrderLine persistedLine = persistedPurchaseOrder.getPurchaseOrderLines().stream()
                    .filter(l -> l.getId().equals(line.getId()))
                    .findAny().get();
                persistedLine.quantity(line.getQuantity()).orderPrice(line.getOrderPrice());
            }

            if (line.getDemand() != null) {
                // Set demand status to ORDERED
                line.setDemand(demandService.changeStatus(line.getDemand().getId(), DemandStatus.ORDERED));
                double quantityOrdered = demandService.getQuantityOrderedFromPO(line.getDemand().getId());
                line.getDemand().setQuantityOrdered(quantityOrdered);
                demandService.save(line.getDemand());
            }
        }
        PurchaseOrder result = purchaseOrderRepository.save(persistedPurchaseOrder);
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
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseOrders for query {}", query);
        return purchaseOrderSearchRepository.search(queryStringQuery(query), pageable);
    }
}
