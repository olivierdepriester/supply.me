package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.DeliveryNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DeliveryNote entity.
 */
public interface DeliveryNoteSearchRepository extends ElasticsearchRepository<DeliveryNote, Long> {
}
