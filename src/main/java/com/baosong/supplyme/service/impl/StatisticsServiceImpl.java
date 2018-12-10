package com.baosong.supplyme.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
        // Get the purchase order lines using this material
        Iterable<PurchaseOrderLine> lines = this.getPurchaseOrderLinesUsingMaterial(materialId, startDate, endDate);
        // Read the lines
        for (PurchaseOrderLine line : lines) {
            // Initialize the whole serie for supplier and the data point for the current month
            point = this.getDataPointForPurchaseOrderLine(result, line, startDate, endDate);
            // Set the data point value
            point.setValue(line.getOrderPrice());
            // Set the min price for this month
            if (point.getMinValue() == null || point.getValue() < point.getMinValue()) {
                point.setMinValue(point.getValue());
            }
            // Set the max price for this month
            if (point.getMaxValue() == null || point.getValue() > point.getMaxValue()) {
                point.setMaxValue(point.getValue());
            }
        }
        // Fill in the holes for each series. A hole is filled by copying the value of the previous point
        result.getSeries().forEach(s -> {
            DataPoint<LocalDateTime> previousPoint = null;
            for (DataPoint<LocalDateTime> currentPoint : s.getDataPoints()) {
                // No value and previous value exists -> Copy previous value
                if (currentPoint.getValue() == null && previousPoint != null) {
                    currentPoint.setValue(previousPoint.getValue());
                    currentPoint.setMinValue(previousPoint.getMinValue());
                    currentPoint.setMaxValue(previousPoint.getMaxValue());
                }
                // Current point become previous point
                previousPoint = currentPoint;
            }
        });
        return result;
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
        return result;
    }

    /**
     * Get the 1st day of the month from an instant
     * @param instant
     * @return
     */
    private LocalDateTime truncateInstantToMonth(Instant instant) {
        LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        return result.minus(result.getDayOfMonth() - 1, ChronoUnit.DAYS);
    }

    /**
     * Initialize and/or get a datapoint matching a purchase order line
     * This method do the following things :
     * - If not exists yet, initialize the serie related to the purchase order supplier
     * - If not exists yet, initialize the data point in this serie for the purchase order creation month
     * - Get the matching datapoint
     * @param statisticsDTO Container storing all the series
     * @param line Purchase order line
     * @param startDate
     * @param endDate
     * @return
     */
    private DataPoint<LocalDateTime> getDataPointForPurchaseOrderLine(
            StatisticsDTO<Supplier, LocalDateTime> statisticsDTO, PurchaseOrderLine line, LocalDateTime startDate,
            LocalDateTime endDate) {
        Serie<Supplier, LocalDateTime> serie = null;
        Supplier supplier = line.getPurchaseOrder().getSupplier();
        // Get the serie
        Optional<Serie<Supplier, LocalDateTime>> optionalSerie = statisticsDTO.getSeries().stream()
                .filter(s -> s.getKey().equals(supplier)).findAny();
        if (optionalSerie.isPresent()) {
            serie = optionalSerie.get();
        } else {
            serie = statisticsDTO.addSerie(supplier);
            // Initialize all the datapoints for in the serie for the period
            for (LocalDateTime currentDateTime = startDate; currentDateTime
                    .isBefore(endDate); currentDateTime = currentDateTime.plusMonths(1)) {
                serie.getDataPoints().add(DataPoint.of(currentDateTime));
            }
        }
        LocalDateTime month = truncateInstantToMonth(line.getPurchaseOrder().getCreationDate());
        // Get the data point for this purchase order month
        Optional<DataPoint<LocalDateTime>> optionalPoint = serie.getDataPoints().stream()
                .filter(dp -> dp.getName().equals(month)).findAny();
        if (optionalPoint.isPresent()) {
            return optionalPoint.get();
        } else {
            // Should never be here as all the datapoints have already been initialized above
            return serie.addPoint(DataPoint.of(month, 0D, false));
        }
    }

    /**
     * Get the lines of SENT purchase order created in a given interval using a given material
     *
     * @param materialId Material identifier
     * @param startDate Purchaser order creation period starting date
     * @param endDate Purchaser order creation period end date
     * @return
     */
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
