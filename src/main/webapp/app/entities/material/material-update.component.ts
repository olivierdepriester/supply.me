import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-material-update',
    templateUrl: './material-update.component.html'
})
export class MaterialUpdateComponent implements OnInit {
    private _material: IMaterial;
    isSaving: boolean;
    private currentAccount: any;

    constructor(private materialService: MaterialService, private activatedRoute: ActivatedRoute, private principal: Principal) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ material }) => {
            this.material = material;
        });
        this.principal.identity().then(account => (this.currentAccount = account));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.material.id !== undefined) {
            this.subscribeToSaveResponse(this.materialService.update(this.material));
        } else {
            this.material.temporary = false;
            this.material.partNumber = '0000000000';
            this.material.creationDate = moment();
            this.material.creationUser = this.currentAccount;
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
    }
}
