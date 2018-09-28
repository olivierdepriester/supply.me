import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html'
})
export class DemandDetailComponent implements OnInit {
    demand: IDemand;
    purchaseOrderLines: IPurchaseOrderLine[];

    constructor(private activatedRoute: ActivatedRoute, private purchaseOrderLineService: PurchaseOrderLineService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
        });
        this.purchaseOrderLineService.getBydemandId(this.demand.id).subscribe((res: HttpResponse<IPurchaseOrderLine[]>) => {
            this.purchaseOrderLines = res.body;
        });
    }

    previousState() {
        window.history.back();
    }
}
