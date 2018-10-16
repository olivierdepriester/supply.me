import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser, Principal } from 'app/core';
import { DemandSelectorComponent } from 'app/entities/component/demand-selector';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine, PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { IPurchaseOrder, PurchaseOrderStatus } from 'app/shared/model/purchase-order.model';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { PurchaseOrderService } from './purchase-order.service';

@Component({
    selector: 'jhi-purchase-order-update',
    templateUrl: './purchase-order-update.component.html'
})
export class PurchaseOrderUpdateComponent implements OnInit {
    private _purchaseOrder: IPurchaseOrder;
    private _demand: IDemand;

    @ViewChild(DemandSelectorComponent) private demandSelector: DemandSelectorComponent;

    isSaving: boolean;

    expectedDate: string;
    creationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private purchaseOrderService: PurchaseOrderService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private router: Router
    ) {}

    get purchaseOrder() {
        return this._purchaseOrder;
    }

    set purchaseOrder(purchaseOrder: IPurchaseOrder) {
        this._purchaseOrder = purchaseOrder;
        this.expectedDate = moment(purchaseOrder.expectedDate).format(DATE_FORMAT);
        this.creationDate = moment(purchaseOrder.creationDate).format(DATE_TIME_FORMAT);
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseOrder, demand }) => {
            this.purchaseOrder = purchaseOrder;
            // If new purchase order, initialize default values to make the object consistent
            if (!purchaseOrder.id) {
                this.purchaseOrder.creationDate = moment();
                this.purchaseOrder.status = PurchaseOrderStatus.NEW;
                this.purchaseOrder.code = '0000000000';
            }
            // Check if the purchase order can be edited
            this.principal.identity().then(account => {
                if (!this.purchaseOrder.id) {
                    this.purchaseOrder.creationUser = account;
                }
                if (!this.purchaseOrderService.isEditable(this.purchaseOrder, account)) {
                    // Forbidden
                    this.router.navigate(['accessdenied']);
                } else {
                    // Initialization end
                    this._demand = demand;
                    if (this.purchaseOrder.purchaseOrderLines == null) {
                        this.purchaseOrder.purchaseOrderLines = new Array();
                    }
                    if (this._demand != null && !this.purchaseOrder.id) {
                        this.addPurchaseOrderLineFromDemand(this._demand);
                    }
                }
            });
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.purchaseOrder.expectedDate = moment(this.expectedDate, DATE_FORMAT);
        if (this.validate()) {
            if (this.purchaseOrder.id !== undefined) {
                this.subscribeToSaveResponse(this.purchaseOrderService.update(this.purchaseOrder));
            } else {
                this.subscribeToSaveResponse(this.purchaseOrderService.create(this.purchaseOrder));
            }
        }
        this.isSaving = false;
    }

    /**
     * Validate purchase porder data before being saved
     *
     * @private
     * @returns {boolean} false if purchase order is not valid
     * @memberof PurchaseOrderUpdateComponent
     */
    private validate(): boolean {
        let result = true;
        const errorMessages: any[] = new Array();
        const wrongPrices = this.purchaseOrder.purchaseOrderLines.filter(l => isNaN(Number(l.orderPrice)));
        const wrongQuantities = this.purchaseOrder.purchaseOrderLines.filter(l => isNaN(Number(l.quantity)));
        if (wrongPrices.length !== 0) {
            errorMessages.push({ key: 'error.purchaseOrder.validation.orderPrice', value: wrongPrices.map(l => l.lineNumber).join(',') });
            result = false;
        }
        if (wrongQuantities.length !== 0) {
            errorMessages.push({ key: 'error.purchaseOrder.validation.quantity', value: wrongQuantities.map(l => l.lineNumber).join(',') });
            result = false;
        }
        if (!result) {
            errorMessages.forEach(item => this.jhiAlertService.error(item.key, { value: item.value }, null));
        }
        return result;
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

    trackLineById(index: number, item: IPurchaseOrderLine) {
        return item.id;
    }

    deletePurchaseOrderLine(purchaseOrderLine: PurchaseOrderLine) {
        const index: number = this.purchaseOrder.purchaseOrderLines.indexOf(purchaseOrderLine);
        if (index !== -1) {
            this.purchaseOrder.purchaseOrderLines.splice(index, 1);
        }
    }

    /**
     * Event thrown when click on the Add button
     *
     * @memberof PurchaseOrderUpdateComponent
     */
    addPurchaseOrderLine() {
        if (this.demandSelector.selectedData != null) {
            this.addPurchaseOrderLineFromDemand(this.demandSelector.selectedData);
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
        const line = new PurchaseOrderLine();
        line.lineNumber = this.getNewPOLineNumber();
        line.orderPrice = 0;
        line.quantity = demand.quantity - demand.quantityOrdered;
        line.demand = demand;
        this.purchaseOrder.purchaseOrderLines.push(line);
    }

    private getNewPOLineNumber(): number {
        return (
            this.purchaseOrder.purchaseOrderLines
                .map(l => l.lineNumber)
                .reduce((previous, current) => (current > previous ? current : previous), 0) + 1
        );
    }
}
