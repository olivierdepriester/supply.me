package com.baosong.supplyme.service.dto.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store and transfer data for statistics charts
 * The class consists in a list of series of {@code <T>}.
 * Each serie consists in a list of {@code DataPoints} within the x axis is made of {@code <K>}
 * @param <T> : Key of the series
 * @param <K> : Key of the data points (X axis)
 * @see DataPoint
 *
 */
public class StatisticsDTO<T, K> {

    private List<Serie<T, K>> series = new ArrayList<>();

    /**
     * @return the series
     */
    public List<Serie<T, K>> getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(List<Serie<T, K>> series) {
        this.series = series;
    }

    /**
     * Add a serie to the statistics.
     * @param key Key of the serie
     * @return the newly created serie
     */
    public Serie<T, K> addSerie(T key) {
        Serie<T, K> serie = new Serie<>(key);
        this.series.add(serie);
        return serie;
    }
}
