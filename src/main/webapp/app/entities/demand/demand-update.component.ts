import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser, Principal } from 'app/core';
import { IDemand } from 'app/shared/model/demand.model';
import { IMaterial } from 'app/shared/model/material.model';
import { IProject } from 'app/shared/model/project.model';
import * as moment from 'moment';
import { JhiEventManager } from 'ng-jhipster';
import { Observable, Subscription } from 'rxjs';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-update',
    templateUrl: './demand-update.component.html'
})
export class DemandUpdateComponent implements OnInit, OnDestroy {
    private _demand: IDemand;
    isSaving: boolean;
    expectedDate: Date;
    @ViewChild('editForm') editForm: HTMLFormElement;
    eventSubscriber: Subscription;
    locale: string;

    constructor(
        private demandService: DemandService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
            if (demand.id != null) {
                this.principal.identity().then(account => {
                    if (!this.demandService.isEditAllowed(this.demand, account)) {
                        this.router.navigate(['accessdenied']);
                    }
                    this.locale = 'en';
                });
            }
        });
        // Callback on new material creation
        this.eventSubscriber = this.eventManager.subscribe(
            'temporaryMaterialCreated',
            response => (this.demand.material = response.content)
        );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDemand) {
        return item.id;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.demand.expectedDate = moment(this.expectedDate);
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
        if (demand.expectedDate != null) {
            this.expectedDate = demand.expectedDate.toDate();
        } else {
            this.expectedDate = null;
        }
    }
}
