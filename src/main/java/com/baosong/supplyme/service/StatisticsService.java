package com.baosong.supplyme.service;

import java.time.LocalDateTime;

import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.service.dto.statistics.StatisticsDTO;

/**
 * Service Interface for gathering statistics.
 */
public interface StatisticsService {

    StatisticsDTO<Supplier, LocalDateTime> getPriceEvolutionPerMonth(Long materialId, LocalDateTime startDate, LocalDateTime endDate);

    StatisticsDTO<Supplier, LocalDateTime> getQuantityOrderedPerMonth(Long materialId, LocalDateTime startDate, LocalDateTime endDate);
}
