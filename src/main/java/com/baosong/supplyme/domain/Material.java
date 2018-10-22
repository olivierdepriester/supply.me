package com.baosong.supplyme.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Locale.Category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Material.
 */
@Entity
@Table(name = "material")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "part_number", length = 10, nullable = false)
    private String partNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "CLOB")
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "temporary", nullable = false)
    private Boolean temporary;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private User creationUser;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private MaterialCategory materialCategory;

    @OneToMany(mappedBy = "material")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MaterialAvailability> codes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material id(Long id) {
        this.setId(id);
        return this;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public Material partNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getName() {
        return name;
    }

    public Material name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Material description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Material creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreationUser() {
        return this.creationUser;
    }

    public Material creationUser(User creationUser) {
        this.setCreationUser(creationUser);
        return this;
    }

    public void setCreationUser(User creationUser) {
        this.creationUser = creationUser;
    }


    public Set<MaterialAvailability> getCodes() {
        return codes;
    }

    public Material codes(Set<MaterialAvailability> materialAvailabilities) {
        this.codes = materialAvailabilities;
        return this;
    }

    public Material addCode(MaterialAvailability materialAvailability) {
        this.codes.add(materialAvailability);
        materialAvailability.setMaterial(this);
        return this;
    }

    public Material removeCode(MaterialAvailability materialAvailability) {
        this.codes.remove(materialAvailability);
        materialAvailability.setMaterial(null);
        return this;
    }

    public void setCodes(Set<MaterialAvailability> materialAvailabilities) {
        this.codes = materialAvailabilities;
    }

    public Boolean isTemporary() {
        return this.temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public Material temporary(Boolean temporary) {
        this.setTemporary(temporary);
        return this;
    }

	public MaterialCategory getMaterialCategory()
	{
		return this.materialCategory;
	}

	public void setMaterialCategory(MaterialCategory materialCategory)
	{
		this.materialCategory = materialCategory;
    }

    public Material materialCategory(MaterialCategory materialCategory) {
        this.setMaterialCategory(materialCategory);
        return this;
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
        Material material = (Material) o;
        if (material.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), material.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Material{" + "id=" + getId() + ", partNumber='" + getPartNumber() + "'" + ", name='" + getName() + "'"
                + ", description='" + getDescription() + "'" + ", creationDate='" + getCreationDate() + "'" + "}";
    }
}
