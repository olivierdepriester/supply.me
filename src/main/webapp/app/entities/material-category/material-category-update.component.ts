import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialCategory } from 'app/shared/model/material-category.model';
import { MaterialCategoryService } from './material-category.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-material-category-update',
    templateUrl: './material-category-update.component.html'
})
export class MaterialCategoryUpdateComponent implements OnInit {
    materialCategory: IMaterialCategory;
    isSaving: boolean;

    materialcategories: IMaterialCategory[];

    users: IUser[];
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialCategoryService: MaterialCategoryService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialCategory }) => {
            this.materialCategory = materialCategory;
            this.creationDate =
                this.materialCategory.creationDate != null ? this.materialCategory.creationDate.format(DATE_TIME_FORMAT) : null;
        });
        this.materialCategoryService.query().subscribe(
            (res: HttpResponse<IMaterialCategory[]>) => {
                this.materialcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        this.materialCategory.creationDate = this.creationDate != null ? moment(this.creationDate, DATE_TIME_FORMAT) : null;
        if (this.materialCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.materialCategoryService.update(this.materialCategory));
        } else {
            this.subscribeToSaveResponse(this.materialCategoryService.create(this.materialCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialCategory>>) {
        result.subscribe((res: HttpResponse<IMaterialCategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMaterialCategoryById(index: number, item: IMaterialCategory) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
