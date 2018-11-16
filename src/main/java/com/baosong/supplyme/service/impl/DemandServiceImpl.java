package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.search.DemandSearchRepository;
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
 * Service Implementation for managing Demand.
 */
@Service
@Transactional
public class DemandServiceImpl implements DemandService {

    private final Logger log = LoggerFactory.getLogger(DemandServiceImpl.class);

    private final DemandRepository demandRepository;

    private final DemandSearchRepository demandSearchRepository;

    public DemandServiceImpl(DemandRepository demandRepository, DemandSearchRepository demandSearchRepository) {
        this.demandRepository = demandRepository;
        this.demandSearchRepository = demandSearchRepository;
    }

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     */
    @Override
    public Demand save(Demand demand) {
        log.debug("Request to save Demand : {}", demand);
        Demand result = demandRepository.save(demand);
        demandSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the demands.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Demand> findAll() {
        log.debug("Request to get all Demands");
        return demandRepository.findAll();
    }


    /**
     * Get one demand by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Demand> findOne(Long id) {
        log.debug("Request to get Demand : {}", id);
        return demandRepository.findById(id);
    }

    /**
     * Delete the demand by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Demand : {}", id);
        demandRepository.deleteById(id);
        demandSearchRepository.deleteById(id);
    }

    /**
     * Search for the demand corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Demand> search(String query) {
        log.debug("Request to search Demands for query {}", query);
        return StreamSupport
            .stream(demandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
