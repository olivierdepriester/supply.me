package com.baosong.supplyme.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of InvoiceLineSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InvoiceLineSearchRepositoryMockConfiguration {

    @MockBean
    private InvoiceLineSearchRepository mockInvoiceLineSearchRepository;

}
