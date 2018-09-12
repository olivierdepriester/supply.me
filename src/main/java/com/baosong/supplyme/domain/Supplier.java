package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Supplier
 */
@ApiModel(description = "Supplier")
@Entity
@Table(name = "supplier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "supplier")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "reference_number", length = 10, nullable = false)
    private String referenceNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MaterialAvailability> names = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public Supplier referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getName() {
        return name;
    }

    public Supplier name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MaterialAvailability> getNames() {
        return names;
    }

    public Supplier names(Set<MaterialAvailability> materialAvailabilities) {
        this.names = materialAvailabilities;
        return this;
    }

    public Supplier addName(MaterialAvailability materialAvailability) {
        this.names.add(materialAvailability);
        materialAvailability.setSupplier(this);
        return this;
    }

    public Supplier removeName(MaterialAvailability materialAvailability) {
        this.names.remove(materialAvailability);
        materialAvailability.setSupplier(null);
        return this;
    }

    public void setNames(Set<MaterialAvailability> materialAvailabilities) {
        this.names = materialAvailabilities;
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
        Supplier supplier = (Supplier) o;
        if (supplier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), supplier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
