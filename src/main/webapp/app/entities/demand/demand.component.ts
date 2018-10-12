import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { faShareSquare, faShoppingCart, faThumbsDown, faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { Principal } from 'app/core';
import { DemandSearchCriteria, DemandStatus, IDemand } from 'app/shared/model/demand.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand',
    templateUrl: './demand.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandComponent implements OnInit, OnDestroy {
    demands: IDemand[];
    currentAccount: any;
    eventSubscriber: Subscription;

    searchCriteria: DemandSearchCriteria = new DemandSearchCriteria();
    faShareSquare = faShareSquare;
    faThumbsUp = faThumbsUp;
    faThumbsDown = faThumbsDown;
    faShoppingCart = faShoppingCart;
    authorities: boolean[] = new Array();

    constructor(
        private demandService: DemandService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.searchCriteria.query =
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

    search() {
        this.demandService
            .search(this.searchCriteria.getQuery())
            .subscribe((res: HttpResponse<IDemand[]>) => (this.demands = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.searchCriteria.query = '';
        this.searchCriteria.status = null;
        this.searchCriteria.material = null;
        this.searchCriteria.project = null;
        this.searchCriteria.creationUser = null;
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
        // Current user
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.searchCriteria.creationUser = this.currentAccount;
            this.search();
        });
        this.principal.hasAuthority('ROLE_PURCHASER').then(value => (this.authorities['ROLE_PURCHASER'] = value));
        this.principal.hasAuthority('ROLE_APPROVAL_LVL1').then(value => (this.authorities['ROLE_APPROVAL_LVL1'] = value));
        this.principal.hasAuthority('ROLE_APPROVAL_LVL2').then(value => (this.authorities['ROLE_APPROVAL_LVL2'] = value));
        this.principal.hasAuthority('ROLE_ADMIN').then(value => (this.authorities['ROLE_ADMIN'] = value));
        this.registerChangeInDemands();
    }

    isPurchaseOrderAllowed(demand: IDemand): boolean {
        return (
            ((demand.status === DemandStatus.ORDERED &&
                (demand.quantityOrdered == null || demand.quantity.valueOf() > demand.quantityOrdered.valueOf())) ||
                demand.status === DemandStatus.APPROVED) &&
            this.authorities['ROLE_PURCHASER']
        );
    }

    isApprovalAllowed(demand: IDemand) {
        return this.demandService.isApprovalAllowed(demand, this.currentAccount);
    }

    isEditAllowed(demand: IDemand): boolean {
        return this.demandService.isEditAllowed(demand, this.currentAccount);
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
