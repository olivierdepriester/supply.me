package com.baosong.supplyme.web.rest;

import java.time.LocalDateTime;

import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.service.StatisticsService;
import com.baosong.supplyme.service.dto.statistics.StatisticsDTO;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private final StatisticsService statisticsService;

    public StatisticsResource(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * GET /invoices : get all the invoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in
     *         body
     */
    @GetMapping("/statistics/material/prices-by-month")
    @Timed
    public ResponseEntity<StatisticsDTO<Supplier, LocalDateTime>> getMaterialPriceEvolutionByMonth(Long materialId) {
        log.debug("REST request to get a page of MaterialPriceEvolutionByMonth");
        StatisticsDTO<Supplier, LocalDateTime> result = this.statisticsService.getAveragePriceEvolutionByMonth(materialId);
        return ResponseEntity.ok().body(result);
    }
}
