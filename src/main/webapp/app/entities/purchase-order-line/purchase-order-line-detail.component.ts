import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

@Component({
    selector: 'jhi-purchase-order-line-detail',
    templateUrl: './purchase-order-line-detail.component.html'
})
export class PurchaseOrderLineDetailComponent implements OnInit {
    purchaseOrderLine: IPurchaseOrderLine;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseOrderLine }) => {
            this.purchaseOrderLine = purchaseOrderLine;
        });
    }

    previousState() {
        window.history.back();
    }
}
