package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.repository.MutablePropertiesRepository;
import com.baosong.supplyme.repository.search.MutablePropertiesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MutableProperties.
 */
@Service
@Transactional
public class MutablePropertiesServiceImpl implements MutablePropertiesService {

    private final Logger log = LoggerFactory.getLogger(MutablePropertiesServiceImpl.class);

    private final MutablePropertiesRepository mutablePropertiesRepository;

    private final MutablePropertiesSearchRepository mutablePropertiesSearchRepository;

    public MutablePropertiesServiceImpl(MutablePropertiesRepository mutablePropertiesRepository, MutablePropertiesSearchRepository mutablePropertiesSearchRepository) {
        this.mutablePropertiesRepository = mutablePropertiesRepository;
        this.mutablePropertiesSearchRepository = mutablePropertiesSearchRepository;
    }

    /**
     * Save a mutableProperties.
     *
     * @param mutableProperties the entity to save
     * @return the persisted entity
     */
    @Override
    public MutableProperties save(MutableProperties mutableProperties) {
        log.debug("Request to save MutableProperties : {}", mutableProperties);        MutableProperties result = mutablePropertiesRepository.save(mutableProperties);
        mutablePropertiesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the mutableProperties.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MutableProperties> findAll() {
        log.debug("Request to get all MutableProperties");
        return mutablePropertiesRepository.findAll();
    }


    /**
     * Get one mutableProperties by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MutableProperties> findOne(Long id) {
        log.debug("Request to get MutableProperties : {}", id);
        return mutablePropertiesRepository.findById(id);
    }

    /**
     * Delete the mutableProperties by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MutableProperties : {}", id);
        mutablePropertiesRepository.deleteById(id);
        mutablePropertiesSearchRepository.deleteById(id);
    }

    /**
     * Search for the mutableProperties corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MutableProperties> search(String query) {
        log.debug("Request to search MutableProperties for query {}", query);
        return StreamSupport
            .stream(mutablePropertiesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
