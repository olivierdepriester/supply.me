package com.baosong.supplyme.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of MaterialAvailabilitySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MaterialAvailabilitySearchRepositoryMockConfiguration {

    @MockBean
    private MaterialAvailabilitySearchRepository mockMaterialAvailabilitySearchRepository;

}
