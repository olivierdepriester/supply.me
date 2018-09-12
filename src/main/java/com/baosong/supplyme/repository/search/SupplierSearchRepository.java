package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.Supplier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Supplier entity.
 */
public interface SupplierSearchRepository extends ElasticsearchRepository<Supplier, Long> {
}
