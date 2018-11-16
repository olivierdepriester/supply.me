package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.repository.MutablePropertiesRepository;
import com.baosong.supplyme.repository.search.MutablePropertiesSearchRepository;
import com.baosong.supplyme.service.MutablePropertiesService;
import com.baosong.supplyme.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.baosong.supplyme.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baosong.supplyme.domain.enumeration.PropertiesKey;
/**
 * Test class for the MutablePropertiesResource REST controller.
 *
 * @see MutablePropertiesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class MutablePropertiesResourceIntTest {

    private static final PropertiesKey DEFAULT_KEY = PropertiesKey.THRESHOLD_SECOND_LEVEL_APPROVAL;
    private static final PropertiesKey UPDATED_KEY = PropertiesKey.THRESHOLD_SECOND_LEVEL_APPROVAL;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TYPE = "BBBBBBBBBB";

    @Autowired
    private MutablePropertiesRepository mutablePropertiesRepository;

    

    @Autowired
    private MutablePropertiesService mutablePropertiesService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.MutablePropertiesSearchRepositoryMockConfiguration
     */
    @Autowired
    private MutablePropertiesSearchRepository mockMutablePropertiesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMutablePropertiesMockMvc;

    private MutableProperties mutableProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MutablePropertiesResource mutablePropertiesResource = new MutablePropertiesResource(mutablePropertiesService);
        this.restMutablePropertiesMockMvc = MockMvcBuilders.standaloneSetup(mutablePropertiesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MutableProperties createEntity(EntityManager em) {
        MutableProperties mutableProperties = new MutableProperties()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE);
        return mutableProperties;
    }

    @Before
    public void initTest() {
        mutableProperties = createEntity(em);
    }

    @Test
    @Transactional
    public void createMutableProperties() throws Exception {
        int databaseSizeBeforeCreate = mutablePropertiesRepository.findAll().size();

        // Create the MutableProperties
        restMutablePropertiesMockMvc.perform(post("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isCreated());

        // Validate the MutableProperties in the database
        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeCreate + 1);
        MutableProperties testMutableProperties = mutablePropertiesList.get(mutablePropertiesList.size() - 1);
        assertThat(testMutableProperties.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testMutableProperties.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMutableProperties.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);

        // Validate the MutableProperties in Elasticsearch
        verify(mockMutablePropertiesSearchRepository, times(1)).save(testMutableProperties);
    }

    @Test
    @Transactional
    public void createMutablePropertiesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mutablePropertiesRepository.findAll().size();

        // Create the MutableProperties with an existing ID
        mutableProperties.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMutablePropertiesMockMvc.perform(post("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isBadRequest());

        // Validate the MutableProperties in the database
        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeCreate);

        // Validate the MutableProperties in Elasticsearch
        verify(mockMutablePropertiesSearchRepository, times(0)).save(mutableProperties);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = mutablePropertiesRepository.findAll().size();
        // set the field null
        mutableProperties.setKey(null);

        // Create the MutableProperties, which fails.

        restMutablePropertiesMockMvc.perform(post("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isBadRequest());

        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = mutablePropertiesRepository.findAll().size();
        // set the field null
        mutableProperties.setValue(null);

        // Create the MutableProperties, which fails.

        restMutablePropertiesMockMvc.perform(post("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isBadRequest());

        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mutablePropertiesRepository.findAll().size();
        // set the field null
        mutableProperties.setValueType(null);

        // Create the MutableProperties, which fails.

        restMutablePropertiesMockMvc.perform(post("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isBadRequest());

        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMutableProperties() throws Exception {
        // Initialize the database
        mutablePropertiesRepository.saveAndFlush(mutableProperties);

        // Get all the mutablePropertiesList
        restMutablePropertiesMockMvc.perform(get("/api/mutable-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mutableProperties.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getMutableProperties() throws Exception {
        // Initialize the database
        mutablePropertiesRepository.saveAndFlush(mutableProperties);

        // Get the mutableProperties
        restMutablePropertiesMockMvc.perform(get("/api/mutable-properties/{id}", mutableProperties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mutableProperties.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMutableProperties() throws Exception {
        // Get the mutableProperties
        restMutablePropertiesMockMvc.perform(get("/api/mutable-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMutableProperties() throws Exception {
        // Initialize the database
        mutablePropertiesService.save(mutableProperties);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMutablePropertiesSearchRepository);

        int databaseSizeBeforeUpdate = mutablePropertiesRepository.findAll().size();

        // Update the mutableProperties
        MutableProperties updatedMutableProperties = mutablePropertiesRepository.findById(mutableProperties.getId()).get();
        // Disconnect from session so that the updates on updatedMutableProperties are not directly saved in db
        em.detach(updatedMutableProperties);
        updatedMutableProperties
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);

        restMutablePropertiesMockMvc.perform(put("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMutableProperties)))
            .andExpect(status().isOk());

        // Validate the MutableProperties in the database
        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeUpdate);
        MutableProperties testMutableProperties = mutablePropertiesList.get(mutablePropertiesList.size() - 1);
        assertThat(testMutableProperties.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testMutableProperties.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMutableProperties.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);

        // Validate the MutableProperties in Elasticsearch
        verify(mockMutablePropertiesSearchRepository, times(1)).save(testMutableProperties);
    }

    @Test
    @Transactional
    public void updateNonExistingMutableProperties() throws Exception {
        int databaseSizeBeforeUpdate = mutablePropertiesRepository.findAll().size();

        // Create the MutableProperties

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restMutablePropertiesMockMvc.perform(put("/api/mutable-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mutableProperties)))
            .andExpect(status().isBadRequest());

        // Validate the MutableProperties in the database
        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MutableProperties in Elasticsearch
        verify(mockMutablePropertiesSearchRepository, times(0)).save(mutableProperties);
    }

    @Test
    @Transactional
    public void deleteMutableProperties() throws Exception {
        // Initialize the database
        mutablePropertiesService.save(mutableProperties);

        int databaseSizeBeforeDelete = mutablePropertiesRepository.findAll().size();

        // Get the mutableProperties
        restMutablePropertiesMockMvc.perform(delete("/api/mutable-properties/{id}", mutableProperties.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MutableProperties> mutablePropertiesList = mutablePropertiesRepository.findAll();
        assertThat(mutablePropertiesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MutableProperties in Elasticsearch
        verify(mockMutablePropertiesSearchRepository, times(1)).deleteById(mutableProperties.getId());
    }

    @Test
    @Transactional
    public void searchMutableProperties() throws Exception {
        // Initialize the database
        mutablePropertiesService.save(mutableProperties);
        when(mockMutablePropertiesSearchRepository.search(queryStringQuery("id:" + mutableProperties.getId())))
            .thenReturn(Collections.singletonList(mutableProperties));
        // Search the mutableProperties
        restMutablePropertiesMockMvc.perform(get("/api/_search/mutable-properties?query=id:" + mutableProperties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mutableProperties.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MutableProperties.class);
        MutableProperties mutableProperties1 = new MutableProperties();
        mutableProperties1.setId(1L);
        MutableProperties mutableProperties2 = new MutableProperties();
        mutableProperties2.setId(mutableProperties1.getId());
        assertThat(mutableProperties1).isEqualTo(mutableProperties2);
        mutableProperties2.setId(2L);
        assertThat(mutableProperties1).isNotEqualTo(mutableProperties2);
        mutableProperties1.setId(null);
        assertThat(mutableProperties1).isNotEqualTo(mutableProperties2);
    }
}
