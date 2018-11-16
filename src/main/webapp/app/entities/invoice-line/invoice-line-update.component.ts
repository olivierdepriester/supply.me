import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IInvoiceLine } from 'app/shared/model/invoice-line.model';
import { InvoiceLineService } from './invoice-line.service';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material';

@Component({
    selector: 'jhi-invoice-line-update',
    templateUrl: './invoice-line-update.component.html'
})
export class InvoiceLineUpdateComponent implements OnInit {
    invoiceLine: IInvoiceLine;
    isSaving: boolean;

    purchaseorders: IPurchaseOrder[];

    materials: IMaterial[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private invoiceLineService: InvoiceLineService,
        private purchaseOrderService: PurchaseOrderService,
        private materialService: MaterialService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ invoiceLine }) => {
            this.invoiceLine = invoiceLine;
        });
        this.purchaseOrderService.query().subscribe(
            (res: HttpResponse<IPurchaseOrder[]>) => {
                this.purchaseorders = res.body;
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
        if (this.invoiceLine.id !== undefined) {
            this.subscribeToSaveResponse(this.invoiceLineService.update(this.invoiceLine));
        } else {
            this.subscribeToSaveResponse(this.invoiceLineService.create(this.invoiceLine));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceLine>>) {
        result.subscribe((res: HttpResponse<IInvoiceLine>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPurchaseOrderById(index: number, item: IPurchaseOrder) {
        return item.id;
    }

    trackMaterialById(index: number, item: IMaterial) {
        return item.id;
    }
}
