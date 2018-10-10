import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser, Principal } from 'app/core';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { IDemand } from 'app/shared/model/demand.model';
import { IMaterial } from 'app/shared/model/material.model';
import { IProject } from 'app/shared/model/project.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-update',
    templateUrl: './demand-update.component.html'
})
export class DemandUpdateComponent implements OnInit {
    private _demand: IDemand;
    isSaving: boolean;
    expectedDate: string;
    creationDate: string;
    @ViewChild('editForm') editForm: HTMLFormElement;

    constructor(
        private demandService: DemandService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
            this.principal.identity().then(account => {
                if (!this.demandService.isEditAllowed(this.demand, account)) {
                    this.router.navigate(['accessdenied']);
                }
            });
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.demand.expectedDate = moment(this.expectedDate, DATE_FORMAT);
        if (this.demand.id !== undefined) {
            this.subscribeToSaveResponse(this.demandService.update(this.demand));
        } else {
            this.subscribeToSaveResponse(this.demandService.create(this.demand));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDemand>>) {
        result.subscribe((res: HttpResponse<IDemand>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    trackMaterialById(index: number, item: IMaterial) {
        return item.id;
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get demand() {
        return this._demand;
    }

    set demand(demand: IDemand) {
        this._demand = demand;
        this.expectedDate = moment(demand.expectedDate).format(DATE_FORMAT);
        this.creationDate = moment(demand.creationDate).format(DATE_FORMAT);
    }
}
