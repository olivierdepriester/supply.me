package com.baosong.supplyme.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.search.DemandSearchRepository;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.UserService;
import com.google.common.collect.Sets;

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
    
    static {
    	 demandWorkflowRules = new HashMap<>();
    	 demandWorkflowRules.put(DemandStatus.WAITING_FOR_APPROVAL, Sets.newHashSet(DemandStatus.NEW, DemandStatus.REJECTED));
    	 demandWorkflowRules.put(DemandStatus.APPROVED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
    	 demandWorkflowRules.put(DemandStatus.REJECTED, Sets.newHashSet(DemandStatus.WAITING_FOR_APPROVAL));
    	 demandWorkflowRules.put(DemandStatus.ORDERED, Sets.newHashSet(DemandStatus.APPROVED));
    	 demandWorkflowRules.put(DemandStatus.PARTIALLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED));
    	 demandWorkflowRules.put(DemandStatus.FULLY_DELIVERED, Sets.newHashSet(DemandStatus.ORDERED, DemandStatus.PARTIALLY_DELIVERED));
    }
    @Autowired
    private UserService userService;

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

	@Override
	@Transactional(readOnly = true)
	public List<Demand> search(String query, Long materialId, Long projectId, DemandStatus demandStatus) {
        log.debug("Request to search Demands for query {}", query);
        return StreamSupport
    			.stream(demandSearchRepository.findByMaterialIdAndProjectIdAndStatus(materialId, projectId, demandStatus).spliterator(), false)
    			.collect(Collectors.toList());
//        return demandRepository.findAllById(
//        			
//            .map(Demand::getId).collect(Collectors.toList()));
	}

	@Override
	public Demand changeStatus(Long id, DemandStatus status) throws ServiceException {
		Demand demand = findOne(id).orElse(null);
		if (demand == null) {
			throw new ServiceException(String.format("No demand found for id %d", id));
		}
		if (!demandWorkflowRules.containsKey(status)) {
			throw new ServiceException(String.format("The status %s can never be directly set", status));
		} else if (!demandWorkflowRules.get(status).contains(demand.getStatus()) ) {
			throw new ServiceException(String.format("The status %s can not be set on demand %d (current is %s)", status, id, demand.getStatus()));
		}
		
		switch (status) {
		case WAITING_FOR_APPROVAL:
			break;
		case APPROVED:
			// TODO Send eMail to purchaser
			break;
		case REJECTED:
			// TODO Send eMail to demander
			break;
		default:
			break;
		}
		demand.setStatus(status);
		this.save(demand);
		return demand;
	}
	
	@Override
	public void rebuildIndex() {
		template.deleteIndex(Demand.class);
		List<Demand> demands = findAll();
		demands.stream().forEach(d -> demandSearchRepository.save(d));
	}
}
