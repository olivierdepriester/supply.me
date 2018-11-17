package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.repository.DemandRepository;
import com.baosong.supplyme.repository.search.DemandSearchRepository;
import com.baosong.supplyme.service.AttachmentFileService;
import com.baosong.supplyme.service.DemandService;
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

import com.baosong.supplyme.domain.enumeration.DemandStatus;
/**
 * Test class for the DemandResource REST controller.
 *
 * @see DemandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class DemandResourceIntTest {

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final DemandStatus DEFAULT_STATUS = DemandStatus.NEW;
    private static final DemandStatus UPDATED_STATUS = DemandStatus.ORDERED;

    private static final Instant DEFAULT_EXPECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private DemandService demandService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.DemandSearchRepositoryMockConfiguration
     */
    @Autowired
    private DemandSearchRepository mockDemandSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDemandMockMvc;

    private Demand demand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DemandResource demandResource = new DemandResource(demandService, attachmentFileService);
        this.restDemandMockMvc = MockMvcBuilders.standaloneSetup(demandResource)
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
    public static Demand createEntity(EntityManager em) {
        Demand demand = new Demand()
            .quantity(DEFAULT_QUANTITY)
            .status(DEFAULT_STATUS)
            .expectedDate(DEFAULT_EXPECTED_DATE)
            .creationDate(DEFAULT_CREATION_DATE);
        return demand;
    }

    @Before
    public void initTest() {
        demand = createEntity(em);
    }

    @Test
    @Transactional
    public void createDemand() throws Exception {
        int databaseSizeBeforeCreate = demandRepository.findAll().size();

        // Create the Demand
        restDemandMockMvc.perform(post("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isCreated());

        // Validate the Demand in the database
        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeCreate + 1);
        Demand testDemand = demandList.get(demandList.size() - 1);
        assertThat(testDemand.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDemand.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDemand.getExpectedDate()).isEqualTo(DEFAULT_EXPECTED_DATE);
        assertThat(testDemand.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the Demand in Elasticsearch
        verify(mockDemandSearchRepository, times(1)).save(testDemand);
    }

    @Test
    @Transactional
    public void createDemandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = demandRepository.findAll().size();

        // Create the Demand with an existing ID
        demand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandMockMvc.perform(post("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isBadRequest());

        // Validate the Demand in the database
        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeCreate);

        // Validate the Demand in Elasticsearch
        verify(mockDemandSearchRepository, times(0)).save(demand);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandRepository.findAll().size();
        // set the field null
        demand.setQuantity(null);

        // Create the Demand, which fails.

        restDemandMockMvc.perform(post("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isBadRequest());

        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandRepository.findAll().size();
        // set the field null
        demand.setStatus(null);

        // Create the Demand, which fails.

        restDemandMockMvc.perform(post("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isBadRequest());

        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandRepository.findAll().size();
        // set the field null
        demand.setCreationDate(null);

        // Create the Demand, which fails.

        restDemandMockMvc.perform(post("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isBadRequest());

        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDemands() throws Exception {
        // Initialize the database
        demandRepository.saveAndFlush(demand);

        // Get all the demandList
        restDemandMockMvc.perform(get("/api/demands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demand.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expectedDate").value(hasItem(DEFAULT_EXPECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDemand() throws Exception {
        // Initialize the database
        demandRepository.saveAndFlush(demand);

        // Get the demand
        restDemandMockMvc.perform(get("/api/demands/{id}", demand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(demand.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expectedDate").value(DEFAULT_EXPECTED_DATE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemand() throws Exception {
        // Get the demand
        restDemandMockMvc.perform(get("/api/demands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemand() throws Exception {
        // Initialize the database
        demandService.save(demand);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDemandSearchRepository);

        int databaseSizeBeforeUpdate = demandRepository.findAll().size();

        // Update the demand
        Demand updatedDemand = demandRepository.findById(demand.getId()).get();
        // Disconnect from session so that the updates on updatedDemand are not directly saved in db
        em.detach(updatedDemand);
        updatedDemand
            .quantity(UPDATED_QUANTITY)
            .status(UPDATED_STATUS)
            .expectedDate(UPDATED_EXPECTED_DATE)
            .creationDate(UPDATED_CREATION_DATE);

        restDemandMockMvc.perform(put("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDemand)))
            .andExpect(status().isOk());

        // Validate the Demand in the database
        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeUpdate);
        Demand testDemand = demandList.get(demandList.size() - 1);
        assertThat(testDemand.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDemand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemand.getExpectedDate()).isEqualTo(UPDATED_EXPECTED_DATE);
        assertThat(testDemand.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the Demand in Elasticsearch
        verify(mockDemandSearchRepository, times(1)).save(testDemand);
    }

    @Test
    @Transactional
    public void updateNonExistingDemand() throws Exception {
        int databaseSizeBeforeUpdate = demandRepository.findAll().size();

        // Create the Demand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandMockMvc.perform(put("/api/demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demand)))
            .andExpect(status().isBadRequest());

        // Validate the Demand in the database
        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Demand in Elasticsearch
        verify(mockDemandSearchRepository, times(0)).save(demand);
    }

    @Test
    @Transactional
    public void deleteDemand() throws Exception {
        // Initialize the database
        demandService.save(demand);

        int databaseSizeBeforeDelete = demandRepository.findAll().size();

        // Get the demand
        restDemandMockMvc.perform(delete("/api/demands/{id}", demand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Demand> demandList = demandRepository.findAll();
        assertThat(demandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Demand in Elasticsearch
        verify(mockDemandSearchRepository, times(1)).deleteById(demand.getId());
    }

    @Test
    @Transactional
    public void searchDemand() throws Exception {
        // Initialize the database
        demandService.save(demand);
        when(mockDemandSearchRepository.search(queryStringQuery("id:" + demand.getId())))
            .thenReturn(Collections.singletonList(demand));
        // Search the demand
        restDemandMockMvc.perform(get("/api/_search/demands?query=id:" + demand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demand.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expectedDate").value(hasItem(DEFAULT_EXPECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demand.class);
        Demand demand1 = new Demand();
        demand1.setId(1L);
        Demand demand2 = new Demand();
        demand2.setId(demand1.getId());
        assertThat(demand1).isEqualTo(demand2);
        demand2.setId(2L);
        assertThat(demand1).isNotEqualTo(demand2);
        demand1.setId(null);
        assertThat(demand1).isNotEqualTo(demand2);
    }
}
