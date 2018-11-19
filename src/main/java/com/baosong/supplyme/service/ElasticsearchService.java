package com.baosong.supplyme.service;

import java.util.List;

import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.dto.ElasticsearchIndexResult;

/**
 * Service Interface for managing Elasticsearch tools.
 */
public interface ElasticsearchService {

    /**
     * Reindex all entities.
     * This method will drop all the indices, recreate them and index all the persisted entities from db
     *
     * @return Number of reindexed entities
     * @throws ServiceException
     */
    List<ElasticsearchIndexResult> reindexAll() throws ServiceException;

    /**
     * Reindex all the items of 1 entity
     * This method will drop the index, recreate it and index all the items of this entity from db
     * @param className
     * @return Number of reindexed entities
     * @throws ServiceException
     */
    ElasticsearchIndexResult reindexOneClass(String className) throws ServiceException;

    /**
     * Reindex an item.
     * The item is deleted of the index then reinserted.
     * The index mapping won't be modified.
     *
     * @param className Class name of the entity to reindex
     * @param id Id of the entity
     * @return
     * @throws ServiceException
     */
    ElasticsearchIndexResult reindexOneItem(String className, Long id) throws ServiceException;

    /**
     * Get the list of entities that can be reindexed.
     * @return List of short class name.
     * @throws ServiceException
     */
    List<ElasticsearchIndexResult> getIndexableClasses() throws ServiceException;
}
