package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.baosong.supplyme.domain.enumeration.DemandStatus;

/**
 * Demand status change record.
 */
@Entity
@Table(name = "demand_status_change")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DemandStatusChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DemandStatus status;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "jhi_comment")
    private String comment;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("")
    private User creationUser;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("demandStatusChanges")
    private Demand demand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public DemandStatusChange(Demand demand, DemandStatus status, User creationUser) {
        this.setDemand(demand);
        this.setStatus(status);
        this.setCreationDate(Instant.now());
        this.setCreationUser(creationUser);
    }

    public DemandStatusChange() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public DemandStatus getStatus() {
        return status;
    }

    public DemandStatusChange status(DemandStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public DemandStatusChange creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public DemandStatusChange creationUser(User user) {
        this.creationUser = user;
        return this;
    }

    public void setCreationUser(User user) {
        this.creationUser = user;
    }

    public String getComment()
	{
		return this.comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Demand getDemand()
	{
		return this.demand;
	}

	public void setDemand(Demand demand)
	{
		this.demand = demand;
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
        DemandStatusChange demand = (DemandStatusChange) o;
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
        return "DemandStatusChange {" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
