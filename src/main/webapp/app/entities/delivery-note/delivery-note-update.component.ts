import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDeliveryNote } from 'app/shared/model/delivery-note.model';
import { DeliveryNoteService } from './delivery-note.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-delivery-note-update',
    templateUrl: './delivery-note-update.component.html'
})
export class DeliveryNoteUpdateComponent implements OnInit {
    private _deliveryNote: IDeliveryNote;
    isSaving: boolean;

    suppliers: ISupplier[];

    users: IUser[];
    deliveryDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private deliveryNoteService: DeliveryNoteService,
        private supplierService: SupplierService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ deliveryNote }) => {
            this.deliveryNote = deliveryNote;
        });
        this.supplierService.query().subscribe(
            (res: HttpResponse<ISupplier[]>) => {
                this.suppliers = res.body;
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
        this.deliveryNote.deliveryDate = moment(this.deliveryDate, DATE_TIME_FORMAT);
        this.deliveryNote.creationDate = moment(this.creationDate, DATE_TIME_FORMAT);
        if (this.deliveryNote.id !== undefined) {
            this.subscribeToSaveResponse(this.deliveryNoteService.update(this.deliveryNote));
        } else {
            this.subscribeToSaveResponse(this.deliveryNoteService.create(this.deliveryNote));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryNote>>) {
        result.subscribe((res: HttpResponse<IDeliveryNote>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get deliveryNote() {
        return this._deliveryNote;
    }

    set deliveryNote(deliveryNote: IDeliveryNote) {
        this._deliveryNote = deliveryNote;
        this.deliveryDate = moment(deliveryNote.deliveryDate).format(DATE_TIME_FORMAT);
        this.creationDate = moment(deliveryNote.creationDate).format(DATE_TIME_FORMAT);
    }
}
