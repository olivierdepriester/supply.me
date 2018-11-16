import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { PurchaseOrderLineService } from './purchase-order-line.service';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order';
import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from 'app/entities/demand';

@Component({
    selector: 'jhi-purchase-order-line-update',
    templateUrl: './purchase-order-line-update.component.html'
})
export class PurchaseOrderLineUpdateComponent implements OnInit {
    purchaseOrderLine: IPurchaseOrderLine;
    isSaving: boolean;

    purchaseorders: IPurchaseOrder[];

    demands: IDemand[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseOrderLineService: PurchaseOrderLineService,
        private purchaseOrderService: PurchaseOrderService,
        private demandService: DemandService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseOrderLine }) => {
            this.purchaseOrderLine = purchaseOrderLine;
        });
        this.purchaseOrderService.query().subscribe(
            (res: HttpResponse<IPurchaseOrder[]>) => {
                this.purchaseorders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.demandService.query().subscribe(
            (res: HttpResponse<IDemand[]>) => {
                this.demands = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.purchaseOrderLine.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseOrderLineService.update(this.purchaseOrderLine));
        } else {
            this.subscribeToSaveResponse(this.purchaseOrderLineService.create(this.purchaseOrderLine));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderLine>>) {
        result.subscribe((res: HttpResponse<IPurchaseOrderLine>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDemandById(index: number, item: IDemand) {
        return item.id;
    }
}
