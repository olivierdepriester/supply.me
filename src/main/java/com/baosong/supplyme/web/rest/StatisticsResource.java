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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/statistics/material/{id}/price-per-month")
    @Timed
    public ResponseEntity<StatisticsDTO<Supplier, LocalDateTime>> getMaterialPriceEvolutionPerMonth(@PathVariable Long id) {
        log.debug("REST request to get stastistics : Price evolution per month for material {}", id);
        LocalDateTime endDate = LocalDateTime.of(
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonth(),
            1, 0, 0, 0
        ).plusMonths(1);
        LocalDateTime startDate = endDate.minusMonths(13);
        StatisticsDTO<Supplier, LocalDateTime> result = this.statisticsService.getPriceEvolutionPerMonth(id, startDate, endDate);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/statistics/material/{id}/quantity-per-month")
    @Timed
    public ResponseEntity<StatisticsDTO<Supplier, LocalDateTime>> getMaterialQuantityPerMonth(@PathVariable Long id) {
        log.debug("REST request to get stastistics : Quantity ordered per month for material {}", id);
        LocalDateTime endDate = LocalDateTime.of(
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonth(),
            1, 0, 0, 0
        ).plusMonths(1);
        LocalDateTime startDate = endDate.minusMonths(13);
        StatisticsDTO<Supplier, LocalDateTime> result = this.statisticsService.getQuantityOrderedPerMonth(id, startDate, endDate);
        return ResponseEntity.ok().body(result);
    }

}
