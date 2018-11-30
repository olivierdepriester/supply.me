import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialAvailability } from 'app/shared/model/material-availability.model';
import { MaterialAvailabilityService } from './material-availability.service';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';

@Component({
    selector: 'jhi-material-availability-update',
    templateUrl: './material-availability-update.component.html'
})
export class MaterialAvailabilityUpdateComponent implements OnInit {
    materialAvailability: IMaterialAvailability;
    isSaving: boolean;

    materials: IMaterial[];

    suppliers: ISupplier[];
    startDate: string;
    endDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialAvailabilityService: MaterialAvailabilityService,
        private materialService: MaterialService,
        private supplierService: SupplierService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialAvailability }) => {
            this.materialAvailability = materialAvailability;
            this.startDate =
                this.materialAvailability.creationDate != null ? this.materialAvailability.creationDate.format(DATE_TIME_FORMAT) : null;
            this.endDate =
                this.materialAvailability.updateDate != null ? this.materialAvailability.updateDate.format(DATE_TIME_FORMAT) : null;
        });
        this.materialService.query().subscribe(
            (res: HttpResponse<IMaterial[]>) => {
                this.materials = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.supplierService.query().subscribe(
            (res: HttpResponse<ISupplier[]>) => {
                this.suppliers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.materialAvailability.creationDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.materialAvailability.updateDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.materialAvailability.id !== undefined) {
            this.subscribeToSaveResponse(this.materialAvailabilityService.update(this.materialAvailability));
        } else {
            this.subscribeToSaveResponse(this.materialAvailabilityService.create(this.materialAvailability));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialAvailability>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialAvailability>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMaterialById(index: number, item: IMaterial) {
        return item.id;
    }

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }
}
