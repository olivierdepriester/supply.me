package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.MaterialCategoryService;
import com.baosong.supplyme.domain.MaterialCategory;
import com.baosong.supplyme.repository.MaterialCategoryRepository;
import com.baosong.supplyme.repository.search.MaterialCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MaterialCategory.
 */
@Service
@Transactional
public class MaterialCategoryServiceImpl implements MaterialCategoryService {

    private final Logger log = LoggerFactory.getLogger(MaterialCategoryServiceImpl.class);

    private final MaterialCategoryRepository materialCategoryRepository;

    private final MaterialCategorySearchRepository materialCategorySearchRepository;

    public MaterialCategoryServiceImpl(MaterialCategoryRepository materialCategoryRepository, MaterialCategorySearchRepository materialCategorySearchRepository) {
        this.materialCategoryRepository = materialCategoryRepository;
        this.materialCategorySearchRepository = materialCategorySearchRepository;
    }

    /**
     * Save a materialCategory.
     *
     * @param materialCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialCategory save(MaterialCategory materialCategory) {
        log.debug("Request to save MaterialCategory : {}", materialCategory);        MaterialCategory result = materialCategoryRepository.save(materialCategory);
        materialCategorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the materialCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialCategory> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialCategories");
        return materialCategoryRepository.findAll(pageable);
    }


    /**
     * Get one materialCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialCategory> findOne(Long id) {
        log.debug("Request to get MaterialCategory : {}", id);
        return materialCategoryRepository.findById(id);
    }

    /**
     * Delete the materialCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialCategory : {}", id);
        materialCategoryRepository.deleteById(id);
        materialCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the materialCategory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MaterialCategories for query {}", query);
        return materialCategorySearchRepository.search(queryStringQuery(query), pageable);    }
}
