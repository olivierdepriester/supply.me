package com.baosong.supplyme.web.rest.vm;

import javax.validation.constraints.NotNull;

import com.baosong.supplyme.domain.enumeration.DemandStatus;

public class DemandStatusChangeVM {

	@NotNull
	private Long id;

	@NotNull
    private DemandStatus status;

    private String comment;

	public DemandStatusChangeVM() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DemandStatus getStatus() {
		return status;
	}

	public void setStatus(DemandStatus status) {
		this.status = status;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
