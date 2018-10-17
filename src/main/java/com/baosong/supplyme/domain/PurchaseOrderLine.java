package com.baosong.supplyme.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A PurchaseOrderLine.
 */
@Entity
@Table(name = "purchase_order_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchaseorderline")
public class PurchaseOrderLine implements Serializable {

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

    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("purchaseOrderLines")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("demandStatusChanges")
    private Demand demand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrderLine id(Long id) {
        this.setId(id);
        return this;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public PurchaseOrderLine lineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getQuantity() {
        return quantity;
    }

    public PurchaseOrderLine quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    public PurchaseOrderLine quantityDelivered(Double quantityDelivered) {
        this.setQuantityDelivered(quantityDelivered);
        return this;
    }

    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public PurchaseOrderLine orderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public PurchaseOrderLine purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Demand getDemand() {
        return demand;
    }

    public PurchaseOrderLine demand(Demand demand) {
        this.demand = demand;
        return this;
    }

    public void setDemand(Demand demand) {
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
        PurchaseOrderLine purchaseOrderLine = (PurchaseOrderLine) o;
        if (purchaseOrderLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrderLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrderLine{" +
            "id=" + getId() +
            ", lineNumber=" + getLineNumber() +
            ", quantity=" + getQuantity() +
            ", orderPrice=" + getOrderPrice() +
            "}";
    }
}
