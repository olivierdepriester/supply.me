package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.DeliveryNoteLine;
import com.baosong.supplyme.repository.DeliveryNoteLineRepository;
import com.baosong.supplyme.repository.search.DeliveryNoteLineSearchRepository;
import com.baosong.supplyme.service.DeliveryNoteLineService;
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
 * Test class for the DeliveryNoteLineResource REST controller.
 *
 * @see DeliveryNoteLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class DeliveryNoteLineResourceIntTest {

    private static final Integer DEFAULT_LINE_NUMBER = 0;
    private static final Integer UPDATED_LINE_NUMBER = 1;

    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    @Autowired
    private DeliveryNoteLineRepository deliveryNoteLineRepository;

    @Autowired
    private DeliveryNoteLineService deliveryNoteLineService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.DeliveryNoteLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeliveryNoteLineSearchRepository mockDeliveryNoteLineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeliveryNoteLineMockMvc;

    private DeliveryNoteLine deliveryNoteLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeliveryNoteLineResource deliveryNoteLineResource = new DeliveryNoteLineResource(deliveryNoteLineService);
        this.restDeliveryNoteLineMockMvc = MockMvcBuilders.standaloneSetup(deliveryNoteLineResource)
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
    public static DeliveryNoteLine createEntity(EntityManager em) {
        DeliveryNoteLine deliveryNoteLine = new DeliveryNoteLine()
            .lineNumber(DEFAULT_LINE_NUMBER)
            .quantity(DEFAULT_QUANTITY);
        return deliveryNoteLine;
    }

    @Before
    public void initTest() {
        deliveryNoteLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliveryNoteLine() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteLineRepository.findAll().size();

        // Create the DeliveryNoteLine
        restDeliveryNoteLineMockMvc.perform(post("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNoteLine)))
            .andExpect(status().isCreated());

        // Validate the DeliveryNoteLine in the database
        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryNoteLine testDeliveryNoteLine = deliveryNoteLineList.get(deliveryNoteLineList.size() - 1);
        assertThat(testDeliveryNoteLine.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testDeliveryNoteLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the DeliveryNoteLine in Elasticsearch
        verify(mockDeliveryNoteLineSearchRepository, times(1)).save(testDeliveryNoteLine);
    }

    @Test
    @Transactional
    public void createDeliveryNoteLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteLineRepository.findAll().size();

        // Create the DeliveryNoteLine with an existing ID
        deliveryNoteLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryNoteLineMockMvc.perform(post("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNoteLine)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNoteLine in the database
        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeliveryNoteLine in Elasticsearch
        verify(mockDeliveryNoteLineSearchRepository, times(0)).save(deliveryNoteLine);
    }

    @Test
    @Transactional
    public void checkLineNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteLineRepository.findAll().size();
        // set the field null
        deliveryNoteLine.setLineNumber(null);

        // Create the DeliveryNoteLine, which fails.

        restDeliveryNoteLineMockMvc.perform(post("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNoteLine)))
            .andExpect(status().isBadRequest());

        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteLineRepository.findAll().size();
        // set the field null
        deliveryNoteLine.setQuantity(null);

        // Create the DeliveryNoteLine, which fails.

        restDeliveryNoteLineMockMvc.perform(post("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNoteLine)))
            .andExpect(status().isBadRequest());

        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliveryNoteLines() throws Exception {
        // Initialize the database
        deliveryNoteLineRepository.saveAndFlush(deliveryNoteLine);

        // Get all the deliveryNoteLineList
        restDeliveryNoteLineMockMvc.perform(get("/api/delivery-note-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNoteLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getDeliveryNoteLine() throws Exception {
        // Initialize the database
        deliveryNoteLineRepository.saveAndFlush(deliveryNoteLine);

        // Get the deliveryNoteLine
        restDeliveryNoteLineMockMvc.perform(get("/api/delivery-note-lines/{id}", deliveryNoteLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryNoteLine.getId().intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliveryNoteLine() throws Exception {
        // Get the deliveryNoteLine
        restDeliveryNoteLineMockMvc.perform(get("/api/delivery-note-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryNoteLine() throws Exception {
        // Initialize the database
        deliveryNoteLineService.save(deliveryNoteLine);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDeliveryNoteLineSearchRepository);

        int databaseSizeBeforeUpdate = deliveryNoteLineRepository.findAll().size();

        // Update the deliveryNoteLine
        DeliveryNoteLine updatedDeliveryNoteLine = deliveryNoteLineRepository.findById(deliveryNoteLine.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryNoteLine are not directly saved in db
        em.detach(updatedDeliveryNoteLine);
        updatedDeliveryNoteLine
            .lineNumber(UPDATED_LINE_NUMBER)
            .quantity(UPDATED_QUANTITY);

        restDeliveryNoteLineMockMvc.perform(put("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeliveryNoteLine)))
            .andExpect(status().isOk());

        // Validate the DeliveryNoteLine in the database
        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNoteLine testDeliveryNoteLine = deliveryNoteLineList.get(deliveryNoteLineList.size() - 1);
        assertThat(testDeliveryNoteLine.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testDeliveryNoteLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the DeliveryNoteLine in Elasticsearch
        verify(mockDeliveryNoteLineSearchRepository, times(1)).save(testDeliveryNoteLine);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliveryNoteLine() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteLineRepository.findAll().size();

        // Create the DeliveryNoteLine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteLineMockMvc.perform(put("/api/delivery-note-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNoteLine)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNoteLine in the database
        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNoteLine in Elasticsearch
        verify(mockDeliveryNoteLineSearchRepository, times(0)).save(deliveryNoteLine);
    }

    @Test
    @Transactional
    public void deleteDeliveryNoteLine() throws Exception {
        // Initialize the database
        deliveryNoteLineService.save(deliveryNoteLine);

        int databaseSizeBeforeDelete = deliveryNoteLineRepository.findAll().size();

        // Get the deliveryNoteLine
        restDeliveryNoteLineMockMvc.perform(delete("/api/delivery-note-lines/{id}", deliveryNoteLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DeliveryNoteLine> deliveryNoteLineList = deliveryNoteLineRepository.findAll();
        assertThat(deliveryNoteLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeliveryNoteLine in Elasticsearch
        verify(mockDeliveryNoteLineSearchRepository, times(1)).deleteById(deliveryNoteLine.getId());
    }

    @Test
    @Transactional
    public void searchDeliveryNoteLine() throws Exception {
        // Initialize the database
        deliveryNoteLineService.save(deliveryNoteLine);
        when(mockDeliveryNoteLineSearchRepository.search(queryStringQuery("id:" + deliveryNoteLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deliveryNoteLine), PageRequest.of(0, 1), 1));
        // Search the deliveryNoteLine
        restDeliveryNoteLineMockMvc.perform(get("/api/_search/delivery-note-lines?query=id:" + deliveryNoteLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNoteLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryNoteLine.class);
        DeliveryNoteLine deliveryNoteLine1 = new DeliveryNoteLine();
        deliveryNoteLine1.setId(1L);
        DeliveryNoteLine deliveryNoteLine2 = new DeliveryNoteLine();
        deliveryNoteLine2.setId(deliveryNoteLine1.getId());
        assertThat(deliveryNoteLine1).isEqualTo(deliveryNoteLine2);
        deliveryNoteLine2.setId(2L);
        assertThat(deliveryNoteLine1).isNotEqualTo(deliveryNoteLine2);
        deliveryNoteLine1.setId(null);
        assertThat(deliveryNoteLine1).isNotEqualTo(deliveryNoteLine2);
    }
}
