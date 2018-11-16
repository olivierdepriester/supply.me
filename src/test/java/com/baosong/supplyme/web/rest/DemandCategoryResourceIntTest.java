package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.DemandCategory;
import com.baosong.supplyme.domain.User;
import com.baosong.supplyme.repository.DemandCategoryRepository;
import com.baosong.supplyme.repository.search.DemandCategorySearchRepository;
import com.baosong.supplyme.service.DemandCategoryService;
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
 * Test class for the DemandCategoryResource REST controller.
 *
 * @see DemandCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class DemandCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DemandCategoryRepository demandCategoryRepository;

    @Autowired
    private DemandCategoryService demandCategoryService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.DemandCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private DemandCategorySearchRepository mockDemandCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDemandCategoryMockMvc;

    private DemandCategory demandCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DemandCategoryResource demandCategoryResource = new DemandCategoryResource(demandCategoryService);
        this.restDemandCategoryMockMvc = MockMvcBuilders.standaloneSetup(demandCategoryResource)
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
    public static DemandCategory createEntity(EntityManager em) {
        DemandCategory demandCategory = new DemandCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        demandCategory.setCreationUser(user);
        return demandCategory;
    }

    @Before
    public void initTest() {
        demandCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDemandCategory() throws Exception {
        int databaseSizeBeforeCreate = demandCategoryRepository.findAll().size();

        // Create the DemandCategory
        restDemandCategoryMockMvc.perform(post("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandCategory)))
            .andExpect(status().isCreated());

        // Validate the DemandCategory in the database
        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        DemandCategory testDemandCategory = demandCategoryList.get(demandCategoryList.size() - 1);
        assertThat(testDemandCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDemandCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDemandCategory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the DemandCategory in Elasticsearch
        verify(mockDemandCategorySearchRepository, times(1)).save(testDemandCategory);
    }

    @Test
    @Transactional
    public void createDemandCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = demandCategoryRepository.findAll().size();

        // Create the DemandCategory with an existing ID
        demandCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandCategoryMockMvc.perform(post("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandCategory)))
            .andExpect(status().isBadRequest());

        // Validate the DemandCategory in the database
        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the DemandCategory in Elasticsearch
        verify(mockDemandCategorySearchRepository, times(0)).save(demandCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandCategoryRepository.findAll().size();
        // set the field null
        demandCategory.setName(null);

        // Create the DemandCategory, which fails.

        restDemandCategoryMockMvc.perform(post("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandCategory)))
            .andExpect(status().isBadRequest());

        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandCategoryRepository.findAll().size();
        // set the field null
        demandCategory.setCreationDate(null);

        // Create the DemandCategory, which fails.

        restDemandCategoryMockMvc.perform(post("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandCategory)))
            .andExpect(status().isBadRequest());

        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDemandCategories() throws Exception {
        // Initialize the database
        demandCategoryRepository.saveAndFlush(demandCategory);

        // Get all the demandCategoryList
        restDemandCategoryMockMvc.perform(get("/api/demand-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getDemandCategory() throws Exception {
        // Initialize the database
        demandCategoryRepository.saveAndFlush(demandCategory);

        // Get the demandCategory
        restDemandCategoryMockMvc.perform(get("/api/demand-categories/{id}", demandCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(demandCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemandCategory() throws Exception {
        // Get the demandCategory
        restDemandCategoryMockMvc.perform(get("/api/demand-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemandCategory() throws Exception {
        // Initialize the database
        demandCategoryService.save(demandCategory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDemandCategorySearchRepository);

        int databaseSizeBeforeUpdate = demandCategoryRepository.findAll().size();

        // Update the demandCategory
        DemandCategory updatedDemandCategory = demandCategoryRepository.findById(demandCategory.getId()).get();
        // Disconnect from session so that the updates on updatedDemandCategory are not directly saved in db
        em.detach(updatedDemandCategory);
        updatedDemandCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);

        restDemandCategoryMockMvc.perform(put("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDemandCategory)))
            .andExpect(status().isOk());

        // Validate the DemandCategory in the database
        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeUpdate);
        DemandCategory testDemandCategory = demandCategoryList.get(demandCategoryList.size() - 1);
        assertThat(testDemandCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDemandCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDemandCategory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the DemandCategory in Elasticsearch
        verify(mockDemandCategorySearchRepository, times(1)).save(testDemandCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingDemandCategory() throws Exception {
        int databaseSizeBeforeUpdate = demandCategoryRepository.findAll().size();

        // Create the DemandCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandCategoryMockMvc.perform(put("/api/demand-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandCategory)))
            .andExpect(status().isBadRequest());

        // Validate the DemandCategory in the database
        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DemandCategory in Elasticsearch
        verify(mockDemandCategorySearchRepository, times(0)).save(demandCategory);
    }

    @Test
    @Transactional
    public void deleteDemandCategory() throws Exception {
        // Initialize the database
        demandCategoryService.save(demandCategory);

        int databaseSizeBeforeDelete = demandCategoryRepository.findAll().size();

        // Get the demandCategory
        restDemandCategoryMockMvc.perform(delete("/api/demand-categories/{id}", demandCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DemandCategory> demandCategoryList = demandCategoryRepository.findAll();
        assertThat(demandCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DemandCategory in Elasticsearch
        verify(mockDemandCategorySearchRepository, times(1)).deleteById(demandCategory.getId());
    }

    @Test
    @Transactional
    public void searchDemandCategory() throws Exception {
        // Initialize the database
        demandCategoryService.save(demandCategory);
        when(mockDemandCategorySearchRepository.search(queryStringQuery("id:" + demandCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(demandCategory), PageRequest.of(0, 1), 1));
        // Search the demandCategory
        restDemandCategoryMockMvc.perform(get("/api/_search/demand-categories?query=id:" + demandCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandCategory.class);
        DemandCategory demandCategory1 = new DemandCategory();
        demandCategory1.setId(1L);
        DemandCategory demandCategory2 = new DemandCategory();
        demandCategory2.setId(demandCategory1.getId());
        assertThat(demandCategory1).isEqualTo(demandCategory2);
        demandCategory2.setId(2L);
        assertThat(demandCategory1).isNotEqualTo(demandCategory2);
        demandCategory1.setId(null);
        assertThat(demandCategory1).isNotEqualTo(demandCategory2);
    }
}
