package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.Demand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Demand entity.
 */
public interface DemandSearchRepository extends ElasticsearchRepository<Demand, Long> {
}
