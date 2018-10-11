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
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
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
        boolean statusChange = false;
        if (purchaseOrder.getId() == null) {
            // New purchase order --> Generate a new code
            persistedPurchaseOrder = purchaseOrder.status(PurchaseOrderStatus.NEW)
                    .code(mutablePropertiesService.getNewPurchaseCode()).creationDate(Instant.now())
                    .creationUser(userService.getCurrentUser().get());
        } else {
            // Update -> Retrieve entity from database
            persistedPurchaseOrder = findOne(purchaseOrder.getId()).get();
            if (persistedPurchaseOrder.getStatus().equals(purchaseOrder.getStatus())) {
                // Update value fields
                persistedPurchaseOrder.expectedDate(purchaseOrder.getExpectedDate())
                        .supplier(purchaseOrder.getSupplier());
            } else {
                // Update status
                persistedPurchaseOrder.status(purchaseOrder.getStatus());
                statusChange = true;
            }
        }
        if (!statusChange) {
            if(!isEditable(persistedPurchaseOrder)) {
                throw new ServiceException(String.format("The purchase order %d can not be edited by the current user", persistedPurchaseOrder.getId()));
            }
            this.updatePurchaseOrderLines(purchaseOrder, persistedPurchaseOrder);
            persistedPurchaseOrder
                .quantity(persistedPurchaseOrder.getPurchaseOrderLines().stream().mapToDouble(PurchaseOrderLine::getQuantity).sum())
                .amount(persistedPurchaseOrder.getPurchaseOrderLines().stream().mapToDouble(pol -> pol.getQuantity() * pol.getOrderPrice()).sum())
                .numberOfMaterials(persistedPurchaseOrder.getPurchaseOrderLines().stream().mapToLong(pol -> pol.getDemand().getMaterial().getId()).distinct().count());
        }
        PurchaseOrder result = purchaseOrderRepository.save(persistedPurchaseOrder);
        purchaseOrderSearchRepository.save(result);
        return result;
    }

    /**
     * Update the lines of a purchase order
     *
     * @param sourcePurchaseOrder    : source purchase order (bean with user data
     * @param persistedPurchaseOrder : persisted version of the purchase order.
     *                                  If the purchase order is new, persistedPurchaseOrder and sourcePurchaseOrder are the same
     * @throws ServiceException
     */
    private void updatePurchaseOrderLines(PurchaseOrder sourcePurchaseOrder, PurchaseOrder persistedPurchaseOrder)
            throws ServiceException {
        if (persistedPurchaseOrder.getId() == null) {
            // Gets the POLs ids remaining in the PO to save
            final Set<Long> purchaseOrderLineIdsPresent = sourcePurchaseOrder.getPurchaseOrderLines().stream()
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

        // Lines are updated if the call does not remain from a status change
        for (PurchaseOrderLine line : sourcePurchaseOrder.getPurchaseOrderLines()) {
            line.purchaseOrder(persistedPurchaseOrder);
            if (sourcePurchaseOrder.getId() != null) {
                // If PO update : add new lines or update existing lines
                if (line.getId() == null) {
                    // New line
                    persistedPurchaseOrder.getPurchaseOrderLines().add(line);
                } else {
                    // Get the line
                    PurchaseOrderLine persistedLine = persistedPurchaseOrder.getPurchaseOrderLines().stream()
                            .filter(l -> l.getId().equals(line.getId())).findAny().get();
                    persistedLine.quantity(line.getQuantity()).orderPrice(line.getOrderPrice());
                }
            }
            if (line.getDemand() != null) {
                // Set demand status to ORDERED
                line.setDemand(demandService.changeStatus(line.getDemand().getId(), DemandStatus.ORDERED));
                /// Calculate the demand ordered quantity
                double quantityOrdered = demandService.getQuantityOrderedFromPO(line.getDemand().getId());
                line.getDemand().setQuantityOrdered(quantityOrdered);
                demandService.save(line.getDemand());
            }
        }
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
    public void delete(Long id) throws ServiceException {
        log.debug("Request to delete PurchaseOrder : {}", id);
        PurchaseOrder purchaseOrder = findOne(id).get();
        if (!isEditable(purchaseOrder)) {
            throw new ServiceException(
                    String.format("The purchase order %d can not be deleted by the current user", id));
        }
        for (PurchaseOrderLine line : purchaseOrder.getPurchaseOrderLines()) {
            // Get demand
            Demand demand = line.getDemand();
            demand.setQuantityOrdered(demand.getQuantityOrdered() - line.getQuantity());
            if (demand.getQuantityOrdered() <= 0) {
                // If no quantity remaining --> status decrease
                demand.setStatus(DemandStatus.APPROVED);
            }
            // Update line
            demandService.save(demand);
        }
        purchaseOrderRepository.delete(purchaseOrder);
        purchaseOrderSearchRepository.deleteById(id);
    }

    /**
     * Check if a purchase order can be edited
     *
     * @param purchaseOrder
     * @return
     */
    private boolean isEditable(PurchaseOrder purchaseOrder) {
        return PurchaseOrderStatus.NEW.equals(purchaseOrder.getStatus())
                && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PURCHASER);
    }

    /**
     * Search for the purchaseOrder corresponding to the query.
     *
     * @param query    the search query
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
