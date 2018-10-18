import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { DemandSearchCriteria, DemandStatus, IDemand } from 'app/shared/model/demand.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { DemandService } from './demand.service';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
    selector: 'jhi-demand',
    templateUrl: './demand.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandComponent implements OnInit, OnDestroy {
    demands: IDemand[];
    currentAccount: any;
    eventSubscriber: Subscription;
    searchCriteria: DemandSearchCriteria;
    isAdvancedFilterDisplayed = false;

    constructor(
        private demandService: DemandService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private sessionStorageService: SessionStorageService
    ) {
        // this.searchCriteria.query =
        //     this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
        //         ? this.activatedRoute.snapshot.params['search']
        //         : '';
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ criteria }) => {
            if (criteria !== undefined) {
                this.searchCriteria = criteria;
                this.search();
            } else {
                console.log('session criteria');
                console.log(this.sessionStorageService.retrieve('demandCriteria'));
                this.searchCriteria = DemandSearchCriteria.of(this.sessionStorageService.retrieve('demandCriteria'));
                console.log('copy of criteria');
                console.log(this.searchCriteria);
                if (!this.searchCriteria) {
                    this.searchCriteria = new DemandSearchCriteria();
                } else {
                    this.search();
                }
                this.isAdvancedFilterDisplayed = true;
            }
        });
        // Current user
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDemands();
    }

    search() {
        this.sessionStorageService.store('demandCriteria', this.searchCriteria);
        this.demandService
            .search(this.searchCriteria.getQuery())
            .subscribe((res: HttpResponse<IDemand[]>) => (this.demands = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.searchCriteria.clear();
    }

    sendToApproval(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.WAITING_FOR_APPROVAL, null);
    }

    approve(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.APPROVED, null);
    }

    /**
     * Change the status of a given demand
     * @param demand
     * @param status
     */
    private changeStatus(demand: IDemand, status: DemandStatus, comment: string) {
        this.demandService.changeStatus(demand.id, status, comment).subscribe(
            (res: HttpResponse<IDemand>) => {
                // Refresh list modified demand
                const demandInList = this.demands.find(d => d.id === res.body.id);
                if (demandInList !== null) {
                    demandInList.status = res.body.status;
                }
            },
            (res: HttpErrorResponse) => {
                this.onError(res.message);
            }
        );
    }

    isPurchaseOrderAllowed(demand: IDemand): boolean {
        return (
            ((demand.status === DemandStatus.ORDERED &&
                (demand.quantityOrdered == null || demand.quantity.valueOf() > demand.quantityOrdered.valueOf())) ||
                demand.status === DemandStatus.APPROVED) &&
            this.currentAccount.authorities.includes('ROLE_PURCHASER')
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
        this.eventSubscriber = this.eventManager.subscribe('demandListModification', () => this.search());
        this.eventSubscriber = this.eventManager.subscribe('demandComment', response =>
            this.changeStatus(response.content.demand, response.content.status, response.content.comment)
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    expandCollapse(): void {
        this.isAdvancedFilterDisplayed = !this.isAdvancedFilterDisplayed;
    }
}
