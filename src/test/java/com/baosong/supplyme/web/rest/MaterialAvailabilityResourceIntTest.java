package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.MaterialAvailability;
import com.baosong.supplyme.repository.MaterialAvailabilityRepository;
import com.baosong.supplyme.repository.search.MaterialAvailabilitySearchRepository;
import com.baosong.supplyme.service.MaterialAvailabilityService;
import com.baosong.supplyme.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.baosong.supplyme.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MaterialAvailabilityResource REST controller.
 *
 * @see MaterialAvailabilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class MaterialAvailabilityResourceIntTest {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PURCHASE_PRICE = 0D;
    private static final Double UPDATED_PURCHASE_PRICE = 1D;

    @Autowired
    private MaterialAvailabilityRepository materialAvailabilityRepository;



    @Autowired
    private MaterialAvailabilityService materialAvailabilityService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.MaterialAvailabilitySearchRepositoryMockConfiguration
     */
    @Autowired
    private MaterialAvailabilitySearchRepository mockMaterialAvailabilitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaterialAvailabilityMockMvc;

    private MaterialAvailability materialAvailability;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialAvailabilityResource materialAvailabilityResource = new MaterialAvailabilityResource(materialAvailabilityService);
        this.restMaterialAvailabilityMockMvc = MockMvcBuilders.standaloneSetup(materialAvailabilityResource)
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
    public static MaterialAvailability createEntity(EntityManager em) {
        MaterialAvailability materialAvailability = new MaterialAvailability()
            .creationDate(DEFAULT_START_DATE)
            .updateDate(DEFAULT_END_DATE)
            .purchasePrice(DEFAULT_PURCHASE_PRICE);
        return materialAvailability;
    }

    @Before
    public void initTest() {
        materialAvailability = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialAvailability() throws Exception {
        int databaseSizeBeforeCreate = materialAvailabilityRepository.findAll().size();

        // Create the MaterialAvailability
        restMaterialAvailabilityMockMvc.perform(post("/api/material-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialAvailability)))
            .andExpect(status().isCreated());

        // Validate the MaterialAvailability in the database
        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialAvailability testMaterialAvailability = materialAvailabilityList.get(materialAvailabilityList.size() - 1);
        assertThat(testMaterialAvailability.getCreationDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMaterialAvailability.getUpdateDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMaterialAvailability.getPurchasePrice()).isEqualTo(DEFAULT_PURCHASE_PRICE);

        // Validate the MaterialAvailability in Elasticsearch
        verify(mockMaterialAvailabilitySearchRepository, times(1)).save(testMaterialAvailability);
    }

    @Test
    @Transactional
    public void createMaterialAvailabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialAvailabilityRepository.findAll().size();

        // Create the MaterialAvailability with an existing ID
        materialAvailability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialAvailabilityMockMvc.perform(post("/api/material-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialAvailability in the database
        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeCreate);

        // Validate the MaterialAvailability in Elasticsearch
        verify(mockMaterialAvailabilitySearchRepository, times(0)).save(materialAvailability);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialAvailabilityRepository.findAll().size();
        // set the field null
        materialAvailability.setCreationDate(null);

        // Create the MaterialAvailability, which fails.

        restMaterialAvailabilityMockMvc.perform(post("/api/material-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialAvailability)))
            .andExpect(status().isBadRequest());

        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterialAvailabilities() throws Exception {
        // Initialize the database
        materialAvailabilityRepository.saveAndFlush(materialAvailability);

        // Get all the materialAvailabilityList
        restMaterialAvailabilityMockMvc.perform(get("/api/material-availabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialAvailability.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())));
    }


    @Test
    @Transactional
    public void getMaterialAvailability() throws Exception {
        // Initialize the database
        materialAvailabilityRepository.saveAndFlush(materialAvailability);

        // Get the materialAvailability
        restMaterialAvailabilityMockMvc.perform(get("/api/material-availabilities/{id}", materialAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialAvailability.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.purchasePrice").value(DEFAULT_PURCHASE_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMaterialAvailability() throws Exception {
        // Get the materialAvailability
        restMaterialAvailabilityMockMvc.perform(get("/api/material-availabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialAvailability() throws Exception {
        // Initialize the database
        materialAvailabilityService.save(materialAvailability);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMaterialAvailabilitySearchRepository);

        int databaseSizeBeforeUpdate = materialAvailabilityRepository.findAll().size();

        // Update the materialAvailability
        MaterialAvailability updatedMaterialAvailability = materialAvailabilityRepository.findById(materialAvailability.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialAvailability are not directly saved in db
        em.detach(updatedMaterialAvailability);
        updatedMaterialAvailability
            .creationDate(UPDATED_START_DATE)
            .updateDate(UPDATED_END_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE);

        restMaterialAvailabilityMockMvc.perform(put("/api/material-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterialAvailability)))
            .andExpect(status().isOk());

        // Validate the MaterialAvailability in the database
        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        MaterialAvailability testMaterialAvailability = materialAvailabilityList.get(materialAvailabilityList.size() - 1);
        assertThat(testMaterialAvailability.getCreationDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMaterialAvailability.getUpdateDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMaterialAvailability.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);

        // Validate the MaterialAvailability in Elasticsearch
        verify(mockMaterialAvailabilitySearchRepository, times(1)).save(testMaterialAvailability);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialAvailability() throws Exception {
        int databaseSizeBeforeUpdate = materialAvailabilityRepository.findAll().size();

        // Create the MaterialAvailability

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialAvailabilityMockMvc.perform(put("/api/material-availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialAvailability)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialAvailability in the database
        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MaterialAvailability in Elasticsearch
        verify(mockMaterialAvailabilitySearchRepository, times(0)).save(materialAvailability);
    }

    @Test
    @Transactional
    public void deleteMaterialAvailability() throws Exception {
        // Initialize the database
        materialAvailabilityService.save(materialAvailability);

        int databaseSizeBeforeDelete = materialAvailabilityRepository.findAll().size();

        // Get the materialAvailability
        restMaterialAvailabilityMockMvc.perform(delete("/api/material-availabilities/{id}", materialAvailability.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MaterialAvailability> materialAvailabilityList = materialAvailabilityRepository.findAll();
        assertThat(materialAvailabilityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MaterialAvailability in Elasticsearch
        verify(mockMaterialAvailabilitySearchRepository, times(1)).deleteById(materialAvailability.getId());
    }

    @Test
    @Transactional
    public void searchMaterialAvailability() throws Exception {
        // Initialize the database
        materialAvailabilityService.save(materialAvailability);
        when(mockMaterialAvailabilitySearchRepository.search(queryStringQuery("id:" + materialAvailability.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(materialAvailability), PageRequest.of(0, 1), 1));
        // Search the materialAvailability
        restMaterialAvailabilityMockMvc.perform(get("/api/_search/material-availabilities?query=id:" + materialAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialAvailability.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialAvailability.class);
        MaterialAvailability materialAvailability1 = new MaterialAvailability();
        materialAvailability1.setId(1L);
        MaterialAvailability materialAvailability2 = new MaterialAvailability();
        materialAvailability2.setId(materialAvailability1.getId());
        assertThat(materialAvailability1).isEqualTo(materialAvailability2);
        materialAvailability2.setId(2L);
        assertThat(materialAvailability1).isNotEqualTo(materialAvailability2);
        materialAvailability1.setId(null);
        assertThat(materialAvailability1).isNotEqualTo(materialAvailability2);
    }
}
