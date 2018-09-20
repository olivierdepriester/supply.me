package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.baosong.supplyme.domain.enumeration.PurchaseOrderStatus;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchaseorder")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 10, max = 10)
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @Column(name = "expected_date")
    private Instant expectedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PurchaseOrderStatus status;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Supplier supplier;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User creationUser;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "purchaseOrder")
    List<PurchaseOrderLine> purchaseOrderLines;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public PurchaseOrder code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getExpectedDate() {
        return expectedDate;
    }

    public PurchaseOrder expectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
        return this;
    }

    public void setExpectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public PurchaseOrder status(PurchaseOrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public PurchaseOrder creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public PurchaseOrder supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public PurchaseOrder creationUser(User user) {
        this.creationUser = user;
        return this;
    }

    public void setCreationUser(User user) {
        this.creationUser = user;
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
        PurchaseOrder purchaseOrder = (PurchaseOrder) o;
        if (purchaseOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", expectedDate='" + getExpectedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
