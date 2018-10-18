package com.baosong.supplyme.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.baosong.supplyme.domain.enumeration.DemandStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Demand.
 */
@Entity
@Table(name = "demand")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demand")
public class Demand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "quantity_ordered", nullable = false)
    private Double quantityOrdered;

    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DemandStatus status;

    @Column(name = "expected_date")
    private Instant expectedDate;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne(optional = false)
    private Material material;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User creationUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "demand", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("creationDate desc")
    @JsonIgnoreProperties("demand")
    private List<DemandStatusChange> demandStatusChanges;

    public List<DemandStatusChange> getDemandStatusChanges() {
        return this.demandStatusChanges;
    }

    public void setDemandStatusChanges(List<DemandStatusChange> demandStatusChanges) {
        this.demandStatusChanges = demandStatusChanges;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Demand quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public DemandStatus getStatus() {
        return status;
    }

    public Demand status(DemandStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public Instant getExpectedDate() {
        return expectedDate;
    }

    public Demand expectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
        return this;
    }

    public void setExpectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Demand creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Material getMaterial() {
        return material;
    }

    public Demand material(Material material) {
        this.material = material;
        return this;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Project getProject() {
        return project;
    }

    public Demand project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public Demand creationUser(User user) {
        this.creationUser = user;
        return this;
    }

    public void setCreationUser(User user) {
        this.creationUser = user;
    }

    /**
     * @return the quantityDelivered
     */
    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    /**
     * @return the quantityOrdered
     */
    public Double getQuantityOrdered() {
        return quantityOrdered;
    }

    /**
     * @param quantityOrdered the quantityOrdered to set
     */
    public void setQuantityOrdered(Double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    /**
     * @param quantityDelivered the delivered quantity to set
     */
    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    /**
     *
     * @param quantityDelivered the delivered quantity to set
     * @return the demand
     */
    public Demand quantityDelivered(Double quantityDelivered) {
        this.setQuantityDelivered(quantityDelivered);
        return this;
    }

    /**
     *
     * @param quantityOrdered the ordered quantity to set
     * @return the demand
     */
    public Demand quantityOrdered(Double quantityOrdered) {
        this.setQuantityOrdered(quantityOrdered);
        return this;
    }

    /**
     * Gets if the demand can be added to a purchase order
     *
     * @return
     */
    public boolean getCanBePurchased() {
        // The demand must be approved or ordered and not fully fulfilled by an existing
        // order
        return (this.getQuantityOrdered() == null || this.getQuantityOrdered() < this.getQuantity())
                && (DemandStatus.APPROVED.equals(this.status) || DemandStatus.ORDERED.equals(this.status));
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Demand demand = (Demand) o;
        if (demand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Demand{" + "id=" + getId() + ", quantity=" + getQuantity() + ", status='" + getStatus() + "'"
                + ", expectedDate='" + getExpectedDate() + "'" + ", creationDate='" + getCreationDate() + "'" + "}";
    }
}
