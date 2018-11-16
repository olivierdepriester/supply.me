import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';

@Component({
    selector: 'jhi-material-update',
    templateUrl: './material-update.component.html'
})
export class MaterialUpdateComponent implements OnInit {
    private _material: IMaterial;
    isSaving: boolean;
    creationDate: string;

    constructor(private materialService: MaterialService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.material.creationDate = moment(this.creationDate, DATE_TIME_FORMAT);
        if (this.material.id !== undefined) {
            this.subscribeToSaveResponse(this.materialService.update(this.material));
        } else {
            this.subscribeToSaveResponse(this.materialService.create(this.material));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterial>>) {
        result.subscribe((res: HttpResponse<IMaterial>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get material() {
        return this._material;
    }

    set material(material: IMaterial) {
        this._material = material;
        this.creationDate = moment(material.creationDate).format(DATE_TIME_FORMAT);
    }
}
