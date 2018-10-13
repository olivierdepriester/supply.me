import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrder, PurchaseOrderStatus } from 'app/shared/model/purchase-order.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { PurchaseOrderService } from '.';
import { JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-purchase-order-detail',
    templateUrl: './purchase-order-detail.component.html'
})
export class PurchaseOrderDetailComponent implements OnInit {
    purchaseOrder: IPurchaseOrder;
    isSending = false;
    isEditable = false;
    currentAccount: any;

    constructor(
        private activatedRoute: ActivatedRoute,
        private purchaseOrderService: PurchaseOrderService,
        private jhiAlertService: JhiAlertService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
            this.purchaseOrder = purchaseOrder;
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.isEditable = this.purchaseOrderService.isEditable(this.purchaseOrder, account);
            });
        });
    }

    previousState() {
        window.history.back();
    }

    send() {
        this.isSending = true;
        this.purchaseOrder.status = PurchaseOrderStatus.SENT;
        this.purchaseOrderService.update(this.purchaseOrder).subscribe(
            (res: HttpResponse<IPurchaseOrder>) => {
                this.isEditable = this.purchaseOrderService.isEditable(this.purchaseOrder, this.currentAccount);
                this.onSuccess();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private onSuccess() {
        this.isSending = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage);
        this.isSending = false;
    }
}
