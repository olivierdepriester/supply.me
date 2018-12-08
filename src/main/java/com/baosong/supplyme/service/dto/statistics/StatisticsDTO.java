package com.baosong.supplyme.service.dto.statistics;

import java.util.ArrayList;
import java.util.List;

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

    public Serie<T, K> addSerie(T key) {
        Serie<T, K> serie = new Serie<>(key);
        this.series.add(serie);
        return serie;
    }
}
