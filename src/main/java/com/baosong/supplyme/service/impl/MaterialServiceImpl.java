package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.Optional;

import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.domain.MaterialAvailability;
import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.MaterialRepository;
import com.baosong.supplyme.repository.search.MaterialSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.MaterialService;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private UserService userService;

    @Autowired
    private DemandService demandService;

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    public MaterialServiceImpl(MaterialRepository materialRepository,
            MaterialSearchRepository materialSearchRepository) {
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
        Material persistedMaterial = null;
        if (material.getId() == null) {
            persistedMaterial = new Material().partNumber(mutablePropertiesService.getNewPartNumber())
                    .creationDate(Instant.now()).creationUser(userService.getCurrentUser().get())
                    .temporary(material.isTemporary());
        } else {
            // Get the persisted version of the material
            persistedMaterial = findOne(material.getId()).get();
        }
        if (!isMaterialEditable(persistedMaterial)) {
            // Check the persisted version in case of the
            throw new ServiceException(String.format("Material %d is not editable", persistedMaterial.getId()),
                    "material.edit.forbidden");
        }
        persistedMaterial = persistedMaterial.name(material.getName()).description(material.getDescription())
                .estimatedPrice(material.getEstimatedPrice()).materialCategory(material.getMaterialCategory());
        if (!material.isTemporary().booleanValue()
                && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MATERIAL_MANAGER)
                && persistedMaterial.isTemporary().booleanValue()) {
            // If the material switch from temporary to definitive or if it is a new
            // definitive material
            // we generate a new part number for it
            persistedMaterial.setTemporary(Boolean.FALSE);
        }
        return this.saveAndCascadeIndex(persistedMaterial);
    }

    private boolean isMaterialEditable(Material material) {
        return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MATERIAL_MANAGER)
                || (material.isTemporary() && (material.getId() == null
                        || material.getCreationUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().get())));
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
     * @throws ServiceException
     */
    @Override
    public void delete(Long id) throws ServiceException {
        log.debug("Request to delete Material : {}", id);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MATERIAL_MANAGER)) {
            throw new ServiceException(String.format("The current user cannot delete Material %d", id),
                    "material.delete.forbidden");
        }
        materialRepository.deleteById(id);
        materialSearchRepository.deleteById(id);
    }

    /**
     * Search for the material corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Material> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Materials for query {}", query);
        return materialSearchRepository.search(
                queryStringQuery(query.endsWith("*") ? query : new StringBuilder(query).append("*").toString()),
                pageable);
    }

    @Override
    public Material saveAndCascadeIndex(Material material) {
        boolean isNew = material.getId() == null;
        Material result = materialRepository.save(material);
        materialSearchRepository.save(result);
        if (!isNew) {
            this.demandService.findByMaterialId(material.getId())
                    .forEach(demand -> this.demandService.saveAndCascadeIndex(demand));
        }
        return result;
    }

    @Override
    public Material updateEstimatedPrice(final Material material, final Supplier supplier, final Double newPrice) {
        if (material != null) {
            // Update the default material price with the new price
            material.setEstimatedPrice(newPrice);
            // Store the price for this supplier and this material
            // Get an existing availability for the demand supplier
            MaterialAvailability availability = material.getAvailabilities().stream()
                    .filter(a -> a.getSupplier().equals(supplier)).findAny().orElse(null);
            if (availability == null) {
                // No existing : create a new one and add it to the material
                availability = new MaterialAvailability().supplier(supplier).creationDate(Instant.now());
                material.addAvailability(availability);
            }
            // Update price and last update date
            availability.purchasePrice(newPrice).setUpdateDate(Instant.now());
            // Save the material indices
            this.saveAndCascadeIndex(material);
        }
        return material;
    }
}
