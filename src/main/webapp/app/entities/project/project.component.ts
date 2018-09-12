import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { Principal } from 'app/core';
import { ProjectService } from './project.service';

@Component({
    selector: 'jhi-project',
    templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit, OnDestroy {
    projects: IProject[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private projectService: ProjectService,
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
            this.projectService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IProject[]>) => (this.projects = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
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
        this.registerChangeInProjects();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProject) {
        return item.id;
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe('projectListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
