import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { PurchaseOrderService } from './purchase-order.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';
import { IUser, UserService } from 'app/core';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine, PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

@Component({
    selector: 'jhi-purchase-order-update',
    templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
    private _purchaseOrder: IPurchaseOrder;
    private _demand: IDemand;
    lines: IPurchaseOrderLine[];
    isSaving: boolean;

    suppliers: ISupplier[];

    users: IUser[];
    expectedDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseOrderService: PurchaseOrderService,
        private supplierService: SupplierService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseOrder, demand }) => {
            this.purchaseOrder = purchaseOrder;
            this.demand = demand;
        });
        if (this.demand != null && !this.purchaseOrder.id) {
            this.lines = new Array();
            const line = new PurchaseOrderLine();
            line.lineNumber = 1;
            line.purchaseOrder = this.purchaseOrder;
            line.demand = this.demand;
            line.quantity = this.demand.quantity;
            this.lines[0] = line;
        }

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
        this.purchaseOrder.expectedDate = moment(this.expectedDate, DATE_TIME_FORMAT);
        this.purchaseOrder.creationDate = moment();
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

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }

    trackLineById(index: number, item: IPurchaseOrderLine) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get purchaseOrder() {
        return this._purchaseOrder;
    }

    set purchaseOrder(purchaseOrder: IPurchaseOrder) {
        this._purchaseOrder = purchaseOrder;
        this.expectedDate = moment(purchaseOrder.expectedDate).format(DATE_TIME_FORMAT);
        this.creationDate = moment(purchaseOrder.creationDate).format(DATE_TIME_FORMAT);
    }

    get demand() {
        return this._demand;
    }

    set demand(demand: IDemand) {
        this._demand = demand;
    }
}
