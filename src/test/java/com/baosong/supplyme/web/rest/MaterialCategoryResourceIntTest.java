package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.MaterialCategory;
import com.baosong.supplyme.repository.MaterialCategoryRepository;
import com.baosong.supplyme.repository.search.MaterialCategorySearchRepository;
import com.baosong.supplyme.service.MaterialCategoryService;
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
 * Test class for the MaterialCategoryResource REST controller.
 *
 * @see MaterialCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class MaterialCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MaterialCategoryRepository materialCategoryRepository;

    @Autowired
    private MaterialCategoryService materialCategoryService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.MaterialCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private MaterialCategorySearchRepository mockMaterialCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaterialCategoryMockMvc;

    private MaterialCategory materialCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialCategoryResource materialCategoryResource = new MaterialCategoryResource(materialCategoryService);
        this.restMaterialCategoryMockMvc = MockMvcBuilders.standaloneSetup(materialCategoryResource)
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
    public static MaterialCategory createEntity(EntityManager em) {
        MaterialCategory materialCategory = new MaterialCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE);
        return materialCategory;
    }

    @Before
    public void initTest() {
        materialCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialCategory() throws Exception {
        int databaseSizeBeforeCreate = materialCategoryRepository.findAll().size();

        // Create the MaterialCategory
        restMaterialCategoryMockMvc.perform(post("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialCategory)))
            .andExpect(status().isCreated());

        // Validate the MaterialCategory in the database
        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialCategory testMaterialCategory = materialCategoryList.get(materialCategoryList.size() - 1);
        assertThat(testMaterialCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterialCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaterialCategory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the MaterialCategory in Elasticsearch
        verify(mockMaterialCategorySearchRepository, times(1)).save(testMaterialCategory);
    }

    @Test
    @Transactional
    public void createMaterialCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialCategoryRepository.findAll().size();

        // Create the MaterialCategory with an existing ID
        materialCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialCategoryMockMvc.perform(post("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialCategory)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialCategory in the database
        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the MaterialCategory in Elasticsearch
        verify(mockMaterialCategorySearchRepository, times(0)).save(materialCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialCategoryRepository.findAll().size();
        // set the field null
        materialCategory.setName(null);

        // Create the MaterialCategory, which fails.

        restMaterialCategoryMockMvc.perform(post("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialCategory)))
            .andExpect(status().isBadRequest());

        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialCategoryRepository.findAll().size();
        // set the field null
        materialCategory.setCreationDate(null);

        // Create the MaterialCategory, which fails.

        restMaterialCategoryMockMvc.perform(post("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialCategory)))
            .andExpect(status().isBadRequest());

        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterialCategories() throws Exception {
        // Initialize the database
        materialCategoryRepository.saveAndFlush(materialCategory);

        // Get all the materialCategoryList
        restMaterialCategoryMockMvc.perform(get("/api/material-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getMaterialCategory() throws Exception {
        // Initialize the database
        materialCategoryRepository.saveAndFlush(materialCategory);

        // Get the materialCategory
        restMaterialCategoryMockMvc.perform(get("/api/material-categories/{id}", materialCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialCategory() throws Exception {
        // Get the materialCategory
        restMaterialCategoryMockMvc.perform(get("/api/material-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialCategory() throws Exception {
        // Initialize the database
        materialCategoryService.save(materialCategory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMaterialCategorySearchRepository);

        int databaseSizeBeforeUpdate = materialCategoryRepository.findAll().size();

        // Update the materialCategory
        MaterialCategory updatedMaterialCategory = materialCategoryRepository.findById(materialCategory.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialCategory are not directly saved in db
        em.detach(updatedMaterialCategory);
        updatedMaterialCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);

        restMaterialCategoryMockMvc.perform(put("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterialCategory)))
            .andExpect(status().isOk());

        // Validate the MaterialCategory in the database
        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeUpdate);
        MaterialCategory testMaterialCategory = materialCategoryList.get(materialCategoryList.size() - 1);
        assertThat(testMaterialCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterialCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaterialCategory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the MaterialCategory in Elasticsearch
        verify(mockMaterialCategorySearchRepository, times(1)).save(testMaterialCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialCategory() throws Exception {
        int databaseSizeBeforeUpdate = materialCategoryRepository.findAll().size();

        // Create the MaterialCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialCategoryMockMvc.perform(put("/api/material-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialCategory)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialCategory in the database
        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MaterialCategory in Elasticsearch
        verify(mockMaterialCategorySearchRepository, times(0)).save(materialCategory);
    }

    @Test
    @Transactional
    public void deleteMaterialCategory() throws Exception {
        // Initialize the database
        materialCategoryService.save(materialCategory);

        int databaseSizeBeforeDelete = materialCategoryRepository.findAll().size();

        // Get the materialCategory
        restMaterialCategoryMockMvc.perform(delete("/api/material-categories/{id}", materialCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MaterialCategory> materialCategoryList = materialCategoryRepository.findAll();
        assertThat(materialCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MaterialCategory in Elasticsearch
        verify(mockMaterialCategorySearchRepository, times(1)).deleteById(materialCategory.getId());
    }

    @Test
    @Transactional
    public void searchMaterialCategory() throws Exception {
        // Initialize the database
        materialCategoryService.save(materialCategory);
        when(mockMaterialCategorySearchRepository.search(queryStringQuery("id:" + materialCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(materialCategory), PageRequest.of(0, 1), 1));
        // Search the materialCategory
        restMaterialCategoryMockMvc.perform(get("/api/_search/material-categories?query=id:" + materialCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialCategory.class);
        MaterialCategory materialCategory1 = new MaterialCategory();
        materialCategory1.setId(1L);
        MaterialCategory materialCategory2 = new MaterialCategory();
        materialCategory2.setId(materialCategory1.getId());
        assertThat(materialCategory1).isEqualTo(materialCategory2);
        materialCategory2.setId(2L);
        assertThat(materialCategory1).isNotEqualTo(materialCategory2);
        materialCategory1.setId(null);
        assertThat(materialCategory1).isNotEqualTo(materialCategory2);
    }
}
