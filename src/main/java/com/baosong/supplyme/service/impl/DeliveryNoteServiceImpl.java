package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.baosong.supplyme.domain.DeliveryNote;
import com.baosong.supplyme.domain.DeliveryNoteLine;
import com.baosong.supplyme.domain.enumeration.DeliveryNoteStatus;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.DeliveryNoteRepository;
import com.baosong.supplyme.repository.search.DeliveryNoteSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
import com.baosong.supplyme.service.DeliveryNoteService;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DeliveryNote.
 */
@Service
@Transactional
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteServiceImpl.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteSearchRepository deliveryNoteSearchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    @Autowired
    private DemandService demandService;

    public DeliveryNoteServiceImpl(DeliveryNoteRepository deliveryNoteRepository,
            DeliveryNoteSearchRepository deliveryNoteSearchRepository) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteSearchRepository = deliveryNoteSearchRepository;
    }

    /**
     * Save a deliveryNote.
     *
     * @param deliveryNote the entity to save
     * @return the persisted entity
     * @throws ServiceException
     */
    @Override
    public DeliveryNote save(DeliveryNote deliveryNote) throws ServiceException {
        log.debug("Request to save DeliveryNote : {}", deliveryNote);
        DeliveryNote persistedDeliveryNote = null;
        if (deliveryNote.getId() == null) {
            // New delivery note --> Generate a new code
            persistedDeliveryNote = deliveryNote.status(DeliveryNoteStatus.NEW).creationDate(Instant.now())
                    .creationUser(userService.getCurrentUser().get());
        } else {
            // Update -> Retrieve entity from database and update
            persistedDeliveryNote = findOne(deliveryNote.getId()).get().deliveryDate(deliveryNote.getDeliveryDate())
                    .supplier(deliveryNote.getSupplier());
        }
        // Check if the note can be edited
        if (!isEditable(persistedDeliveryNote)) {
            throw new ServiceException(
                    String.format("The Delivery Note %d is not editable", persistedDeliveryNote.getId()),
                    "deliveryNote.not.editable");
        }
        // Update lines
        this.updateDeliveryNoteLines(deliveryNote, persistedDeliveryNote);
        return this.saveAndCascadeIndex(deliveryNote);
    }

    private void updateDeliveryNoteLines(DeliveryNote sourceDeliveryNote, DeliveryNote persistedDeliveryNote)
            throws ServiceException {
        if (persistedDeliveryNote.getId() != null) {
            // Gets the line ids remaining in the delivery to save
            final Set<Long> lineIdsPresent = sourceDeliveryNote.getDeliveryNoteLines().stream()
                    .filter(line -> line.getId() != null).map(DeliveryNoteLine::getId).collect(Collectors.toSet());
            // Identify the lines to remove from the PO
            Set<DeliveryNoteLine> linesToDelete = persistedDeliveryNote.getDeliveryNoteLines().stream()
                    .filter(pol -> !lineIdsPresent.contains(pol.getId())).collect(Collectors.toSet());
            // Remove these lines
            persistedDeliveryNote.getDeliveryNoteLines().removeIf(pol -> linesToDelete.contains(pol));
        }

        // Lines are updated if the call does not remain from a status change
        for (DeliveryNoteLine line : sourceDeliveryNote.getDeliveryNoteLines()) {
            line.setDeliveryNote(persistedDeliveryNote);
            if (sourceDeliveryNote.getId() != null) {
                // If PO update : add new lines or update existing lines
                if (line.getId() == null) {
                    // New line
                    persistedDeliveryNote.getDeliveryNoteLines().add(line);
                } else {
                    // Get the line
                    DeliveryNoteLine persistedLine = persistedDeliveryNote.getDeliveryNoteLines().stream()
                            .filter(l -> l.getId().equals(line.getId())).findAny().get();
                    persistedLine.quantity(line.getQuantity());
                }
            }

            if (line.getPurchaseOrderLine() != null) {
                // Set demand status to ORDERED
                line.setPurchaseOrderLine(
                        this.purchaseOrderLineService.findOne(line.getPurchaseOrderLine().getId()).get());
                /// Calculate et set the delivered quantity for the purchase order line
                this.purchaseOrderLineService
                        .save(line.getPurchaseOrderLine().quantityDelivered(this.purchaseOrderLineService
                                .getQuantityDeliveredFromDeliveryNotes(line.getPurchaseOrderLine().getId())));
                /// Calculate et set the delivered quantity for the demand
                this.demandService.save(line.getPurchaseOrderLine().getDemand().quantityDelivered(this.demandService
                        .getQuantityDeliveredFromPO(line.getPurchaseOrderLine().getDemand().getId())));
            }
        }
    }

    /**
     * Check if a delivery note can be edited.
     *
     * @param deliveryNote the delivery note to check
     * @return
     */
    private boolean isEditable(DeliveryNote deliveryNote) {
        return DeliveryNoteStatus.NEW.equals(deliveryNote.getStatus())
                && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.DELIVERY_MANAGER);
    }

    /**
     * Get all the deliveryNotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNote> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryNotes");
        return deliveryNoteRepository.findAll(pageable);
    }

    /**
     * Get one deliveryNote by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryNote> findOne(Long id) {
        log.debug("Request to get DeliveryNote : {}", id);
        return deliveryNoteRepository.findById(id);
    }

    /**
     * Delete the deliveryNote by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryNote : {}", id);
        deliveryNoteRepository.deleteById(id);
        deliveryNoteSearchRepository.deleteById(id);
    }

    /**
     * Search for the deliveryNote corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNote> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeliveryNotes for query {}", query);
        return deliveryNoteSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public DeliveryNote saveAndCascadeIndex(DeliveryNote deliveryNote) {
        DeliveryNote result = deliveryNoteRepository.save(deliveryNote);
        deliveryNoteSearchRepository.save(result);
        return result;
    }
}
