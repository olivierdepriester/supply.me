package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.DeliveryNoteService;
import com.baosong.supplyme.domain.DeliveryNote;
import com.baosong.supplyme.repository.DeliveryNoteRepository;
import com.baosong.supplyme.repository.search.DeliveryNoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DeliveryNote.
 */
@Service
@Transactional
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteServiceImpl.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteSearchRepository deliveryNoteSearchRepository;

    public DeliveryNoteServiceImpl(DeliveryNoteRepository deliveryNoteRepository, DeliveryNoteSearchRepository deliveryNoteSearchRepository) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteSearchRepository = deliveryNoteSearchRepository;
    }

    /**
     * Save a deliveryNote.
     *
     * @param deliveryNote the entity to save
     * @return the persisted entity
     */
    @Override
    public DeliveryNote save(DeliveryNote deliveryNote) {
        log.debug("Request to save DeliveryNote : {}", deliveryNote);
        DeliveryNote result = deliveryNoteRepository.save(deliveryNote);
        deliveryNoteSearchRepository.save(result);
        return result;
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
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryNote> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeliveryNotes for query {}", query);
        return deliveryNoteSearchRepository.search(queryStringQuery(query), pageable);    }
}
