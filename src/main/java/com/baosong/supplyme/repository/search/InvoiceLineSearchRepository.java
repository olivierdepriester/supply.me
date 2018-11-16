package com.baosong.supplyme.repository.search;

import com.baosong.supplyme.domain.InvoiceLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InvoiceLine entity.
 */
public interface InvoiceLineSearchRepository extends ElasticsearchRepository<InvoiceLine, Long> {
}
