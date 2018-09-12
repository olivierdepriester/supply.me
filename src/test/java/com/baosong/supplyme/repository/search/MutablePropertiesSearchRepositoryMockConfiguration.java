package com.baosong.supplyme.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of MutablePropertiesSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MutablePropertiesSearchRepositoryMockConfiguration {

    @MockBean
    private MutablePropertiesSearchRepository mockMutablePropertiesSearchRepository;

}
