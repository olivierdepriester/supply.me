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

@Component({
    selector: 'jhi-purchase-order-update',
    templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
    purchaseOrder: IPurchaseOrder;
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
        this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
            this.purchaseOrder = purchaseOrder;
            this.expectedDate = this.purchaseOrder.expectedDate != null ? this.purchaseOrder.expectedDate.format(DATE_TIME_FORMAT) : null;
            this.creationDate = this.purchaseOrder.creationDate != null ? this.purchaseOrder.creationDate.format(DATE_TIME_FORMAT) : null;
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
        this.purchaseOrder.expectedDate = this.expectedDate != null ? moment(this.expectedDate, DATE_TIME_FORMAT) : null;
        this.purchaseOrder.creationDate = this.creationDate != null ? moment(this.creationDate, DATE_TIME_FORMAT) : null;
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
