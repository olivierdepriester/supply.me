package com.baosong.supplyme.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.AuthorityRepository;
import com.baosong.supplyme.repository.DeliveryNoteRepository;
import com.baosong.supplyme.repository.DemandCategoryRepository;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.DepartmentRepository;
import com.baosong.supplyme.repository.InvoiceRepository;
import com.baosong.supplyme.repository.MaterialAvailabilityRepository;
import com.baosong.supplyme.repository.MaterialCategoryRepository;
import com.baosong.supplyme.repository.MaterialRepository;
import com.baosong.supplyme.repository.MutablePropertiesRepository;
import com.baosong.supplyme.repository.ProjectRepository;
import com.baosong.supplyme.repository.PurchaseOrderLineRepository;
import com.baosong.supplyme.repository.PurchaseOrderRepository;
import com.baosong.supplyme.repository.SupplierRepository;
import com.baosong.supplyme.repository.UserRepository;
import com.baosong.supplyme.service.ElasticsearchService;
import com.baosong.supplyme.service.dto.ElasticsearchIndexResult;
import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for ElasticSearch indices management.
 *
 * @author Olivier.
 */
@Service
@Transactional
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    private final static String BASE_PACKAGE = "com.baosong.supplyme";

    /**
     * Template to manipulate ES indices
     *
     * Autowired required is set to false because I don't know yet how to inject
     * template for the unit tests.
     *
     */
    @Autowired(required = false)
    private JestElasticsearchTemplate elasticsearchTemplate;

    private Map<String, Pair<Class<?>, JpaRepository<?, ?>>> repositories;

    public ElasticsearchServiceImpl(DemandRepository demandRepository, DepartmentRepository departmentRepository,
            SupplierRepository supplierRepository, DemandCategoryRepository demandCategoryRepository,
            MaterialAvailabilityRepository materialAvailabilityRepository,
            MaterialCategoryRepository materialCategoryRepository, MaterialRepository materialRepository,
            ProjectRepository projectRepository, UserRepository userRepository,
            MutablePropertiesRepository mutablePropertiesRepository, PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderLineRepository purchaseOrderLineRepository, DeliveryNoteRepository deliveryNoteRepository,
            InvoiceRepository invoiceRepository) {
        this.repositories = new HashMap<>();
        this.addRepository(deliveryNoteRepository);
        this.addRepository(demandCategoryRepository);
        this.addRepository(demandRepository);
        this.addRepository(departmentRepository);
        this.addRepository(invoiceRepository);
        this.addRepository(materialAvailabilityRepository);
        this.addRepository(materialCategoryRepository);
        this.addRepository(materialRepository);
        this.addRepository(mutablePropertiesRepository);
        this.addRepository(projectRepository);
        this.addRepository(purchaseOrderRepository);
        this.addRepository(purchaseOrderLineRepository);
        this.addRepository(supplierRepository);
        this.addRepository(userRepository);
    }

    private void addRepository(JpaRepository<?, ?> repository) {
        Type[] interfaces = repository.getClass().getInterfaces();
        for (Type t : interfaces) {
            if (t instanceof Class<?>) {
                Class<?> clazz = (Class<?>) t;
                if (clazz.getPackage().getName().startsWith(BASE_PACKAGE)) {
                    Type genericInterface = clazz.getGenericInterfaces()[0];
                    Class<?> entityType = (Class<?>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
                    String name = entityType.getSimpleName();
                    this.repositories.put(name, Pair.of((Class<?>) entityType, repository));
                    break;
                }
            }
        }
    }

    /**
     * Reindex all entities. This method will drop all the indices, recreate them
     * and index all the persisted entities from db
     *
     * @return Number of reindexed entities
     * @throws ServiceException
     */
    public List<ElasticsearchIndexResult> reindexAll() throws ServiceException {
        log.debug("Reindex all");
        List<ElasticsearchIndexResult> result = new ArrayList<>(this.repositories.size());
        for (Entry<String, Pair<Class<?>, JpaRepository<?, ?>>> entry : this.repositories.entrySet()) {
            result.add(ElasticsearchIndexResult.of(entry.getKey(), this.reindex(entry.getValue())));
        }
        return result;
    }

    /**
     * Reindex all the items of 1 entity This method will drop the index, recreate
     * it and index all the items of this entity from db
     *
     * @param className
     * @return Number of reindexed entities
     * @throws ServiceException
     */
    public ElasticsearchIndexResult reindexOneClass(String className) throws ServiceException {
        log.debug("Reindex class {}", className);
        return ElasticsearchIndexResult.of(className, this.reindex(this.repositories.get(className)));
    }

    /**
     * Reindex an item. The item is deleted of the index then reinserted. The index
     * mapping won't be modified.
     *
     * @param className Class name of the entity to reindex
     * @param id        Id of the entity
     * @return
     * @throws ServiceException
     */
    public ElasticsearchIndexResult reindexOneItem(String className, Long id) throws ServiceException {
        log.debug("Reindex class {} id {}", className, id);
        return ElasticsearchIndexResult.of(className, 0);
    }

    /**
     * Get the list of entities that can be reindexed.
     *
     * @return List of short class name.
     * @throws ServiceException
     */
    public List<ElasticsearchIndexResult> getIndexableClasses() throws ServiceException {
        log.debug("Get indexable classes");
        return this.repositories.keySet().stream().sorted().map(r -> new ElasticsearchIndexResult(r, null))
                .collect(Collectors.toList());
    }

    private int reindex(Pair<Class<?>, JpaRepository<?, ?>> entityClassRepository) {
        this.elasticsearchTemplate.deleteIndex(entityClassRepository.getFirst());
        this.elasticsearchTemplate.createIndex(entityClassRepository.getFirst());
        this.elasticsearchTemplate.putMapping(entityClassRepository.getFirst());
        ElasticsearchPersistentEntity<Object> indexEntity = this.elasticsearchTemplate
                .getPersistentEntityFor(entityClassRepository.getFirst());
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setIndexName(indexEntity.getIndexName());
        List<?> entities = entityClassRepository.getSecond().findAll();
        for (Object entity : entities) {
            indexQuery.setObject(entity);
            this.elasticsearchTemplate.index(indexQuery);
        }
        return entities.size();
    }
}
