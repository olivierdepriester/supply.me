import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMutableProperties } from 'app/shared/model/mutable-properties.model';
import { MutablePropertiesService } from './mutable-properties.service';

@Component({
    selector: 'jhi-mutable-properties-update',
    templateUrl: './mutable-properties-update.component.html'
})
export class MutablePropertiesUpdateComponent implements OnInit {
    mutableProperties: IMutableProperties;
    isSaving: boolean;

    constructor(private mutablePropertiesService: MutablePropertiesService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mutableProperties }) => {
            this.mutableProperties = mutableProperties;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mutableProperties.id !== undefined) {
            this.subscribeToSaveResponse(this.mutablePropertiesService.update(this.mutableProperties));
        } else {
            this.subscribeToSaveResponse(this.mutablePropertiesService.create(this.mutableProperties));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMutableProperties>>) {
        result.subscribe((res: HttpResponse<IMutableProperties>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
