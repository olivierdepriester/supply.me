package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.MaterialAvailability;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaterialAvailability entity.
 */
public interface MaterialAvailabilitySearchRepository extends ElasticsearchRepository<MaterialAvailability, Long> {
}
