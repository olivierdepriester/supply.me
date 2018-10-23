package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Define if a material is a available for a supplier
 */
@ApiModel(description = "Define if a material is a available for a supplier")
@Entity
@Table(name = "material_availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "materialavailability")
public class MaterialAvailability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @DecimalMin(value = "0")
    @Column(name = "purchase_price")
    private Double purchasePrice;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("codes")
    private Material material;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("names")
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public MaterialAvailability creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getUpdateDate() {
        return this.updateDate;
    }

    public MaterialAvailability updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public MaterialAvailability purchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Material getMaterial() {
        return material;
    }

    public MaterialAvailability material(Material material) {
        this.material = material;
        return this;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public MaterialAvailability supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialAvailability materialAvailability = (MaterialAvailability) o;
        if (materialAvailability.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialAvailability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialAvailability{" +
            "id=" + getId() +
            ", startDate='" + getCreationDate() + "'" +
            ", endDate='" + getUpdateDate() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            "}";
    }
}
