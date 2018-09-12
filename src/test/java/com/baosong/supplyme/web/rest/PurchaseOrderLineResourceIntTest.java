package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.repository.PurchaseOrderLineRepository;
import com.baosong.supplyme.repository.search.PurchaseOrderLineSearchRepository;
import com.baosong.supplyme.service.PurchaseOrderLineService;
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
 * Test class for the PurchaseOrderLineResource REST controller.
 *
 * @see PurchaseOrderLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class PurchaseOrderLineResourceIntTest {

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    private static final Double DEFAULT_ORDER_PRICE = 0D;
    private static final Double UPDATED_ORDER_PRICE = 1D;

    @Autowired
    private PurchaseOrderLineRepository purchaseOrderLineRepository;

    

    @Autowired
    private PurchaseOrderLineService purchaseOrderLineService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.PurchaseOrderLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseOrderLineSearchRepository mockPurchaseOrderLineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseOrderLineMockMvc;

    private PurchaseOrderLine purchaseOrderLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseOrderLineResource purchaseOrderLineResource = new PurchaseOrderLineResource(purchaseOrderLineService);
        this.restPurchaseOrderLineMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderLineResource)
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
    public static PurchaseOrderLine createEntity(EntityManager em) {
        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine()
            .lineNumber(DEFAULT_LINE_NUMBER)
            .quantity(DEFAULT_QUANTITY)
            .orderPrice(DEFAULT_ORDER_PRICE);
        return purchaseOrderLine;
    }

    @Before
    public void initTest() {
        purchaseOrderLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseOrderLine() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderLineRepository.findAll().size();

        // Create the PurchaseOrderLine
        restPurchaseOrderLineMockMvc.perform(post("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderLine in the database
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderLine testPurchaseOrderLine = purchaseOrderLineList.get(purchaseOrderLineList.size() - 1);
        assertThat(testPurchaseOrderLine.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testPurchaseOrderLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPurchaseOrderLine.getOrderPrice()).isEqualTo(DEFAULT_ORDER_PRICE);

        // Validate the PurchaseOrderLine in Elasticsearch
        verify(mockPurchaseOrderLineSearchRepository, times(1)).save(testPurchaseOrderLine);
    }

    @Test
    @Transactional
    public void createPurchaseOrderLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderLineRepository.findAll().size();

        // Create the PurchaseOrderLine with an existing ID
        purchaseOrderLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderLineMockMvc.perform(post("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLine in the database
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the PurchaseOrderLine in Elasticsearch
        verify(mockPurchaseOrderLineSearchRepository, times(0)).save(purchaseOrderLine);
    }

    @Test
    @Transactional
    public void checkLineNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderLineRepository.findAll().size();
        // set the field null
        purchaseOrderLine.setLineNumber(null);

        // Create the PurchaseOrderLine, which fails.

        restPurchaseOrderLineMockMvc.perform(post("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderLineRepository.findAll().size();
        // set the field null
        purchaseOrderLine.setQuantity(null);

        // Create the PurchaseOrderLine, which fails.

        restPurchaseOrderLineMockMvc.perform(post("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderLineRepository.findAll().size();
        // set the field null
        purchaseOrderLine.setOrderPrice(null);

        // Create the PurchaseOrderLine, which fails.

        restPurchaseOrderLineMockMvc.perform(post("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderLines() throws Exception {
        // Initialize the database
        purchaseOrderLineRepository.saveAndFlush(purchaseOrderLine);

        // Get all the purchaseOrderLineList
        restPurchaseOrderLineMockMvc.perform(get("/api/purchase-order-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].orderPrice").value(hasItem(DEFAULT_ORDER_PRICE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getPurchaseOrderLine() throws Exception {
        // Initialize the database
        purchaseOrderLineRepository.saveAndFlush(purchaseOrderLine);

        // Get the purchaseOrderLine
        restPurchaseOrderLineMockMvc.perform(get("/api/purchase-order-lines/{id}", purchaseOrderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderLine.getId().intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.orderPrice").value(DEFAULT_ORDER_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPurchaseOrderLine() throws Exception {
        // Get the purchaseOrderLine
        restPurchaseOrderLineMockMvc.perform(get("/api/purchase-order-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrderLine() throws Exception {
        // Initialize the database
        purchaseOrderLineService.save(purchaseOrderLine);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPurchaseOrderLineSearchRepository);

        int databaseSizeBeforeUpdate = purchaseOrderLineRepository.findAll().size();

        // Update the purchaseOrderLine
        PurchaseOrderLine updatedPurchaseOrderLine = purchaseOrderLineRepository.findById(purchaseOrderLine.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderLine are not directly saved in db
        em.detach(updatedPurchaseOrderLine);
        updatedPurchaseOrderLine
            .lineNumber(UPDATED_LINE_NUMBER)
            .quantity(UPDATED_QUANTITY)
            .orderPrice(UPDATED_ORDER_PRICE);

        restPurchaseOrderLineMockMvc.perform(put("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrderLine)))
            .andExpect(status().isOk());

        // Validate the PurchaseOrderLine in the database
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderLine testPurchaseOrderLine = purchaseOrderLineList.get(purchaseOrderLineList.size() - 1);
        assertThat(testPurchaseOrderLine.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testPurchaseOrderLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseOrderLine.getOrderPrice()).isEqualTo(UPDATED_ORDER_PRICE);

        // Validate the PurchaseOrderLine in Elasticsearch
        verify(mockPurchaseOrderLineSearchRepository, times(1)).save(testPurchaseOrderLine);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineRepository.findAll().size();

        // Create the PurchaseOrderLine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restPurchaseOrderLineMockMvc.perform(put("/api/purchase-order-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLine)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLine in the database
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrderLine in Elasticsearch
        verify(mockPurchaseOrderLineSearchRepository, times(0)).save(purchaseOrderLine);
    }

    @Test
    @Transactional
    public void deletePurchaseOrderLine() throws Exception {
        // Initialize the database
        purchaseOrderLineService.save(purchaseOrderLine);

        int databaseSizeBeforeDelete = purchaseOrderLineRepository.findAll().size();

        // Get the purchaseOrderLine
        restPurchaseOrderLineMockMvc.perform(delete("/api/purchase-order-lines/{id}", purchaseOrderLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findAll();
        assertThat(purchaseOrderLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PurchaseOrderLine in Elasticsearch
        verify(mockPurchaseOrderLineSearchRepository, times(1)).deleteById(purchaseOrderLine.getId());
    }

    @Test
    @Transactional
    public void searchPurchaseOrderLine() throws Exception {
        // Initialize the database
        purchaseOrderLineService.save(purchaseOrderLine);
        when(mockPurchaseOrderLineSearchRepository.search(queryStringQuery("id:" + purchaseOrderLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(purchaseOrderLine), PageRequest.of(0, 1), 1));
        // Search the purchaseOrderLine
        restPurchaseOrderLineMockMvc.perform(get("/api/_search/purchase-order-lines?query=id:" + purchaseOrderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].orderPrice").value(hasItem(DEFAULT_ORDER_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderLine.class);
        PurchaseOrderLine purchaseOrderLine1 = new PurchaseOrderLine();
        purchaseOrderLine1.setId(1L);
        PurchaseOrderLine purchaseOrderLine2 = new PurchaseOrderLine();
        purchaseOrderLine2.setId(purchaseOrderLine1.getId());
        assertThat(purchaseOrderLine1).isEqualTo(purchaseOrderLine2);
        purchaseOrderLine2.setId(2L);
        assertThat(purchaseOrderLine1).isNotEqualTo(purchaseOrderLine2);
        purchaseOrderLine1.setId(null);
        assertThat(purchaseOrderLine1).isNotEqualTo(purchaseOrderLine2);
    }
}
