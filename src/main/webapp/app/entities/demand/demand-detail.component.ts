import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { Principal } from 'app/core';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html'
})
export class DemandDetailComponent implements OnInit {
    demand: IDemand;
    purchaseOrderLines: IPurchaseOrderLine[];
    editable: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private purchaseOrderLineService: PurchaseOrderLineService,
        private demandService: DemandService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand }) => {
            this.demand = demand;
            this.purchaseOrderLineService.getBydemandId(this.demand.id).subscribe((res: HttpResponse<IPurchaseOrderLine[]>) => {
                this.purchaseOrderLines = res.body;
            });
            this.principal.identity().then(account => {
                this.editable = this.demandService.isEditAllowed(this.demand, account);
            });
        });
    }

    previousState() {
        window.history.back();
    }
}
