package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.Material;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Material entity.
 */
public interface MaterialSearchRepository extends ElasticsearchRepository<Material, Long> {
}
