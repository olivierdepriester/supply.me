import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IMaterial } from 'app/shared/model/material.model';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { MaterialService } from './material.service';

@Component({
    selector: 'jhi-material',
    templateUrl: './material.component.html',
    styleUrls: ['./material.scss']
})
export class MaterialComponent implements OnInit, OnDestroy {
    materials: IMaterial[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    private defaultPredicate = 'partNumber.keyword';

    constructor(
        private materialService: MaterialService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.materials = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = this.defaultPredicate;
        this.reverse = true;
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        this.materialService
            .search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IMaterial[]>) => this.paginateMaterials(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        return;
    }

    reset() {
        this.page = 0;
        this.materials = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.materials = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = this.defaultPredicate;
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.materials = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = this.defaultPredicate;
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    validate(material: IMaterial) {
        material.temporary = false;
        this.materialService.update(material).subscribe((res: HttpResponse<IMaterial>) => (material = res.body));
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMaterials();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMaterial) {
        return item.id;
    }

    registerChangeInMaterials() {
        this.eventSubscriber = this.eventManager.subscribe('materialListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateMaterials(data: IMaterial[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.materials.push(data[i]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
