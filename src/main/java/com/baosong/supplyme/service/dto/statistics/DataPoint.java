package com.baosong.supplyme.service.dto.statistics;

public class DataPoint<T> {

    private T name;

    private Double value;

    private Double minValue;

    private Double maxValue;

    public static <T> DataPoint<T> of(T name, Double value, boolean initMinMax) {
        DataPoint<T> dataPoint = new DataPoint<>();
        dataPoint.setName(name);
        dataPoint.setValue(value);
        if (initMinMax) {
            dataPoint.setMinValue(value);
            dataPoint.setMaxValue(value);
        }
        return dataPoint;
    }

    /**
     * @return the name
     */
    public T getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(T name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * @return the minValue
     */
    public Double getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maxValue
     */
    public Double getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
}
