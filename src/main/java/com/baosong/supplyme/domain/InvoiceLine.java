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
 * A InvoiceLine.
 */
@Entity
@Table(name = "invoice_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoiceline")
public class InvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount_net", nullable = false)
    private Double amountNet;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount_with_tax", nullable = false)
    private Double amountWithTax;

    @ManyToOne
    @JsonIgnoreProperties("")
    private PurchaseOrder purchaseOrder;

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

    public InvoiceLine lineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getQuantity() {
        return quantity;
    }

    public InvoiceLine quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAmountNet() {
        return amountNet;
    }

    public InvoiceLine amountNet(Double amountNet) {
        this.amountNet = amountNet;
        return this;
    }

    public void setAmountNet(Double amountNet) {
        this.amountNet = amountNet;
    }

    public Double getAmountWithTax() {
        return amountWithTax;
    }

    public InvoiceLine amountWithTax(Double amountWithTax) {
        this.amountWithTax = amountWithTax;
        return this;
    }

    public void setAmountWithTax(Double amountWithTax) {
        this.amountWithTax = amountWithTax;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public InvoiceLine purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Material getMaterial() {
        return material;
    }

    public InvoiceLine material(Material material) {
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
        InvoiceLine invoiceLine = (InvoiceLine) o;
        if (invoiceLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceLine{" +
            "id=" + getId() +
            ", lineNumber=" + getLineNumber() +
            ", quantity=" + getQuantity() +
            ", amountNet=" + getAmountNet() +
            ", amountWithTax=" + getAmountWithTax() +
            "}";
    }
}
