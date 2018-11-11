import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IDepartment } from 'app/shared/model/department.model';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { DepartmentService } from './department.service';

@Component({
    selector: 'jhi-department-update',
    templateUrl: './department-update.component.html'
})
export class DepartmentUpdateComponent implements OnInit {
    private _department: IDepartment;
    isSaving: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private departmentService: DepartmentService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ department }) => {
            this.department = department;
            if (!department.id) {
                this.department.activated = true;
            }
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.department.id !== undefined) {
            this.subscribeToSaveResponse(this.departmentService.update(this.department));
        } else {
            this.subscribeToSaveResponse(this.departmentService.create(this.department));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>) {
        result.subscribe((res: HttpResponse<IDepartment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get department() {
        return this._department;
    }

    set department(department: IDepartment) {
        this._department = department;
    }
}
