import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser, Principal } from 'app/core';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IDeliveryNote, DeliveryNoteStatus } from 'app/shared/model/delivery-note.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { ISupplier } from 'app/shared/model/supplier.model';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { DeliveryNoteService } from './delivery-note.service';
import { DeliveryNoteLine, IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

@Component({
    selector: 'jhi-delivery-note-update',
    templateUrl: './delivery-note-update.component.html'
})
export class DeliveryNoteUpdateComponent implements OnInit {
    private _deliveryNote: IDeliveryNote;
    selectedPurchaseOrderLine: IPurchaseOrderLine;
    isSaving: boolean;

    deliveryDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private deliveryNoteService: DeliveryNoteService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ deliveryNote }) => {
            this.deliveryNote = deliveryNote;
            // If new purchase order, initialize default values to make the object consistent
            if (!deliveryNote.id) {
                this.deliveryNote.creationDate = moment();
                this.deliveryNote.status = DeliveryNoteStatus.NEW;
                if (this.deliveryNote.deliveryNoteLines == null) {
                    this.deliveryNote.deliveryNoteLines = new Array();
                }
            }
            // Check if the purchase order can be edited
            this.principal.identity().then(account => {
                if (!this.deliveryNote.id) {
                    this.deliveryNote.creationUser = account;
                }
            });
        });
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

    get deliveryNote() {
        return this._deliveryNote;
    }

    set deliveryNote(deliveryNote: IDeliveryNote) {
        this._deliveryNote = deliveryNote;
        this.deliveryDate = moment(deliveryNote.deliveryDate).format(DATE_TIME_FORMAT);
        this.creationDate = moment(deliveryNote.creationDate).format(DATE_TIME_FORMAT);
    }

    /**
     * Delete a line from the current delivery note.
     *
     * @param {IDeliveryNoteLine} deliveryNoteLine
     * @memberof DeliveryNoteUpdateComponent
     */
    deleteDeliveryNoteLine(deliveryNoteLine: IDeliveryNoteLine) {
        const index: number = this.deliveryNote.deliveryNoteLines.indexOf(deliveryNoteLine);
        if (index !== -1) {
            this.deliveryNote.deliveryNoteLines.splice(index, 1);
        }
    }
    /**
     * Add a new line to the current delivery note.
     *
     * @memberof DeliveryNoteUpdateComponent
     */
    addDeliveryNoteLine() {
        if (this.selectedPurchaseOrderLine !== null) {
            const deliveryNoteLine = new DeliveryNoteLine();
            deliveryNoteLine.purchaseOrderLine = this.selectedPurchaseOrderLine;
            deliveryNoteLine.lineNumber = this.getNewDeliveryNoteLineNumber();
            deliveryNoteLine.quantity = deliveryNoteLine.purchaseOrderLine.quantity;
            this.deliveryNote.deliveryNoteLines.push(deliveryNoteLine);
        }
    }

    private getNewDeliveryNoteLineNumber(): number {
        return (
            this.deliveryNote.deliveryNoteLines
                .map(l => l.lineNumber)
                .reduce((previous, current) => (current > previous ? current : previous), 0) + 1
        );
    }
}
