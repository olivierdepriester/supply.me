package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.DemandCategoryService;
import com.baosong.supplyme.domain.DemandCategory;
import com.baosong.supplyme.repository.DemandCategoryRepository;
import com.baosong.supplyme.repository.search.DemandCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DemandCategory.
 */
@Service
@Transactional
public class DemandCategoryServiceImpl implements DemandCategoryService {

    private final Logger log = LoggerFactory.getLogger(DemandCategoryServiceImpl.class);

    private final DemandCategoryRepository demandCategoryRepository;

    private final DemandCategorySearchRepository demandCategorySearchRepository;

    public DemandCategoryServiceImpl(DemandCategoryRepository demandCategoryRepository, DemandCategorySearchRepository demandCategorySearchRepository) {
        this.demandCategoryRepository = demandCategoryRepository;
        this.demandCategorySearchRepository = demandCategorySearchRepository;
    }

    /**
     * Save a demandCategory.
     *
     * @param demandCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public DemandCategory save(DemandCategory demandCategory) {
        log.debug("Request to save DemandCategory : {}", demandCategory);
        DemandCategory result = demandCategoryRepository.save(demandCategory);
        demandCategorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the demandCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DemandCategory> findAll(Pageable pageable) {
        log.debug("Request to get all DemandCategories");
        return demandCategoryRepository.findAll(pageable);
    }


    /**
     * Get one demandCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DemandCategory> findOne(Long id) {
        log.debug("Request to get DemandCategory : {}", id);
        return demandCategoryRepository.findById(id);
    }

    /**
     * Delete the demandCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemandCategory : {}", id);
        demandCategoryRepository.deleteById(id);
        demandCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the demandCategory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DemandCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DemandCategories for query {}", query);
        return demandCategorySearchRepository.search(queryStringQuery(query), pageable);    }
}
