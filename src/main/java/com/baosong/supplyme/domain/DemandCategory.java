package com.baosong.supplyme.domain;

import com.baosong.supplyme.domain.enumeration.DemandCategoryKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DemandCategory.
 */
@Entity
@Table(name = "demand_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demandcategory")
public class DemandCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "key", nullable = true)
    private DemandCategoryKey key;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private User creationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DemandCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public DemandCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public DemandCategory creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public DemandCategory creationUser(User user) {
        this.creationUser = user;
        return this;
    }

    public void setCreationUser(User user) {
        this.creationUser = user;
    }

    /**
     * @return the key
     */
    public DemandCategoryKey getKey() {
        return key;
    }

    public DemandCategory key(DemandCategoryKey key) {
        this.key = key;
        return this;
    }

    /**
     * @param key the key to set
     */
    public void setKey(DemandCategoryKey key) {
        this.key = key;
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
        DemandCategory demandCategory = (DemandCategory) o;
        if (demandCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demandCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DemandCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
