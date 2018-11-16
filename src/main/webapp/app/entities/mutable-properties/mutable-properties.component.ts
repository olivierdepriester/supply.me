import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMutableProperties } from 'app/shared/model/mutable-properties.model';
import { Principal } from 'app/core';
import { MutablePropertiesService } from './mutable-properties.service';

@Component({
    selector: 'jhi-mutable-properties',
    templateUrl: './mutable-properties.component.html'
})
export class MutablePropertiesComponent implements OnInit, OnDestroy {
    mutableProperties: IMutableProperties[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private mutablePropertiesService: MutablePropertiesService,
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
            this.mutablePropertiesService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IMutableProperties[]>) => (this.mutableProperties = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.mutablePropertiesService.query().subscribe(
            (res: HttpResponse<IMutableProperties[]>) => {
                this.mutableProperties = res.body;
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
        this.registerChangeInMutableProperties();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMutableProperties) {
        return item.id;
    }

    registerChangeInMutableProperties() {
        this.eventSubscriber = this.eventManager.subscribe('mutablePropertiesListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
