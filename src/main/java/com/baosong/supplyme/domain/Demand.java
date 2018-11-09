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
import javax.validation.constraints.Size;

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

    @Column(name = "code", length = 12, nullable = false)
    private String code;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "estimated_price", nullable = true)
    private Double estimatedPrice;

    @Column(name = "quantity_ordered", nullable = false)
    private Double quantityOrdered;

    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;

    @Column(name = "description", nullable = true, columnDefinition = "CLOB")
    private String description;

    @Column(name="validation_authority", nullable = true, columnDefinition="VARCHAR(50)")
    private String validationAuthority;

    @Column(name="reached_authority", nullable = true, columnDefinition="VARCHAR(50)")
    private String reachedAuthority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DemandStatus status;

    @Column(name = "expected_date")
    private Instant expectedDate;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(name = "urgent", nullable = false)
    private Boolean urgent;

    @Column(name = "use_annual_budget", nullable = true)
    private Boolean useAnnualBudget;

    @Column(name = "planned", nullable = true)
    private Boolean planned;

    @Size(max = 50)
    @Column(name = "unit", nullable = true)
    private String unit;

    @Size(max = 50)
    @Column(name = "where_use", nullable = true)
    private String whereUse;

    @Column(name = "vat", nullable = true)
    private Double vat;

    @NotNull
    @ManyToOne(optional = false)
    private Material material;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("creationUser")
    private Department department;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("creationUser")
    private Project project;

    @ManyToOne(optional = true)
    @JsonIgnoreProperties("creationUser")
    private Supplier supplier;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("creationUser")
    private DemandCategory demandCategory;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private User creationUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "demand", cascade = CascadeType.ALL)
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

    public String getCode() {
        return this.code;
    }

    public Demand code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
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

    /**
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    public Demand department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public String getValidationAuthority() {
        return this.validationAuthority;
    }

    public Demand validationAuthority(String validationAuthority) {
        this.validationAuthority = validationAuthority;
        return this;
    }

    public void setValidationAuthority(String validationAuthority) {
        this.validationAuthority = validationAuthority;
    }

    public String getReachedAuthority() {
        return this.reachedAuthority;
    }

    public Boolean isUrgent() {
        return this.urgent;
    }

    public Demand urgent(Boolean urgent) {
        this.setUrgent(urgent);
        return this;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }


	public Boolean isUseAnnualBudget() {
        return this.useAnnualBudget;
    }

    public Demand useAnnualBudget(Boolean useAnnualBudget) {
        this.setUseAnnualBudget(useAnnualBudget);
        return this;
    }

    public void setUseAnnualBudget(Boolean useAnnualBudget) {
        this.useAnnualBudget = useAnnualBudget;
    }


    public Boolean isPlanned() {
        return this.planned;
    }

    public Demand planned(Boolean planned) {
        this.setPlanned(planned);
        return this;
    }

    public void setPlanned(Boolean planned) {
        this.planned = planned;
    }

    public String getUnit() {
        return this.unit;
    }

    public Demand unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWhereUse() {
        return this.whereUse;
    }

    public Demand whereUse(String whereUse) {
        this.setWhereUse(whereUse);
        return this;
    }

    public void setWhereUse(String whereUse) {
        this.whereUse = whereUse;
    }

    /**
     * Get the VAT as a decimal : 16% = 0.16
     *
     * @return the VAT
     */
    public Double getVat() {
        return this.vat;
    }

    /**
     * Set the VAT.
     *
     * @param the VAT to set as a decimal value.
     * @return the demand.
     */
    public Demand vat(Double vat) {
        this.setVat(vat);
        return this;
    }

    /**
     * Set the VAT
     *
     * @param the VAT to set as a decimal value
     */
    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Demand reachedAuthority(String reachedAuthority) {
        this.setReachedAuthority(reachedAuthority);
        return this;
    }

    public void setReachedAuthority(String reachedAuthority) {
        this.reachedAuthority = reachedAuthority;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Demand supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Demand description(String description) {
        this.setDescription(description);
        return this;
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
     * Get the unit estimated price of the material for this demand.
     *
     * @return
     */
	public Double getEstimatedPrice() {
        return this.estimatedPrice;
    }

    /**
     * Set the estimated price of the material for this demand.
     *
     * @param estimatedPrice
     */
    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    /**
     * Set the estimated price of the material for this demand.
     *
     * @param estimatedPrice
     * @return the demand.
     */
    public Demand estimatedPrice(Double estimatedPrice) {
        this.setEstimatedPrice(estimatedPrice);
        return this;
    }

    public DemandCategory getDemandCategory() {
        return this.demandCategory;
    }

    public Demand category(DemandCategory demandCategory) {
        this.setDemandCategory(demandCategory);
        return this;
    }

    public void setDemandCategory(DemandCategory demandCategory) {
        this.demandCategory = demandCategory;
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

    /**
     * Get unit estimated price with VAT.
     *
     * @return null if no
     */
    public Double getEstimatedPriceWithVat() {
        if (this.getEstimatedPrice() != null && this.getVat() != null) {
            return this.getEstimatedPrice() * (1 + this.getVat());
        } else {
            return null;
        }
    }

    public Double getAmountWithVat() {
        Double estimatedPriceWithVat = this.getEstimatedPriceWithVat();
        if (estimatedPriceWithVat != null) {
            return estimatedPriceWithVat * this.getQuantity();
        } else {
            return null;
        }
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
