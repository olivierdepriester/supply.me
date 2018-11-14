package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.DemandStatusChange;
import com.baosong.supplyme.domain.Material;
import com.baosong.supplyme.domain.MaterialAvailability;
import com.baosong.supplyme.domain.Project;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.User;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.errors.MessageParameterBean;
import com.baosong.supplyme.domain.errors.ParameterizedServiceException;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.search.DemandSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.MailService;
import com.baosong.supplyme.service.MaterialService;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.service.UserService;
import com.baosong.supplyme.service.util.DemandSearchCriteria;
import com.google.common.collect.Sets;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing Demand.
 */
@Service
@Transactional
public class DemandServiceImpl implements DemandService {

    private final Logger log = LoggerFactory.getLogger(DemandServiceImpl.class);

    private final DemandRepository demandRepository;

    private final DemandSearchRepository demandSearchRepository;

    private final static Map<DemandStatus, Set<DemandStatus>> DEMAND_WORKFLOW_RULES;

    @Autowired(required = false)
    private ElasticsearchTemplate template;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    @Autowired
    private MaterialService materialService;

    static {
        DEMAND_WORKFLOW_RULES = new HashMap<>();
        DEMAND_WORKFLOW_RULES.put(DemandStatus.WAITING_FOR_APPROVAL,
                Sets.newHashSet(DemandStatus.NEW, DemandStatus.REJECTED));
        DEMAND_WORKFLOW_RULES.put(DemandStatus.APPROVED,
                Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL, DemandStatus.APPROVED));
        DEMAND_WORKFLOW_RULES.put(DemandStatus.REJECTED,
                Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL, DemandStatus.APPROVED));
        DEMAND_WORKFLOW_RULES.put(DemandStatus.ORDERED, Sets.newHashSet(DemandStatus.APPROVED));
        DEMAND_WORKFLOW_RULES.put(DemandStatus.PARTIALLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED));
        DEMAND_WORKFLOW_RULES.put(DemandStatus.FULLY_DELIVERED,
                Sets.newHashSet(DemandStatus.ORDERED, DemandStatus.PARTIALLY_DELIVERED));
    }

    public DemandServiceImpl(DemandRepository demandRepository, DemandSearchRepository demandSearchRepository) {
        this.demandRepository = demandRepository;
        this.demandSearchRepository = demandSearchRepository;
    }

    /**
     * Save a demand.
     *
     * @param demand the entity to save
     * @return the persisted entity
     * @throws ServiceException
     */
    @Override
    public Demand save(Demand demand) throws ServiceException {
        log.debug("Request to save Demand : {}", demand);
        if (demand.getId() == null) {
            // New demand
            demand.status(DemandStatus.NEW).quantityDelivered(0d).quantityOrdered(0d).creationDate(Instant.now())
                    .code(mutablePropertiesService.getNewDemandCode())
                    .setCreationUser(userService.getCurrentUser().get());
            List<DemandStatusChange> dsc = new ArrayList<>(1);
            dsc.add(new DemandStatusChange(demand, demand.getStatus(), demand.getCreationUser()));
            demand.setDemandStatusChanges(dsc);
        }
        return this.saveAndCascadeIndex(demand);
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
     * Get one demand by id with its status changes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Demand> findOneWithStatusChanges(Long id) {
        log.debug("Request to get Demand with status changes : {}", id);
        Optional<Demand> demand = demandRepository.findById(id);
        demand.ifPresent(d -> d.getDemandStatusChanges().size());
        return demand;
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
        return StreamSupport.stream(demandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Search demands allowed to be added to a purchase order
     *
     * @param query search term
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Demand> searchDemandsToPurchase(String query, Pageable pageable) {
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("canBePurchased", true))
                .must(QueryBuilders.queryStringQuery(query.endsWith("*") ? query.toLowerCase()
                        : new StringBuilder(query.toLowerCase()).append("*").toString()));
        return this.demandSearchRepository.search(booleanQueryBuilder, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demand> search(DemandSearchCriteria criteria, Pageable pageable) {
        log.debug("Request to search Demands for query {}", criteria);
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(criteria.getQuery())) {
            booleanQueryBuilder.must(QueryBuilders.queryStringQuery(criteria.getQuery()));
        }
        if (criteria.getMaterialId() != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("material.id", criteria.getMaterialId()));
        }
        if (criteria.getProjectId() != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("project.id", criteria.getProjectId()));
        }
        if (criteria.getDepartmentId() != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("department.id", criteria.getProjectId()));
        }
        if (criteria.getCreationUserId() != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("creationUser.id", criteria.getCreationUserId()));
        }
        if (criteria.getDemandStatus() != null) {
            BoolQueryBuilder statusQueryBuilder = QueryBuilders.boolQuery();
            criteria.getDemandStatus().forEach(
                    status -> statusQueryBuilder.should(QueryBuilders.matchQuery("status", status.toString())));
            booleanQueryBuilder.must(statusQueryBuilder);
        }
        return StreamSupport.stream(demandSearchRepository.search(booleanQueryBuilder, pageable).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public double getQuantityOrderedFromPO(Long id) {
        return purchaseOrderLineService.getByDemandId(id).stream().mapToDouble(PurchaseOrderLine::getQuantity).sum();
    }

    @Override
    @Transactional(readOnly = true)
    public double getQuantityDeliveredFromPO(Long id) {
        return purchaseOrderLineService.getByDemandId(id).stream().mapToDouble(PurchaseOrderLine::getQuantityDelivered)
                .sum();
    }

    /**
     * Set a demand to the given status. The wanted <b>status</b> may be modified
     * depending on the workflow rules
     *
     * @param id     : Demand id
     * @param status : Status to set.
     *
     * @return : Updated demand
     * @exception : ServiceException in case of issue
     */
    @Override
    public Demand changeStatus(Long id, DemandStatus status, String comment) throws ServiceException {
        Demand demand = null;
        User currentUser = userService.getCurrentUser().get();
        try {
            demand = findOne(id).get();
        } catch (NoSuchElementException e) {
            // Demand not found
            throw new ServiceException(
                String.format("No demand found for id %d", id),
                "notFound");
        }
        if (status.equals(demand.getStatus())) {
            // If the current status is the same as the new status, nothing to be done
            return demand;
        }

        if (!DEMAND_WORKFLOW_RULES.containsKey(status)) {
            // The wanted status can never be set
            throw new ServiceException(String.format("The status %s can never be directly set", status),
                "demand.status.forbidden");
        } else if (!DEMAND_WORKFLOW_RULES.get(status).contains(demand.getStatus())) {
            // The current status prevent the new status of being set
            throw new ServiceException(String.format("The status %s can not be set on demand %d (current is %s)",
                    status, id, demand.getStatus()),
                    "demand.status.forbidden");
        }
        DemandStatus targetStatus = status;
        DemandStatusChange demandStatusChange = new DemandStatusChange(demand, targetStatus, currentUser);
        demandStatusChange.setComment(comment);
        switch (status) {
        case WAITING_FOR_APPROVAL:
            // Check if all the required fields are filled - Exception thrown if missing
            // field
            if (this.checkRequiredFields(demand)) {
                if (!this.isDemandEditable(demand)) {
                    // Can not be edited -> Error
                    throw new ServiceException(
                            String.format("The current user can not edit nor send it to approval the demand %d", id),
                        "demand.edit.forbidden");
                }
                demand.setStatus(targetStatus);
                demand.setValidationAuthority(this.getValidationAuthorityToSet(demand));
                // Try to appove Automatically if possible
                demand = this.changeStatus(demand.getId(), DemandStatus.APPROVED, "Auto");
            }
            break;
        case APPROVED:
            String currentUserHighestAuthority = SecurityUtils.getCurrentUserHighestAuthority();
            boolean hasReachedAuthorityChanged = false;

            // If current user is LVL1 and head of department or has higher level : change
            // the reached validation level
            if (!StringUtils.isEmpty(currentUserHighestAuthority)
                    && (!AuthoritiesConstants.VALIDATION_LVL1.equals(currentUserHighestAuthority)
                            || (AuthoritiesConstants.VALIDATION_LVL1.equals(currentUserHighestAuthority)
                                    && demand.getDepartment().getHeadUser() != null
                                    && demand.getDepartment().getHeadUser().equals(currentUser)))) {
                // Compare the "demand already reached authority" and "user max authority"
                if (SecurityUtils.compare(currentUserHighestAuthority, demand.getReachedAuthority()) > 0) {
                    // Current user authority is greater than demand latest authority -> Update (=
                    // partial validation)
                    hasReachedAuthorityChanged = true;
                    demand.setReachedAuthority(currentUserHighestAuthority);
                    // P
                    demandStatusChange.setComment(currentUserHighestAuthority);
                }
            }
            if (canBeSetToApproved(demand)) {
                // Demand can be approved --> Change status to APPROVED
                demand.setStatus(targetStatus);

                // Update the material data with this demand data
                this.updateMaterialPrice(demand);

                // Send a mail to the purchasers
                List<User> recipients = userService.getUsersFromAuthority(AuthoritiesConstants.PURCHASER);
                String to = recipients.stream().map(u -> u.getEmail()).collect(Collectors.joining(","));
                mailService.sendApprovedDemandToPurchaserEmail(demand, to);
            } else {
                if (!hasReachedAuthorityChanged) {
                    // No change --> We do not save nor add any change
                    return demand;
                }
            }
            break;
        case REJECTED:
            demand.reachedAuthority(null).setStatus(targetStatus);
            // Send a mail to the demand owner
            mailService.sendRejectedDemandEmail(demand, currentUser);
            break;
        default:
            demand.setStatus(targetStatus);
            break;
        }
        demand.getDemandStatusChanges().add(demandStatusChange);
        return this.saveAndCascadeIndex(demand);
    }

    /**
     * When approved, update the demand material fields with the data of the demand.
     *
     * @param demand The approved demand.
     */
    private void updateMaterialPrice(Demand demand) {
        // If material was a temporary one, make it a normal
        if (demand.getMaterial().isTemporary()) {
            demand.getMaterial().setTemporary(false);
        }
        // Update the default material price with the approved demand
        demand.getMaterial().setEstimatedPrice(demand.getEstimatedPrice());
        // Store the price for this supplier and this material
        final Supplier supplier = demand.getSupplier();
        // Get an existing availability for the demand supplier
        MaterialAvailability availability = demand.getMaterial().getAvailabilities().stream()
                .filter(a -> a.getSupplier().equals(supplier)).findAny().orElse(null);
        if (availability == null) {
            // No existing : create a new one and add it to the material
            availability = new MaterialAvailability().supplier(supplier).creationDate(Instant.now());
            demand.getMaterial().addAvailability(availability);
        }
        // Update price and last update date
        availability.purchasePrice(demand.getEstimatedPrice()).setUpdateDate(Instant.now());
        // Save the material indices
        this.materialService.saveAndCascadeIndex(demand.getMaterial());
    }

    /**
     * Calculate the validation authority level required to validate the demand.
     *
     * @param demand the demand.
     * @return the calculated authority level.
     * @throws ServiceException
     */
    private String getValidationAuthorityToSet(Demand demand) throws ServiceException {
        String validationAuthority = null;
        Double demandAmount = demand.getEstimatedPrice() * demand.getQuantity();
        if (demandAmount.compareTo(mutablePropertiesService.getSecondValidationThresholdAmount().get()) >= 0) {
            validationAuthority = AuthoritiesConstants.VALIDATION_LVL5;
        } else {
            validationAuthority = AuthoritiesConstants.VALIDATION_LVL1;
        }
        return validationAuthority;
    }

    /**
     * Check if the required fields of a demand to be sent are filled.
     *
     * @param demand the demand.
     * @return false if at least 1 field is missing
     * @throws ParameterizedServiceException If it at least 1 fields is missing
     */
    private boolean checkRequiredFields(Demand demand) throws ParameterizedServiceException {
        List<MessageParameterBean> missingFields = new ArrayList<>();
        if (demand.getEstimatedPrice() == null) {
            missingFields.add(MessageParameterBean.of("NotNull", "demand", "estimatedPrice"));
        }
        if (demand.getSupplier() == null) {
            missingFields.add(MessageParameterBean.of("NotNull", "supplier", "detail.title"));
        }
        if (!missingFields.isEmpty()) {
            throw new ParameterizedServiceException(String.format("Missing fields on demand %s", demand.getId()),
                    missingFields);
        }
        return missingFields.isEmpty();
    }

    /**
     * Check if a demand can be set to the APPROVED status
     *
     * @param demand the demand to check
     * @return true if the demand can be approved
     */
    private boolean canBeSetToApproved(Demand demand) {
        if (demand.getValidationAuthority() == null) {
            return true;
        }
        // If the required validation authority has been at least reached --> OK
        return SecurityUtils.compare(demand.getValidationAuthority(), demand.getReachedAuthority()) <= 0;
    }

    @Override
    public void rebuildIndex() {
        template.deleteIndex(Demand.class);
        List<Demand> demands = findAll();
        demands.stream().forEach(d -> demandSearchRepository.save(d));
    }

    /**
     * Check if a demand is editable
     *
     * @param demand the demand to check
     * @return true if the demand can be edited
     */
    private boolean isDemandEditable(Demand demand) {
        User currentUser = this.userService.getUserWithAuthorities().get();
        return (demand.getCreationUser().getId().equals(currentUser.getId())
                || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
                && (DemandStatus.NEW.equals(demand.getStatus()) || DemandStatus.REJECTED.equals(demand.getStatus()));
    }

    @Override
    public Demand saveAndCascadeIndex(Demand demand) {
        boolean isNew = demand.getId() == null;
        Demand result = demandRepository.save(demand);
        demandSearchRepository.save(result);
        if (!isNew) {
            List<PurchaseOrderLine> lines = this.purchaseOrderLineService.getByDemandId(demand.getId());
            lines.forEach(pol -> this.purchaseOrderLineService.saveAndCascaseIndex(pol));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demand> findByMaterialId(Long materialId) {
        return this.demandRepository.findAll(Example.of(new Demand().material(new Material().id(materialId))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demand> findByProjectId(Long projectId) {
        return this.demandRepository.findAll(Example.of(new Demand().project(new Project().id(projectId))));
    }

}
