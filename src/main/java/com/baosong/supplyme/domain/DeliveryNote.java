package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.baosong.supplyme.domain.enumeration.DeliveryNoteStatus;

/**
 * A DeliveryNote.
 */
@Entity
@Table(name = "delivery_note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deliverynote")
public class DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "delivery_date", nullable = false)
    private Instant deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryNoteStatus status;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Supplier supplier;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User creationUser;

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

    public DeliveryNote code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public DeliveryNote deliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliveryNoteStatus getStatus() {
        return status;
    }

    public DeliveryNote status(DeliveryNoteStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DeliveryNoteStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public DeliveryNote creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public DeliveryNote supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public DeliveryNote creationUser(User user) {
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
        DeliveryNote deliveryNote = (DeliveryNote) o;
        if (deliveryNote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deliveryNote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeliveryNote{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
