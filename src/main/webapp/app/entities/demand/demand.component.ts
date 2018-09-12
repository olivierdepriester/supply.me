import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDemand } from 'app/shared/model/demand.model';
import { Principal } from 'app/core';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand',
    templateUrl: './demand.component.html'
})
export class DemandComponent implements OnInit, OnDestroy {
    demands: IDemand[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private demandService: DemandService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.demandService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IDemand[]>) => (this.demands = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.demandService.query().subscribe(
            (res: HttpResponse<IDemand[]>) => {
                this.demands = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
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
