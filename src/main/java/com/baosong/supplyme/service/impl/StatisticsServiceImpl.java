package com.baosong.supplyme.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import com.baosong.supplyme.domain.Supplier;
import com.baosong.supplyme.domain.enumeration.PurchaseOrderStatus;
import com.baosong.supplyme.repository.search.PurchaseOrderLineSearchRepository;
import com.baosong.supplyme.service.StatisticsService;
import com.baosong.supplyme.service.dto.statistics.DataPoint;
import com.baosong.supplyme.service.dto.statistics.Serie;
import com.baosong.supplyme.service.dto.statistics.StatisticsDTO;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * StatisticsServiceImpl
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository;

    public StatisticsServiceImpl(PurchaseOrderLineSearchRepository purchaseOrderLineSearchRepository) {
        this.purchaseOrderLineSearchRepository = purchaseOrderLineSearchRepository;
    }

    @Override
    public StatisticsDTO<Supplier, LocalDateTime> getPriceEvolutionPerMonth(Long materialId, LocalDateTime startDate,
            LocalDateTime endDate) {
        StatisticsDTO<Supplier, LocalDateTime> result = new StatisticsDTO<>();
        DataPoint<LocalDateTime> point = null;
        Iterable<PurchaseOrderLine> lines = this.getPurchaseOrderLinesUsingMaterial(materialId, startDate, endDate);
        for (PurchaseOrderLine line : lines) {
            point = this.getDataPointForPurchaseOrderLine(result, line, startDate, endDate);
            point.setValue(line.getOrderPrice());
            if (point.getMinValue() == null || point.getValue() < point.getMinValue()) {
                point.setMinValue(point.getValue());
            }
            if (point.getMaxValue() == null || point.getValue() > point.getMaxValue()) {
                point.setMaxValue(point.getValue());
            }
        }
        result.getSeries().forEach(s -> {
            DataPoint<LocalDateTime> previousPoint = null;
            for (DataPoint<LocalDateTime> currentPoint : s.getDataPoints()) {
                if (currentPoint.getValue() == null && previousPoint != null) {
                    currentPoint.setValue(previousPoint.getValue());
                    currentPoint.setMinValue(previousPoint.getMinValue());
                    currentPoint.setMaxValue(previousPoint.getMaxValue());
                } else if (currentPoint.getValue() != null) {
                    previousPoint = currentPoint;
                }
            }
        });
        return result;

        // // For each serie, fill each missing value with the previous value. (so that
        // the graphe has no nole)
        // for (SerieDTO<Supplier> serie : result.getSeries()) {
        // Double previousValue = null;
        // int index = 0;
        // for (Double value : serie.getValues()) {
        // if (value == null && previousValue != null) {
        // serie.getValues().set(index, previousValue);
        // } else if (value != null) {
        // previousValue = value;
        // }
        // index++;
        // }
        // }
    }

    private LocalDateTime truncateInstantToMonth(Instant instant) {
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        return result.minus(result.getDayOfMonth() - 1, ChronoUnit.DAYS);
    }

    @Override
    public StatisticsDTO<Supplier, LocalDateTime> getQuantityOrderedPerMonth(Long materialId, LocalDateTime startDate, LocalDateTime endDate) {
        StatisticsDTO<Supplier, LocalDateTime> result = new StatisticsDTO<>();
        DataPoint<LocalDateTime> point = null;
        Iterable<PurchaseOrderLine> lines = this.getPurchaseOrderLinesUsingMaterial(materialId, startDate, endDate);
        for (PurchaseOrderLine line : lines) {
            point = this.getDataPointForPurchaseOrderLine(result, line, startDate, endDate);
            point.setValue((point.getValue() == null ? 0 : point.getValue()) + line.getQuantity());
        }
        result.getSeries().forEach(s -> {
            s.getDataPoints().sort(new Comparator<DataPoint<LocalDateTime>>() {

                @Override
                public int compare(DataPoint<LocalDateTime> o1, DataPoint<LocalDateTime> o2) {
                    return o1.getName().compareTo(o2.getName());
                }

            });
        });
        return result;
    }

    private DataPoint<LocalDateTime> getDataPointForPurchaseOrderLine(
            StatisticsDTO<Supplier, LocalDateTime> statisticsDTO, PurchaseOrderLine line, LocalDateTime startDate,
            LocalDateTime endDate) {
        Serie<Supplier, LocalDateTime> serie = null;
        Supplier supplier = line.getPurchaseOrder().getSupplier();
        LocalDateTime month = truncateInstantToMonth(line.getPurchaseOrder().getCreationDate());
        Optional<Serie<Supplier, LocalDateTime>> optionalSerie = statisticsDTO.getSeries().stream()
                .filter(s -> s.getKey().equals(supplier)).findAny();
        if (optionalSerie.isPresent()) {
            serie = optionalSerie.get();
        } else {
            serie = statisticsDTO.addSerie(supplier);
            for (LocalDateTime currentDateTime = startDate; currentDateTime
                    .isBefore(endDate); currentDateTime = currentDateTime.plusMonths(1)) {
                serie.getDataPoints().add(DataPoint.of(currentDateTime));
            }
        }
        Optional<DataPoint<LocalDateTime>> optionalPoint = serie.getDataPoints().stream()
                .filter(dp -> dp.getName().equals(month)).findAny();
        if (optionalPoint.isPresent()) {
            return optionalPoint.get();
        } else {
            return serie.addPoint(DataPoint.of(month, 0D, false));
        }
    }

    private Iterable<PurchaseOrderLine> getPurchaseOrderLinesUsingMaterial(Long materialId, LocalDateTime startDate,
            LocalDateTime endDate) {
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("purchaseOrder.status", PurchaseOrderStatus.SENT.toString()))
                .must(QueryBuilders.termQuery("demand.material.id", materialId));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("purchaseOrder.creationDate")
                .gte(startDate.toString()).lt(endDate.toString());
        booleanQueryBuilder.must(rangeQueryBuilder);
        return purchaseOrderLineSearchRepository.search(booleanQueryBuilder);
    }
}
