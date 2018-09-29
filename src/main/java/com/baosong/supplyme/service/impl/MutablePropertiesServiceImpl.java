package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.domain.enumeration.PropertiesKey;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.MutablePropertiesRepository;
import com.baosong.supplyme.repository.search.MutablePropertiesSearchRepository;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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

    private static Long purchaseOrderCode;

    public MutablePropertiesServiceImpl(MutablePropertiesRepository mutablePropertiesRepository,
            MutablePropertiesSearchRepository mutablePropertiesSearchRepository) {
        this.mutablePropertiesRepository = mutablePropertiesRepository;
        this.mutablePropertiesSearchRepository = mutablePropertiesSearchRepository;
        purchaseOrderCode = getPropertyAsLong(PropertiesKey.PURCHASE_ORDER_CODE_GENERATOR);
    }

    /**
     * Get a mutable parameter
     *
     * @param key
     * @return The parameter linked to the key
     * @throws NoSuchElementException
     */
    private MutableProperties getByKey(PropertiesKey key) throws NoSuchElementException {
        return this.mutablePropertiesSearchRepository.search(QueryBuilders.matchQuery("key", key.toString())).iterator().next();
    }

    /**
     * Get a new purchase order code and increments the counter
     *
     * @return a new formatted purchase order code
     * @throws ServiceException if the property could not be saved
     */
    @Override
    public String getNewPurchaseCode() throws ServiceException {
        save(PropertiesKey.PURCHASE_ORDER_CODE_GENERATOR, ++purchaseOrderCode);
        return String.format("%010d", purchaseOrderCode);
    }

    private Long getPropertyAsLong(PropertiesKey key) {
        try {
            MutableProperties property = this.getByKey(key);
            return Long.parseLong(property.getValue());
        } catch (NoSuchElementException e) {
            MutableProperties property = new MutableProperties();
            property.setKey(key);
            property.setValue("0");
            property.setValueType(Long.class.getCanonicalName());
            save(property);
            return 0L;
        }
    }

    private <T> MutableProperties save(PropertiesKey key, T value) throws ServiceException {
        MutableProperties property = getByKey(key);
        if (value.getClass().getCanonicalName().equals(property.getValueType())) {
            property.setValue(value.toString());
            return save(property);
        } else {
            throw new ServiceException(String.format("{value} must be of type %s. Received : %s",
                    value.getClass().getCanonicalName(), property.getValueType()));
        }
    }

    /**
     * Save a mutableProperties.
     *
     * @param mutableProperties the entity to save
     * @return the persisted entity
     */
    @Override
    public MutableProperties save(MutableProperties mutableProperties) {
        log.debug("Request to save MutableProperties : {}", mutableProperties);
        MutableProperties result = mutablePropertiesRepository.save(mutableProperties);
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