package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseOrderLine entity.
 */
public interface PurchaseOrderLineSearchRepository extends ElasticsearchRepository<PurchaseOrderLine, Long> {
}
