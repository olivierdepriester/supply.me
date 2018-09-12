package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.MutableProperties;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MutableProperties entity.
 */
public interface MutablePropertiesSearchRepository extends ElasticsearchRepository<MutableProperties, Long> {
}
