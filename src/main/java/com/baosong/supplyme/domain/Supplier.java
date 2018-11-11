package com.baosong.supplyme.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import io.swagger.annotations.ApiModel;

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

    @Max(5)
    @Column(name = "rating", nullable = true)
    private Integer rating;

    @NotNull
    @Column(name = "temporary", nullable = false)
    private Boolean temporary;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private User creationUser;

	public User getCreationUser()
	{
		return this.creationUser;
	}

    public Supplier creationUser(User creationUser) {
        this.setCreationUser(creationUser);
        return this;
    }

	public void setCreationUser(User creationUser)
	{
		this.creationUser = creationUser;
	}

    /**
     * @return the creationDate
     */
    public Instant getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Supplier creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

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

    /**
     * @return the rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * @return the temporary
     */
    public Boolean isTemporary() {
        return temporary;
    }

    /**
     * @param temporary the temporary to set
     */
    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
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
