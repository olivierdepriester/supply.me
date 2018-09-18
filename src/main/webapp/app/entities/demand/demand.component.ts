import { IMaterial } from '../../shared/model/material.model';
import { IProject } from '../../shared/model/project.model';
import { MaterialService } from '../material';
import { ProjectService } from '../project';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDemand, DemandSearchCriteria, DemandStatus } from 'app/shared/model/demand.model';
import { Principal } from 'app/core';
import { DemandService } from './demand.service';
import { faShareSquare, faThumbsUp, faThumbsDown, faShoppingCart } from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'jhi-demand',
    templateUrl: './demand.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandComponent implements OnInit, OnDestroy {
    demands: IDemand[];
    currentAccount: any;
    eventSubscriber: Subscription;
    searchCriteria: DemandSearchCriteria = {};
    materials: IMaterial[];
    projects: IProject[];
    faShareSquare = faShareSquare;
    faThumbsUp = faThumbsUp;
    faThumbsDown = faThumbsDown;
    faShoppingCart = faShoppingCart;

    constructor(
        private demandService: DemandService,
        private materialService: MaterialService,
        private projectService: ProjectService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.searchCriteria.fullText =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        this.demandService.query().subscribe(
            (res: HttpResponse<IDemand[]>) => {
                this.demands = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.demandService
            .search(this.searchCriteria)
            .subscribe((res: HttpResponse<IDemand[]>) => (this.demands = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.searchCriteria.fullText = '';
        this.searchCriteria.status = DemandStatus.NEW;
        this.searchCriteria.materialId = null;
        this.searchCriteria.projectId = null;
        this.loadAll();
    }

    sendToApproval(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.WAITING_FOR_APPROVAL);
    }

    approve(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.APPROVED);
    }

    reject(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.REJECTED);
    }

    /**
     * Change the status of a given demand
     * @param demand
     * @param status
     */
    private changeStatus(demand: IDemand, status: DemandStatus) {
        this.demandService.changeStatus(demand.id, status).subscribe(
            (res: HttpResponse<IDemand>) => {
                demand.status = res.body.status;
            },
            (res: HttpErrorResponse) => {
                this.onError(res.message);
            }
        );
    }

    ngOnInit() {
        // Material list loading
        this.materialService
            .query()
            .subscribe(
                (res: HttpResponse<IMaterial[]>) => (this.materials = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        // Project list loading
        this.projectService
            .query()
            .subscribe(
                (res: HttpResponse<IProject[]>) => (this.projects = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        // Current user
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.loadAll();
        this.registerChangeInDemands();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDemand) {
        return item.id;
    }

    registerChangeInDemands() {
        this.eventSubscriber = this.eventManager.subscribe('demandListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
