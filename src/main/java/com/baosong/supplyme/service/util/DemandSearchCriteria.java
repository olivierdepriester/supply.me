package com.baosong.supplyme.service.util;

import java.time.Instant;
import java.util.List;

import com.baosong.supplyme.domain.enumeration.DemandStatus;

public class DemandSearchCriteria {
    private Long materialId;

    private Long projectId;

    private Long departmentId;

    private List<DemandStatus> demandStatus;

    private Long creationUserId;

    private String query;

    private Instant creationDateFrom;

    private Instant creationDateTo;


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
    public List<DemandStatus> getDemandStatus() {
        return demandStatus;
    }

    /**
     * @param demandStatus the demandStatus to set
     */
    public void setDemandStatus(List<DemandStatus> demandStatus) {
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

    /**
     * @return the departmentId
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the creationDate
     */
    public Instant getCreationDateFrom() {
        return creationDateFrom;
    }

    /**
     * @param creationDateFrom the creationDate to set
     */
    public void setCreationDateFrom(Instant creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    /**
     * @return the creationDateTo
     */
    public Instant getCreationDateTo() {
        return creationDateTo;
    }

    /**
     * @param creationDateTo the creationDateTo to set
     */
    public void setCreationDateTo(Instant creationDateTo) {
        this.creationDateTo = creationDateTo;
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

    public DemandSearchCriteria demandStatus(List<DemandStatus> demandStatus) {
        this.setDemandStatus(demandStatus);
        return this;
    }

    public DemandSearchCriteria query(String query) {
        this.setQuery(query);
        return this;
    }

    public DemandSearchCriteria departmentId(Long departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public DemandSearchCriteria creationDateFrom(Instant creationDate) {
        this.setCreationDateFrom(creationDate);
        return this;
    }

    public DemandSearchCriteria creationDateTo(Instant creationDateTo) {
        this.setCreationDateTo(creationDateTo);
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("DemandSearchCriteria{")
        .append("query:").append(this.query)
        .append(", status:").append(this.demandStatus == null ? "": this.demandStatus.toString())
        .append(", materialId:").append(this.materialId)
        .append(", departmentId:").append(this.departmentId)
        .append(", projectId:").append(this.projectId)
        .append(", creationUserId:").append(this.creationUserId)
        .append(", creationDate: {").append(this.creationDateFrom).append(",").append(this.creationDateTo).append("}")
        .append("}").toString();
    }

}
