package com.baosong.supplyme.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DeliveryNoteLine.
 */
@Entity
@Table(name = "delivery_note_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deliverynoteline")
public class DeliveryNoteLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @ManyToOne
    @JsonIgnoreProperties("deliveryNoteLines")
    private DeliveryNote deliveryNote;

    @ManyToOne
    @JsonIgnoreProperties("")
    private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public DeliveryNoteLine lineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getQuantity() {
        return quantity;
    }

    public DeliveryNoteLine quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public DeliveryNoteLine deliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
        return this;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public PurchaseOrderLine getPurchaseOrderLine() {
        return purchaseOrderLine;
    }

    public DeliveryNoteLine purchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        this.purchaseOrderLine = purchaseOrderLine;
        return this;
    }

    public void setPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        this.purchaseOrderLine = purchaseOrderLine;
    }

    public Material getMaterial() {
        return material;
    }

    public DeliveryNoteLine material(Material material) {
        this.material = material;
        return this;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
        DeliveryNoteLine deliveryNoteLine = (DeliveryNoteLine) o;
        if (deliveryNoteLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deliveryNoteLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeliveryNoteLine{" +
            "id=" + getId() +
            ", lineNumber=" + getLineNumber() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
