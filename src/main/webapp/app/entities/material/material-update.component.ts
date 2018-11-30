import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { IMaterial } from 'app/shared/model/material.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { MaterialService } from './material.service';

@Component({
    selector: 'jhi-material-update',
    templateUrl: './material-update.component.html'
})
export class MaterialUpdateComponent implements OnInit {
    material: IMaterial;
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
}
