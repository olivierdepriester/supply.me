package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.DeliveryNote;
import com.baosong.supplyme.repository.DeliveryNoteRepository;
import com.baosong.supplyme.repository.search.DeliveryNoteSearchRepository;
import com.baosong.supplyme.service.DeliveryNoteService;
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

import com.baosong.supplyme.domain.enumeration.DeliveryNoteStatus;
/**
 * Test class for the DeliveryNoteResource REST controller.
 *
 * @see DeliveryNoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class DeliveryNoteResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DeliveryNoteStatus DEFAULT_STATUS = DeliveryNoteStatus.NEW;
    private static final DeliveryNoteStatus UPDATED_STATUS = DeliveryNoteStatus.NEW;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private DeliveryNoteService deliveryNoteService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.DeliveryNoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeliveryNoteSearchRepository mockDeliveryNoteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeliveryNoteMockMvc;

    private DeliveryNote deliveryNote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeliveryNoteResource deliveryNoteResource = new DeliveryNoteResource(deliveryNoteService);
        this.restDeliveryNoteMockMvc = MockMvcBuilders.standaloneSetup(deliveryNoteResource)
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
    public static DeliveryNote createEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .code(DEFAULT_CODE)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE);
        return deliveryNote;
    }

    @Before
    public void initTest() {
        deliveryNote = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliveryNote() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();

        // Create the DeliveryNote
        restDeliveryNoteMockMvc.perform(post("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isCreated());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDeliveryNote.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryNote.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(1)).save(testDeliveryNote);
    }

    @Test
    @Transactional
    public void createDeliveryNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();

        // Create the DeliveryNote with an existing ID
        deliveryNote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryNoteMockMvc.perform(post("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteRepository.findAll().size();
        // set the field null
        deliveryNote.setCode(null);

        // Create the DeliveryNote, which fails.

        restDeliveryNoteMockMvc.perform(post("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isBadRequest());

        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteRepository.findAll().size();
        // set the field null
        deliveryNote.setDeliveryDate(null);

        // Create the DeliveryNote, which fails.

        restDeliveryNoteMockMvc.perform(post("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isBadRequest());

        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteRepository.findAll().size();
        // set the field null
        deliveryNote.setCreationDate(null);

        // Create the DeliveryNote, which fails.

        restDeliveryNoteMockMvc.perform(post("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isBadRequest());

        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliveryNotes() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList
        restDeliveryNoteMockMvc.perform(get("/api/delivery-notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(get("/api/delivery-notes/{id}", deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryNote.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliveryNote() throws Exception {
        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(get("/api/delivery-notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteService.save(deliveryNote);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDeliveryNoteSearchRepository);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote
        DeliveryNote updatedDeliveryNote = deliveryNoteRepository.findById(deliveryNote.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryNote are not directly saved in db
        em.detach(updatedDeliveryNote);
        updatedDeliveryNote
            .code(UPDATED_CODE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restDeliveryNoteMockMvc.perform(put("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeliveryNote)))
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDeliveryNote.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testDeliveryNote.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryNote.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(1)).save(testDeliveryNote);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Create the DeliveryNote

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc.perform(put("/api/delivery-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryNote)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    public void deleteDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteService.save(deliveryNote);

        int databaseSizeBeforeDelete = deliveryNoteRepository.findAll().size();

        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(delete("/api/delivery-notes/{id}", deliveryNote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(1)).deleteById(deliveryNote.getId());
    }

    @Test
    @Transactional
    public void searchDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteService.save(deliveryNote);
        when(mockDeliveryNoteSearchRepository.search(queryStringQuery("id:" + deliveryNote.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deliveryNote), PageRequest.of(0, 1), 1));
        // Search the deliveryNote
        restDeliveryNoteMockMvc.perform(get("/api/_search/delivery-notes?query=id:" + deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryNote.class);
        DeliveryNote deliveryNote1 = new DeliveryNote();
        deliveryNote1.setId(1L);
        DeliveryNote deliveryNote2 = new DeliveryNote();
        deliveryNote2.setId(deliveryNote1.getId());
        assertThat(deliveryNote1).isEqualTo(deliveryNote2);
        deliveryNote2.setId(2L);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
        deliveryNote1.setId(null);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
    }
}
