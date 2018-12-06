package com.baosong.supplyme.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.StatisticsService;
import com.baosong.supplyme.service.dto.statistics.DataPoint;
import com.baosong.supplyme.service.dto.statistics.Serie;
import com.baosong.supplyme.service.dto.statistics.StatisticsDTO;
import com.baosong.supplyme.service.util.DemandSearchCriteria;
import com.baosong.supplyme.service.util.InstantComparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * StatisticsServiceImpl
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private DemandService demandService;

    @Override
    public StatisticsDTO<Supplier, LocalDateTime> getAveragePriceEvolutionByMonth(Long materialId) {
        StatisticsDTO<Supplier, LocalDateTime> result = new StatisticsDTO<Supplier, LocalDateTime>();
        PageRequest pageable = PageRequest.of(0, 10000, Sort.Direction.ASC, "supplier.id", "creationDate");
        List<Demand> demands = this.demandService.search(new DemandSearchCriteria().materialId(materialId), pageable)
                    .stream().filter(d -> d.getSupplier() != null)
                    .collect(Collectors.toList());
        result.setSeries(demands.stream().map(Demand::getSupplier).distinct().map(s -> new Serie<Supplier, LocalDateTime>(s)).collect(Collectors.toList()));
        // Get the 1st month
        LocalDateTime minMonth = truncateInstantToMonth(demands.stream().map(Demand::getCreationDate).min(new InstantComparator()).get());
        // Get the last month
        LocalDateTime maxMonth = LocalDateTime.ofInstant(demands.stream().map(Demand::getCreationDate).max(new InstantComparator()).get(), ZoneId.systemDefault()).plusMonths(1);
        // Fill all the months range
        for (LocalDateTime i = minMonth; i.isBefore(maxMonth); i = i.plusMonths(1)) {
        }

        // Read the prices from the purchase requests
        for (Demand demand : demands) {
            Serie<Supplier, LocalDateTime> serie = result.getSeries().stream().filter(s -> s.getKey().equals(demand.getSupplier())).findAny().get();
            LocalDateTime month = truncateInstantToMonth(demand.getCreationDate());
            Optional<DataPoint<LocalDateTime>> dataPoint = serie.getDataPoints().stream().filter(dp -> dp.getName().equals(month)).findFirst();
            if (dataPoint.isPresent()) {
                dataPoint.ifPresent(dp -> {
                    dp.setValue(demand.getEstimatedPrice());
                    if (dp.getValue() < dp.getMinValue()) {
                        dp.setMinValue(dp.getValue());
                    } else if (dp.getValue() > dp.getMaxValue()) {
                        dp.setMaxValue(dp.getValue());
                    }
                });
            } else {
                serie.getDataPoints().add(DataPoint.of(month, demand.getEstimatedPrice(), true));
            }
        }
        // // For each serie, fill each missing value with the previous value. (so that the graphe has no nole)
        // for (SerieDTO<Supplier> serie : result.getSeries()) {
        //     Double previousValue = null;
        //     int index = 0;
        //     for (Double value : serie.getValues()) {
        //         if (value == null && previousValue != null) {
        //             serie.getValues().set(index, previousValue);
        //         } else if (value != null) {
        //             previousValue = value;
        //         }
        //         index++;
        //     }
        // }
        return result;
    }

    private LocalDateTime truncateInstantToMonth(Instant instant) {
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        result = result.minus(result.getDayOfMonth() - 1, ChronoUnit.DAYS);
        return result;
    }
}
