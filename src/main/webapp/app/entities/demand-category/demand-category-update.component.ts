import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDemandCategory } from 'app/shared/model/demand-category.model';
import { DemandCategoryService } from './demand-category.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-demand-category-update',
    templateUrl: './demand-category-update.component.html'
})
export class DemandCategoryUpdateComponent implements OnInit {
    demandCategory: IDemandCategory;
    isSaving: boolean;

    users: IUser[];
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private demandCategoryService: DemandCategoryService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demandCategory }) => {
            this.demandCategory = demandCategory;
            this.creationDate = this.demandCategory.creationDate != null ? this.demandCategory.creationDate.format(DATE_TIME_FORMAT) : null;
        });
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
        this.demandCategory.creationDate = this.creationDate != null ? moment(this.creationDate, DATE_TIME_FORMAT) : null;
        if (this.demandCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.demandCategoryService.update(this.demandCategory));
        } else {
            this.subscribeToSaveResponse(this.demandCategoryService.create(this.demandCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDemandCategory>>) {
        result.subscribe((res: HttpResponse<IDemandCategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
