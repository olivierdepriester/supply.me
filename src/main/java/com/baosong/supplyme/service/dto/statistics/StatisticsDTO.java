package com.baosong.supplyme.service.dto.statistics;

import java.util.List;

public class StatisticsDTO<T, K> {

    private List<Serie<T, K>> series;

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
}
