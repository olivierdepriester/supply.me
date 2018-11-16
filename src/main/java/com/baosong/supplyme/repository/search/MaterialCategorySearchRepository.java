package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.MaterialCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaterialCategory entity.
 */
public interface MaterialCategorySearchRepository extends ElasticsearchRepository<MaterialCategory, Long> {
}
