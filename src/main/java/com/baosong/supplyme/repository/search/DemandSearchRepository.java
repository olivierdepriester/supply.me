package com.baosong.supplyme.repository.search;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.enumeration.DemandStatus;

/**
 * Spring Data Elasticsearch repository for the Demand entity.
 */
public interface DemandSearchRepository extends ElasticsearchRepository<Demand, Long> {
	List<Demand> findByMaterialIdAndProjectIdAndStatus(Long materialId, Long projectId, DemandStatus demandStatus);
}
