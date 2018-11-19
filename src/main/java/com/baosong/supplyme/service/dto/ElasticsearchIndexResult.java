package com.baosong.supplyme.service.dto;

import javax.validation.constraints.NotBlank;

/**
 * A DTO representing an attachment file.
 */
public class ElasticsearchIndexResult {

    @NotBlank
    private String className;

    private Integer count;

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    public ElasticsearchIndexResult(String className, Integer count) {
        this.setClassName(className);
        this.setCount(count);
    }

    public static ElasticsearchIndexResult of(String className, Integer count) {
        return new ElasticsearchIndexResult(className, count);
    }

    @Override
    public String toString() {
        return "ElasticsearchIndexResult {" + "className='" + this.className + '\'' + ",count='" + this.count + '\''
                + "}";
    }
}
