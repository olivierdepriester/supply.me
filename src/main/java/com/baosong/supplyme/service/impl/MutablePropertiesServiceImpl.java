package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.domain.enumeration.PropertiesKey;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.MutablePropertiesRepository;
import com.baosong.supplyme.repository.search.MutablePropertiesSearchRepository;
import com.baosong.supplyme.service.MutablePropertiesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MutableProperties.
 */
@Service
@Transactional
public class MutablePropertiesServiceImpl implements MutablePropertiesService {

    private final Logger log = LoggerFactory.getLogger(MutablePropertiesServiceImpl.class);

    private final MutablePropertiesRepository mutablePropertiesRepository;

    private final MutablePropertiesSearchRepository mutablePropertiesSearchRepository;

    private static Long PURCHASE_ORDER_CODE;

    private static Long MATERIAL_PART_NUMBER;

    private static Long DEMAND_CODE;

    public MutablePropertiesServiceImpl(MutablePropertiesRepository mutablePropertiesRepository,
            MutablePropertiesSearchRepository mutablePropertiesSearchRepository) throws ServiceException {
        this.mutablePropertiesRepository = mutablePropertiesRepository;
        this.mutablePropertiesSearchRepository = mutablePropertiesSearchRepository;
        PURCHASE_ORDER_CODE = getPropertyAsLong(PropertiesKey.PURCHASE_ORDER_CODE_GENERATOR);
        MATERIAL_PART_NUMBER = getPropertyAsLong(PropertiesKey.MATERIAL_PART_NUMBER_GENERATOR);
        DEMAND_CODE = getPropertyAsLong(PropertiesKey.DEMAND_CODE_GENERATOR);
    }

    /**
     * Get a mutable parameter
     *
     * @param key
     * @return The parameter linked to the key
     * @throws NoSuchElementException
     */
    private MutableProperties getByKey(PropertiesKey key) throws NoSuchElementException {
        return this.mutablePropertiesRepository.findByKey(key);
    }

    /**
     * Get a new purchase order code and increments the counter
     *
     * @return a new formatted purchase order code : PO00000000000
     * @throws ServiceException if the property could not be saved
     */
    @Override
    public String getNewPurchaseCode() throws ServiceException {
        save(PropertiesKey.PURCHASE_ORDER_CODE_GENERATOR, ++PURCHASE_ORDER_CODE);
        return String.format("PO%010d", PURCHASE_ORDER_CODE);
    }

    /**
     * Get a new purchase order code and increments the counter
     *
     * @return a new formatted purchase request code : PR00000000000
     * @throws ServiceException if the property could not be saved
     */
    @Override
    public String getNewDemandCode() throws ServiceException {
        save(PropertiesKey.DEMAND_CODE_GENERATOR, ++DEMAND_CODE);
        return String.format("PR%010d", DEMAND_CODE);
    }


    private Long getPropertyAsLong(PropertiesKey key) throws ServiceException {
        MutableProperties property = null;
        property = this.getByKey(key);
        if (property == null) {
            property = new MutableProperties();
            property.setKey(key);
            property.setValue("0");
            property.setValueType(Long.class.getCanonicalName());
            save(property);
        } else if (!property.getValueType().equals(Long.class.getCanonicalName())) {
            throw new ServiceException(
                    String.format("Mutable property %s is expected to be %s", key, Double.class.getCanonicalName()));
        }
        return Long.parseLong(property.getValue());
    }

    private Double getPropertyAsDouble(PropertiesKey key, Double defaultValue) throws ServiceException {
        MutableProperties property = null;
        property = this.getByKey(key);
        if (property == null) {
            property = new MutableProperties();
            property.setKey(key);
            property.setValue(Double.toString(defaultValue));
            property.setValueType(defaultValue.getClass().getCanonicalName());
            save(property);
        } else if (!property.getValueType().equals(defaultValue.getClass().getCanonicalName())) {
            throw new ServiceException(
                    String.format("Mutable property %s is expected to be %s", key, defaultValue.getClass().getCanonicalName()));
        }
        return Double.parseDouble(property.getValue());
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

    /**
     * Get a new part number code and increments the counter
     *
     * @return a new formatted purchase order code
     * @throws ServiceException if the property could not be saved
     */
    @Override
    public String getNewPartNumber() throws ServiceException {
        save(PropertiesKey.MATERIAL_PART_NUMBER_GENERATOR, ++MATERIAL_PART_NUMBER);
        return String.format("MA%010d", MATERIAL_PART_NUMBER);
    }

    @Override
    public Optional<Double> getSecondValidationThresholdAmount() throws ServiceException {
        Double value = this.getPropertyAsDouble(PropertiesKey.THRESHOLD_SECOND_LEVEL_APPROVAL, Double.POSITIVE_INFINITY);
        return Optional.of(value);
    }
}
