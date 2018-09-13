import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from './demand.service';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-demand-update',
    templateUrl: './demand-update.component.html'
})
export class DemandUpdateComponent implements OnInit {
    private _demand: IDemand;
    isSaving: boolean;

    materials: IMaterial[];

    projects: IProject[];

    users: IUser[];
    expectedDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private demandService: DemandService,
        private materialService: MaterialService,
        private projectService: ProjectService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
        });
        this.materialService.query().subscribe(
            (res: HttpResponse<IMaterial[]>) => {
                this.materials = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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
