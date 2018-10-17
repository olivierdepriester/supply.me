package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.DeliveryNoteLineService;
import com.baosong.supplyme.domain.DeliveryNoteLine;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.repository.DeliveryNoteLineRepository;
import com.baosong.supplyme.repository.search.DeliveryNoteLineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DeliveryNoteLine.
 */
@Service
@Transactional
public class DeliveryNoteLineServiceImpl implements DeliveryNoteLineService {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteLineServiceImpl.class);

    private final DeliveryNoteLineRepository deliveryNoteLineRepository;

    private final DeliveryNoteLineSearchRepository deliveryNoteLineSearchRepository;

    public DeliveryNoteLineServiceImpl(DeliveryNoteLineRepository deliveryNoteLineRepository, DeliveryNoteLineSearchRepository deliveryNoteLineSearchRepository) {
        this.deliveryNoteLineRepository = deliveryNoteLineRepository;
        this.deliveryNoteLineSearchRepository = deliveryNoteLineSearchRepository;
    }

    /**
     * Save a deliveryNoteLine.
     *
     * @param deliveryNoteLine the entity to save
     * @return the persisted entity
     */
    @Override
    public DeliveryNoteLine save(DeliveryNoteLine deliveryNoteLine) {
        log.debug("Request to save DeliveryNoteLine : {}", deliveryNoteLine);        DeliveryNoteLine result = deliveryNoteLineRepository.save(deliveryNoteLine);
        deliveryNoteLineSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the deliveryNoteLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNoteLine> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryNoteLines");
        return deliveryNoteLineRepository.findAll(pageable);
    }


    /**
     * Get one deliveryNoteLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryNoteLine> findOne(Long id) {
        log.debug("Request to get DeliveryNoteLine : {}", id);
        return deliveryNoteLineRepository.findById(id);
    }

    /**
     * Delete the deliveryNoteLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryNoteLine : {}", id);
        deliveryNoteLineRepository.deleteById(id);
        deliveryNoteLineSearchRepository.deleteById(id);
    }

    /**
     * Search for the deliveryNoteLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNoteLine> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeliveryNoteLines for query {}", query);
        return deliveryNoteLineSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryNoteLine> getByPurchaseOrderLineId(Long purchaseOrderLineId) {
        return this.deliveryNoteLineRepository.findAll(
            Example.of(new DeliveryNoteLine().purchaseOrderLine(new PurchaseOrderLine().id(purchaseOrderLineId)))
            );
    }
}
