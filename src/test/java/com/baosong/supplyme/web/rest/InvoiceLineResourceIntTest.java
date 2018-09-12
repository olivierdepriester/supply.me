package com.baosong.supplyme.web.rest;

import com.baosong.supplyme.SupplyMeApp;

import com.baosong.supplyme.domain.InvoiceLine;
import com.baosong.supplyme.repository.InvoiceLineRepository;
import com.baosong.supplyme.repository.search.InvoiceLineSearchRepository;
import com.baosong.supplyme.service.InvoiceLineService;
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
 * Test class for the InvoiceLineResource REST controller.
 *
 * @see InvoiceLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SupplyMeApp.class)
public class InvoiceLineResourceIntTest {

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    private static final Double DEFAULT_AMOUNT_NET = 0D;
    private static final Double UPDATED_AMOUNT_NET = 1D;

    private static final Double DEFAULT_AMOUNT_WITH_TAX = 0D;
    private static final Double UPDATED_AMOUNT_WITH_TAX = 1D;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    

    @Autowired
    private InvoiceLineService invoiceLineService;

    /**
     * This repository is mocked in the com.baosong.supplyme.repository.search test package.
     *
     * @see com.baosong.supplyme.repository.search.InvoiceLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceLineSearchRepository mockInvoiceLineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceLineMockMvc;

    private InvoiceLine invoiceLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceLineResource invoiceLineResource = new InvoiceLineResource(invoiceLineService);
        this.restInvoiceLineMockMvc = MockMvcBuilders.standaloneSetup(invoiceLineResource)
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
    public static InvoiceLine createEntity(EntityManager em) {
        InvoiceLine invoiceLine = new InvoiceLine()
            .lineNumber(DEFAULT_LINE_NUMBER)
            .quantity(DEFAULT_QUANTITY)
            .amountNet(DEFAULT_AMOUNT_NET)
            .amountWithTax(DEFAULT_AMOUNT_WITH_TAX);
        return invoiceLine;
    }

    @Before
    public void initTest() {
        invoiceLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceLine() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine
        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isCreated());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceLine testInvoiceLine = invoiceLineList.get(invoiceLineList.size() - 1);
        assertThat(testInvoiceLine.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testInvoiceLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceLine.getAmountNet()).isEqualTo(DEFAULT_AMOUNT_NET);
        assertThat(testInvoiceLine.getAmountWithTax()).isEqualTo(DEFAULT_AMOUNT_WITH_TAX);

        // Validate the InvoiceLine in Elasticsearch
        verify(mockInvoiceLineSearchRepository, times(1)).save(testInvoiceLine);
    }

    @Test
    @Transactional
    public void createInvoiceLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine with an existing ID
        invoiceLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the InvoiceLine in Elasticsearch
        verify(mockInvoiceLineSearchRepository, times(0)).save(invoiceLine);
    }

    @Test
    @Transactional
    public void checkLineNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setLineNumber(null);

        // Create the InvoiceLine, which fails.

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setQuantity(null);

        // Create the InvoiceLine, which fails.

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountNetIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setAmountNet(null);

        // Create the InvoiceLine, which fails.

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountWithTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setAmountWithTax(null);

        // Create the InvoiceLine, which fails.

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceLines() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].amountNet").value(hasItem(DEFAULT_AMOUNT_NET.doubleValue())))
            .andExpect(jsonPath("$.[*].amountWithTax").value(hasItem(DEFAULT_AMOUNT_WITH_TAX.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines/{id}", invoiceLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceLine.getId().intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.amountNet").value(DEFAULT_AMOUNT_NET.doubleValue()))
            .andExpect(jsonPath("$.amountWithTax").value(DEFAULT_AMOUNT_WITH_TAX.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceLine() throws Exception {
        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineService.save(invoiceLine);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInvoiceLineSearchRepository);

        int databaseSizeBeforeUpdate = invoiceLineRepository.findAll().size();

        // Update the invoiceLine
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.findById(invoiceLine.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceLine are not directly saved in db
        em.detach(updatedInvoiceLine);
        updatedInvoiceLine
            .lineNumber(UPDATED_LINE_NUMBER)
            .quantity(UPDATED_QUANTITY)
            .amountNet(UPDATED_AMOUNT_NET)
            .amountWithTax(UPDATED_AMOUNT_WITH_TAX);

        restInvoiceLineMockMvc.perform(put("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceLine)))
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLine testInvoiceLine = invoiceLineList.get(invoiceLineList.size() - 1);
        assertThat(testInvoiceLine.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testInvoiceLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceLine.getAmountNet()).isEqualTo(UPDATED_AMOUNT_NET);
        assertThat(testInvoiceLine.getAmountWithTax()).isEqualTo(UPDATED_AMOUNT_WITH_TAX);

        // Validate the InvoiceLine in Elasticsearch
        verify(mockInvoiceLineSearchRepository, times(1)).save(testInvoiceLine);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceLine() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restInvoiceLineMockMvc.perform(put("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InvoiceLine in Elasticsearch
        verify(mockInvoiceLineSearchRepository, times(0)).save(invoiceLine);
    }

    @Test
    @Transactional
    public void deleteInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineService.save(invoiceLine);

        int databaseSizeBeforeDelete = invoiceLineRepository.findAll().size();

        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(delete("/api/invoice-lines/{id}", invoiceLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InvoiceLine in Elasticsearch
        verify(mockInvoiceLineSearchRepository, times(1)).deleteById(invoiceLine.getId());
    }

    @Test
    @Transactional
    public void searchInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineService.save(invoiceLine);
        when(mockInvoiceLineSearchRepository.search(queryStringQuery("id:" + invoiceLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(invoiceLine), PageRequest.of(0, 1), 1));
        // Search the invoiceLine
        restInvoiceLineMockMvc.perform(get("/api/_search/invoice-lines?query=id:" + invoiceLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].amountNet").value(hasItem(DEFAULT_AMOUNT_NET.doubleValue())))
            .andExpect(jsonPath("$.[*].amountWithTax").value(hasItem(DEFAULT_AMOUNT_WITH_TAX.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLine.class);
        InvoiceLine invoiceLine1 = new InvoiceLine();
        invoiceLine1.setId(1L);
        InvoiceLine invoiceLine2 = new InvoiceLine();
        invoiceLine2.setId(invoiceLine1.getId());
        assertThat(invoiceLine1).isEqualTo(invoiceLine2);
        invoiceLine2.setId(2L);
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
        invoiceLine1.setId(null);
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
    }
}
