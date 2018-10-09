package com.baosong.supplyme.service.util;

import com.baosong.supplyme.domain.enumeration.DemandStatus;

public class DemandSearchCriteria {
    private Long materialId;

    private Long projectId;

    private DemandStatus demandStatus;

    private Long creationUserId;

    private String query;


    /**
     * @return the materialId
     */
    public Long getMaterialId() {
        return materialId;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the creationUserId
     */
    public Long getCreationUserId() {
        return creationUserId;
    }

    /**
     * @param creationUserId the creationUserId to set
     */
    public void setCreationUserId(Long creationUserId) {
        this.creationUserId = creationUserId;
    }

    /**
     * @return the demandStatus
     */
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    /**
     * @param demandStatus the demandStatus to set
     */
    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    /**
     * @return the projectId
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public DemandSearchCriteria creationUserId(Long creationUserId) {
        this.setCreationUserId(creationUserId);
        return this;
    }

    public DemandSearchCriteria materialId(Long materialId) {
        this.setMaterialId(materialId);
        return this;
    }

    public DemandSearchCriteria projectId(Long projectId) {
        this.setProjectId(projectId);
        return this;
    }

    public DemandSearchCriteria demandStatus(DemandStatus demandStatus) {
        this.setDemandStatus(demandStatus);
        return this;
    }

    public DemandSearchCriteria query(String query) {
        this.setQuery(query);
        return this;
    }

}
