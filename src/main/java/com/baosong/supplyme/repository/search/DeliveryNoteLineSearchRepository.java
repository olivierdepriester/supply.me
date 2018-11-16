package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.DeliveryNoteLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DeliveryNoteLine entity.
 */
public interface DeliveryNoteLineSearchRepository extends ElasticsearchRepository<DeliveryNoteLine, Long> {
}
