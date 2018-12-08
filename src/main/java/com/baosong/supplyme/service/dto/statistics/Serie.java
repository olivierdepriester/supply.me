package com.baosong.supplyme.service.dto.statistics;

import java.util.ArrayList;
import java.util.List;

public class Serie<T, K> {

    private T key;

    private List<DataPoint<K>> dataPoints;

    public Serie() {
        super();
        this.dataPoints = new ArrayList<>();
    }

    public Serie(T key) {
        this();
        this.key = key;
    }

    /**
     * @return the key
     */
    public T getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(T key) {
        this.key = key;
    }

    /**
     * @return the dataPoints
     */
    public List<DataPoint<K>> getDataPoints() {
        return dataPoints;
    }

    /**
     * @param dataPoints the dataPoints to set
     */
    public void setDataPoints(List<DataPoint<K>> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public DataPoint<K> addPoint(K name) {
        DataPoint<K> point = DataPoint.of(name);
        this.dataPoints.add(point);
        return point;
    }

    public DataPoint<K> addPoint(DataPoint<K> dataPoint) {
        this.dataPoints.add(dataPoint);
        return dataPoint;
    }
}
