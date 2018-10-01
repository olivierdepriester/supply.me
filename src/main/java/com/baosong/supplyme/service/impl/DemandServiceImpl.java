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
import com.baosong.supplyme.domain.Demand_;
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
import com.baosong.supplyme.service.PurchaseOrderLineService;
import com.baosong.supplyme.service.UserService;
import com.google.common.collect.Sets;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static Map<DemandStatus, Set<DemandStatus>> demandWorkflowRules;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    static {
        demandWorkflowRules = new HashMap<>();
        demandWorkflowRules.put(DemandStatus.WAITING_FOR_APPROVAL, Sets.newHashSet(DemandStatus.NEW, DemandStatus.REJECTED));
        demandWorkflowRules.put(DemandStatus.APPROVED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
        demandWorkflowRules.put(DemandStatus.REJECTED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
        demandWorkflowRules.put(DemandStatus.ORDERED, Sets.newHashSet(DemandStatus.APPROVED));
        demandWorkflowRules.put(DemandStatus.PARTIALLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED));
        demandWorkflowRules.put(DemandStatus.FULLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED, DemandStatus.PARTIALLY_DELIVERED));
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
        	demand.setStatus(DemandStatus.NEW);
        	demand.setCreationDate(Instant.now());
        	demand.setCreationUser(userService.getCurrentUser().orElse(null));
        }
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
    	return StreamSupport
    			.stream(demandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
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
            .must(QueryBuilders.queryStringQuery(query.endsWith("*")? query.toLowerCase(): new StringBuilder(query.toLowerCase()).append("*").toString() ));
        return this.demandSearchRepository.search(booleanQueryBuilder, pageable);
    }

	@Override
	@Transactional(readOnly = true)
	public List<Demand> search(String query, Long materialId, Long projectId, DemandStatus demandStatus) {
        log.debug("Request to search Demands for query {}", query);
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(query)) {
            booleanQueryBuilder.must(QueryBuilders.queryStringQuery(query));
        }
        if (materialId != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("material.id", materialId));
        }
        if (projectId != null) {
            booleanQueryBuilder.must(QueryBuilders.termQuery("project.id", projectId));
        }
        if (demandStatus != null) {
            booleanQueryBuilder.must(QueryBuilders.matchQuery("status", demandStatus.toString()));
        }
        return StreamSupport
            .stream(demandSearchRepository.search(booleanQueryBuilder).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public double getQuantityOrderedFromPO(Long id) {
        return purchaseOrderLineService.getByDemandId(id).stream().mapToDouble(PurchaseOrderLine::getQuantity).sum();
    }

    /**
     * Set a demand to the given status.
     * The wanted <b>status</b> may be modified depending on the workflow rules
     *
     * @param id : Demand id
     * @param status : Status to set.
     *
     * @return : Updated demand
     * @exception : ServiceException in case of issue
     */
	@Override
	public Demand changeStatus(Long id, DemandStatus status) throws ServiceException {
        Demand demand = null;
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
		} else if (!demandWorkflowRules.get(status).contains(demand.getStatus()) ) {
            // The current status prevent the new status of being set
			throw new ServiceException(String.format("The status %s can not be set on demand %d (current is %s)", status, id, demand.getStatus()));
        }
        DemandStatus targetStatus = status;
        demand.setStatus(targetStatus);

		switch (status) {
        case WAITING_FOR_APPROVAL:
            // TODO Rules about demand estimated amount and recurrency
			if(canBeSetToApproved(demand)) {
				this.changeStatus(demand.getId(), DemandStatus.APPROVED);
				return demand;
			}
			break;
		case APPROVED:
			List<User> recipients = userService.getUsersFromAuthority(AuthoritiesConstants.PURCHASER);
			String to = recipients.stream().map(u -> u.getEmail()).collect(Collectors.joining(","));
			mailService.sendApprovedDemandToPurchaserEmail(demand, to);
			break;
		case REJECTED:
            mailService.sendRejectedDemandEmail(demand, userService.getCurrentUser().get());
			break;
		default:
			break;
		}
		this.save(demand);
		return demand;
    }

    private boolean canBeSetToApproved(Demand demand) {
        return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.APPROVAL_LVL2);
    }

	@Override
	public void rebuildIndex() {
		template.deleteIndex(Demand.class);
		List<Demand> demands = findAll();
		demands.stream().forEach(d -> demandSearchRepository.save(d));
	}
}
