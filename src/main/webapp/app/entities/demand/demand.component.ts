import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { DemandSearchCriteria, DemandStatus, IDemand, DemandListItem } from 'app/shared/model/demand.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { DemandService } from './demand.service';
import { SessionStorageService } from 'ngx-webstorage';
import { SelectItem } from 'primeng/primeng';

@Component({
    selector: 'jhi-demand',
    templateUrl: './demand.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandComponent implements OnInit, OnDestroy {
    demands: DemandListItem[];
    currentAccount: any;
    eventSubscriber: Subscription;
    searchCriteria: DemandSearchCriteria;
    isCollapsed = true;
    availableStatus: SelectItem[] = [];
    predicate: any;
    reverse: boolean;

    constructor(
        private demandService: DemandService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private sessionStorageService: SessionStorageService
    ) {
        this.predicate = 'id';
        this.reverse = false;
        // Init the status selector
        Object.keys(DemandStatus).forEach(status => this.availableStatus.push({ label: status, value: DemandStatus[status] }));
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ criteria }) => {
            if (criteria !== undefined) {
                // Criteria initialized from route
                this.searchCriteria = criteria;
                this.search();
            } else {
                // Try to find existsing criteria in session
                this.searchCriteria = DemandSearchCriteria.of(this.sessionStorageService.retrieve('demandCriteria'));
                if (this.searchCriteria) {
                    // If criteria are in session : search
                    this.search();
                } else {
                    // No criteria : intialize new block
                    this.searchCriteria = new DemandSearchCriteria();
                }
                this.isCollapsed = true;
            }
        });
        // Current user
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDemands();
    }

    searchClick() {
        this.search();
    }

    private search() {
        this.sessionStorageService.store('demandCriteria', this.searchCriteria);
        this.demandService
            .search({
                query: this.searchCriteria.getQuery(),
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IDemand[]>) =>
                    (this.demands = res.body.map(
                        d =>
                            new DemandListItem(d, {
                                canEdit: this.demandService.isEditAllowed(d, this.currentAccount),
                                canSendToApproval: this.demandService.isEditAllowed(d, this.currentAccount),
                                canApprove: this.demandService.isApprovalAllowed(d, this.currentAccount),
                                canDelete: this.demandService.isDeleteAllowed(d, this.currentAccount),
                                canReject: this.demandService.isRejectAllowed(d, this.currentAccount)
                            })
                    )),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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

    close(demand: IDemand) {
        this.changeStatus(demand, DemandStatus.CLOSED, null);
    }

    /**
     * Change the status of a given demand
     * @param demand
     * @param status
     */
    private changeStatus(demand: IDemand, status: DemandStatus, comment: string) {
        this.demandService.changeStatus(demand.id, status, comment).subscribe((res: HttpResponse<IDemand>) => {
            // Refresh list modified demand
            const demandInList = this.demands.find(d => d.demand.id === res.body.id);
            if (demandInList !== null) {
                demandInList.demand = res.body;
                demandInList.allowance = {
                    canEdit: this.demandService.isEditAllowed(demandInList.demand, this.currentAccount),
                    canSendToApproval: this.demandService.isEditAllowed(demandInList.demand, this.currentAccount),
                    canApprove: this.demandService.isApprovalAllowed(demandInList.demand, this.currentAccount),
                    canDelete: this.demandService.isDeleteAllowed(demandInList.demand, this.currentAccount),
                    canReject: this.demandService.isRejectAllowed(demandInList.demand, this.currentAccount)
                };
            }
            // this.search();
        });
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

    isDeleteAllowed(demand: IDemand): boolean {
        return this.demandService.isDeleteAllowed(demand, this.currentAccount);
    }

    isRejectAllowed(demand: IDemand): boolean {
        return this.demandService.isRejectAllowed(demand, this.currentAccount);
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
        this.isCollapsed = !this.isCollapsed;
    }

    reset() {
        this.demands = [];
        this.search();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
}
