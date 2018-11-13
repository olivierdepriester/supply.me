package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.Optional;

import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.SupplierRepository;
import com.baosong.supplyme.repository.search.SupplierSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.service.SupplierService;
import com.baosong.supplyme.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Supplier.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    private final SupplierSearchRepository supplierSearchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierSearchRepository supplierSearchRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierSearchRepository = supplierSearchRepository;
    }

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save
     * @return the persisted entity
     */
    @Override
    public Supplier save(Supplier supplier) throws ServiceException {
        log.debug("Request to save Supplier : {}", supplier);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPPLIER_MANAGER)) {
            throw new ServiceException(
                String.format("The current user cannot save Supplier %s", supplier.getReferenceNumber()),
                "supplier.edit.forbidden");
        }
        if (supplier.getId() == null) {
            supplier.creationDate(Instant.now()).setCreationUser(userService.getCurrentUser().get());
            if (supplier.isTemporary()) {
                supplier.setReferenceNumber(this.mutablePropertiesService.getNewTemporarySupplierCode());
            }
        }
        Supplier sameReferenceNumberSupplier = this.supplierRepository.getByReferenceNumber(supplier.getReferenceNumber());
        if (sameReferenceNumberSupplier != null && !sameReferenceNumberSupplier.getId().equals(supplier.getId())) {
            throw new ServiceException(
                String.format("Ref %s is already used by Supplier %d.", supplier.getReferenceNumber(), supplier.getId()),
                "supplier.edit.alreadyExists"
            );
        }

        Supplier result = supplierRepository.save(supplier);
        supplierSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll(pageable);
    }


    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity
     * @throws ServiceException
     */
    @Override
    public void delete(Long id) throws ServiceException {
        log.debug("Request to delete Supplier : {}", id);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPPLIER_MANAGER)) {
            throw new ServiceException(
                String.format("The current user cannot delete Supplier %d", id),
                "supplier.delete.forbidden");
        }
        supplierRepository.deleteById(id);
        supplierSearchRepository.deleteById(id);
    }

    /**
     * Search for the supplier corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Suppliers for query {}", query);
        return supplierSearchRepository.search(queryStringQuery(
            query.endsWith("*") ? query.toLowerCase(): new StringBuilder(query.toLowerCase()).append('*').toString()
            ), pageable);
    }
}
