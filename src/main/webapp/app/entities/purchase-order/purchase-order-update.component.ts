import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';
import { IUser } from 'app/core';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine, PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { DemandService } from 'app/entities/demand';
import { DemandSelectorComponent } from 'app/entities/component/demand-selector';

@Component({
    selector: 'jhi-purchase-order-update',
    templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
    private _purchaseOrder: IPurchaseOrder;
    private _demand: IDemand;

    @ViewChild(DemandSelectorComponent) private demandSelector: DemandSelectorComponent;

    demandToAdd: IDemand;

    isSaving: boolean;
    editField: string;

    suppliers: ISupplier[];

    expectedDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseOrderService: PurchaseOrderService,
        private supplierService: SupplierService,
        private demandService: DemandService,
        private activatedRoute: ActivatedRoute
    ) {}

    get purchaseOrder() {
        return this._purchaseOrder;
    }

    set purchaseOrder(purchaseOrder: IPurchaseOrder) {
        this._purchaseOrder = purchaseOrder;
        this.expectedDate = moment(purchaseOrder.expectedDate).format(DATE_FORMAT);
        this.creationDate = moment(purchaseOrder.creationDate).format(DATE_TIME_FORMAT);
    }

    // get demand() {
    //     return this._demand;
    // }

    // set demand(demand: IDemand) {
    //     this._demand = demand;
    // }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseOrder, demand }) => {
            this.purchaseOrder = purchaseOrder;
            this._demand = demand;
        });
        if (this.purchaseOrder.purchaseOrderLines == null) {
            this.purchaseOrder.purchaseOrderLines = new Array();
        }
        if (this._demand != null && !this.purchaseOrder.id) {
            this.addPurchaseOrderLineFromDemand(this._demand);
        }

        this.supplierService.query().subscribe(
            (res: HttpResponse<ISupplier[]>) => {
                this.suppliers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.purchaseOrder.expectedDate = moment(this.expectedDate, DATE_FORMAT);
        if (this.purchaseOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseOrderService.update(this.purchaseOrder));
        } else {
            this.subscribeToSaveResponse(this.purchaseOrderService.create(this.purchaseOrder));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>) {
        result.subscribe((res: HttpResponse<IPurchaseOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    changeValue(id: number, property: string, event: any) {
        this.editField = event.target.textContent;
        console.log(this.editField);
        this.purchaseOrder.purchaseOrderLines[id][property] = this.editField;
    }

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }

    trackLineById(index: number, item: IPurchaseOrderLine) {
        return item.id;
    }

    deletePurchaseOrderLine(purchaseOrderLine: PurchaseOrderLine) {
        const index: number = this.purchaseOrder.purchaseOrderLines.indexOf(purchaseOrderLine);
        if (index !== -1) {
            this.purchaseOrder.purchaseOrderLines.splice(index, 1);
        }
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    /**
     * Event thrown when click on the Add button
     *
     * @memberof PurchaseOrderUpdateComponent
     */
    addPurchoseOrderLine() {
        if (this.demandSelector.selectedDemand != null) {
            this.addPurchaseOrderLineFromDemand(this.demandSelector.selectedDemand);
        }
    }
    /**
     * Add a line to the purchase order from a demand
     *
     * @private
     * @param {IDemand} demand
     * @memberof PurchaseOrderUpdateComponent
     */
    private addPurchaseOrderLineFromDemand(demand: IDemand) {
        console.log(demand);
        const line = new PurchaseOrderLine();
        line.lineNumber = this.purchaseOrder.purchaseOrderLines.length + 1;
        line.orderPrice = 0;
        line.quantity = demand.quantity - demand.quantityOrdered;
        line.demand = demand;
        this.purchaseOrder.purchaseOrderLines.push(line);
    }
}
