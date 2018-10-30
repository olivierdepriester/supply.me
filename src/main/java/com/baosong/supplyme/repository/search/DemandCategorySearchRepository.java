package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.DemandCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DemandCategory entity.
 */
public interface DemandCategorySearchRepository extends ElasticsearchRepository<DemandCategory, Long> {
}
