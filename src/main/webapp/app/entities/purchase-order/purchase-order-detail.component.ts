import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';

@Component({
    selector: 'jhi-purchase-order-detail',
    templateUrl: './purchase-order-detail.component.html'
})
export class PurchaseOrderDetailComponent implements OnInit {
    purchaseOrder: IPurchaseOrder;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
            this.purchaseOrder = purchaseOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
