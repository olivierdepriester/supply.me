import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';
import { DeliveryNoteLineService } from './delivery-note-line.service';
import { IDeliveryNote } from 'app/shared/model/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material';

@Component({
    selector: 'jhi-delivery-note-line-update',
    templateUrl: './delivery-note-line-update.component.html'
})
export class DeliveryNoteLineUpdateComponent implements OnInit {
    deliveryNoteLine: IDeliveryNoteLine;
    isSaving: boolean;

    deliverynotes: IDeliveryNote[];

    purchaseorderlines: IPurchaseOrderLine[];

    materials: IMaterial[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private deliveryNoteLineService: DeliveryNoteLineService,
        private deliveryNoteService: DeliveryNoteService,
        private purchaseOrderLineService: PurchaseOrderLineService,
        private materialService: MaterialService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ deliveryNoteLine }) => {
            this.deliveryNoteLine = deliveryNoteLine;
        });
        this.deliveryNoteService.query().subscribe(
            (res: HttpResponse<IDeliveryNote[]>) => {
                this.deliverynotes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.purchaseOrderLineService.query().subscribe(
            (res: HttpResponse<IPurchaseOrderLine[]>) => {
                this.purchaseorderlines = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.materialService.query().subscribe(
            (res: HttpResponse<IMaterial[]>) => {
                this.materials = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.deliveryNoteLine.id !== undefined) {
            this.subscribeToSaveResponse(this.deliveryNoteLineService.update(this.deliveryNoteLine));
        } else {
            this.subscribeToSaveResponse(this.deliveryNoteLineService.create(this.deliveryNoteLine));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryNoteLine>>) {
        result.subscribe((res: HttpResponse<IDeliveryNoteLine>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDeliveryNoteById(index: number, item: IDeliveryNote) {
        return item.id;
    }

    trackPurchaseOrderLineById(index: number, item: IPurchaseOrderLine) {
        return item.id;
    }

    trackMaterialById(index: number, item: IMaterial) {
        return item.id;
    }
}
