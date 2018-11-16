package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.PurchaseOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseOrder entity.
 */
public interface PurchaseOrderSearchRepository extends ElasticsearchRepository<PurchaseOrder, Long> {
}
