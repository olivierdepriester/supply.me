package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;

import javax.management.Query;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.MaterialRepository;
import com.baosong.supplyme.repository.search.MaterialSearchRepository;
import com.baosong.supplyme.service.MaterialService;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Material.
 */
@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final Logger log = LoggerFactory.getLogger(MaterialServiceImpl.class);

    private final MaterialRepository materialRepository;

    private final MaterialSearchRepository materialSearchRepository;

    @Autowired
    private ElasticsearchTemplate template;

    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialSearchRepository materialSearchRepository) {
        this.materialRepository = materialRepository;
        this.materialSearchRepository = materialSearchRepository;
    }

    /**
     * Save a material.
     *
     * @param material the entity to save
     * @return the persisted entity
     */
    @Override
    public Material save(Material material) throws ServiceException {
        log.debug("Request to save Material : {}", material);
        // if (!material.getTemporary().booleanValue()) {
        //     Material duplicatedPartNumber = materialRepository.findFirstByPartNumberAndNotId(material.getPartNumber(), material.getId());
        //     if (duplicatedPartNumber != null) {
        //         throw new ServiceException("The part number %s is already used");
        //     }
        // }
        // Material result = materialRepository.save(material);
        materialSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Material> findAll(Pageable pageable) {
        log.debug("Request to get all Materials");
        return materialRepository.findAll(pageable);
    }


    /**
     * Get one material by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Material> findOne(Long id) {
        log.debug("Request to get Material : {}", id);
        return materialRepository.findById(id);
    }

    /**
     * Delete the material by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Material : {}", id);
        materialRepository.deleteById(id);
        materialSearchRepository.deleteById(id);
    }

    /**
     * Search for the material corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Material> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Materials for query {}", query);
        // SearchQuery query2 = new NativeSearchQueryBuilder()
        // .withQuery(queryStringQuery(query.endsWith("*") ? query : new StringBuilder(query).append("*").toString()))
        // .withSort(SortBuilders.fieldSort("partNumber.keyword").order(SortOrder.ASC))
        // .withPageable(pageable)
        // .build();
        // return materialSearchRepository.search(query2);
        return materialSearchRepository.search(queryStringQuery(query.endsWith("*") ? query : new StringBuilder(query).append("*").toString()), pageable);
    }

    @Override
    public void rebuildIndex() {
        template.deleteIndex(Material.class);
        List<Material> items = materialRepository.findAll();
        items.stream().forEach(d -> materialSearchRepository.save(d));
    }
}
