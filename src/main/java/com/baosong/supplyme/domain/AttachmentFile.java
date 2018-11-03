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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attachment_file")
public class AttachmentFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @NotNull
    @Min(value = 0)
    @Column(name = "size")
    private Long size;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Demand demand;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * @return the demand
     */
    public Demand getDemand() {
        return demand;
    }

    /**
     * @param demand the demand to set
     */
    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttachmentFile af = (AttachmentFile) o;
        if (af.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), af.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentFile{" + "id=" + getId() + ", name=" + getName() + ", size='" + getSize() + "'"
                + ", type='" + getType() + "'" + "}";
    }

}
