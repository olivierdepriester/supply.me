package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
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
import com.baosong.supplyme.domain.Project;
import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.User;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.search.DemandSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.MailService;
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

    private final static Map<DemandStatus, Set<DemandStatus>> demandWorkflowRules;

    // @Autowired(required = false)
    // private ElasticsearchTemplate template;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    static {
        demandWorkflowRules = new HashMap<>();
        demandWorkflowRules.put(DemandStatus.WAITING_FOR_APPROVAL,
                Sets.newHashSet(DemandStatus.NEW, DemandStatus.REJECTED));
        demandWorkflowRules.put(DemandStatus.APPROVED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
        demandWorkflowRules.put(DemandStatus.REJECTED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
        demandWorkflowRules.put(DemandStatus.ORDERED, Sets.newHashSet(DemandStatus.APPROVED));
        demandWorkflowRules.put(DemandStatus.PARTIALLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED));
        demandWorkflowRules.put(DemandStatus.FULLY_DELIVERED,
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
            demand.status(DemandStatus.NEW)
                .quantityDelivered(0d)
                .quantityOrdered(0d)
                .creationDate(Instant.now())
                .code(mutablePropertiesService.getNewDemandCode())
                .setCreationUser(userService.getCurrentUser().get());
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
        if (criteria.getCreationUserId() != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("creationUser.id", criteria.getCreationUserId()));
        }
        if (criteria.getDemandStatus() != null) {
            BoolQueryBuilder statusQueryBuilder = QueryBuilders.boolQuery();
            criteria.getDemandStatus().forEach(status -> statusQueryBuilder.should(QueryBuilders.matchQuery("status", status.toString())));
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
        return purchaseOrderLineService.getByDemandId(id).stream().mapToDouble(PurchaseOrderLine::getQuantityDelivered).sum();
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
            throw new ServiceException(String.format("No demand found for id %d", id));
        }
        if (status.equals(demand.getStatus())) {
            // If the current status is the same as the new status, nothing to be done
            return demand;
        }

        if (!demandWorkflowRules.containsKey(status)) {
            // The wanted status can never be set
            throw new ServiceException(String.format("The status %s can never be directly set", status));
        } else if (!demandWorkflowRules.get(status).contains(demand.getStatus())) {
            // The current status prevent the new status of being set
            throw new ServiceException(String.format("The status %s can not be set on demand %d (current is %s)",
                    status, id, demand.getStatus()));
        }
        DemandStatus targetStatus = status;
        DemandStatusChange demandStatusChange = new DemandStatusChange(demand, targetStatus, currentUser);
        demandStatusChange.setComment(comment);
        demand.getDemandStatusChanges().add(demandStatusChange);

        switch (status) {
        case WAITING_FOR_APPROVAL:
            if (!isDemandEditable(demand)) {
                // Can not be edited -> Error
                throw new ServiceException(
                        String.format("The current user can not edit nor send it to approval the demand %d", id));
            }
            demand.setStatus(targetStatus);
            if (canBeSetToApproved(demand)) {
                // Automatic approval if possible
                this.changeStatus(demand.getId(), DemandStatus.APPROVED, "Auto");
                return demand;
            }
            break;
        case APPROVED:
            if (!canBeSetToApproved(demand)) {
                // Can not be approved -> Error
                throw new ServiceException(String.format("The current user can not set the demand %d to approved", id));
            }
            demand.setStatus(targetStatus);
            // Send a mail to the purchasers
            List<User> recipients = userService.getUsersFromAuthority(AuthoritiesConstants.PURCHASER);
            String to = recipients.stream().map(u -> u.getEmail()).collect(Collectors.joining(","));
            mailService.sendApprovedDemandToPurchaserEmail(demand, to);
            break;
        case REJECTED:
            demand.setStatus(targetStatus);
            // Send a mail to the demand owner
            mailService.sendRejectedDemandEmail(demand, currentUser);
            break;
        default:
            demand.setStatus(targetStatus);
            break;
        }
        return this.save(demand);
    }

    /**
     * Check if a demand can be set to the APPROVED status
     *
     * @param demand the demand to check
     * @return true if the demand can be approved
     */
    private boolean canBeSetToApproved(Demand demand) {
        // TODO Rules about demand estimated amount and recurrency
        return (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.VALIDATION_LVL1)
                || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.VALIDATION_LVL2))
                && !demand.getMaterial().isTemporary().booleanValue();
    }

    @Override
    public void rebuildIndex() {
        // template.deleteIndex(Demand.class);
        // List<Demand> demands = findAll();
        // demands.stream().forEach(d -> demandSearchRepository.save(d));
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
