import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { DemandAuthorization, DemandStatus, IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { PurchaseOrderLineService } from '../purchase-order-line';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandDetailComponent implements OnInit, OnDestroy {
    demand: IDemand;
    files: IAttachmentFile[];
    purchaseOrderLines: IPurchaseOrderLine[];
    authorization: DemandAuthorization = new DemandAuthorization();
    eventSubscriber: Subscription;

    constructor(
        private activatedRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private demandService: DemandService,
        private purchaseOrderLineService: PurchaseOrderLineService,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand, attachments }) => {
            this.initializeCurrentDemand(demand);
            this.files = attachments;
        });
        this.eventSubscriber = this.eventManager.subscribe('demandComment', response => {
            // Demand reject --> Reload the demand and its events
            this.changeStatus(response.content.status, response.content.comment);
        });
    }

    /**
     * Initialize the current demand model from a demand
     * @param demand Source demand.
     */
    private initializeCurrentDemand(demand: IDemand) {
        this.demand = demand;
        this.principal.identity().then(account => {
            this.authorization = this.demandService.getAuthorizationForDemand(this.demand, account);
        });
        if (this.demand.id) {
            this.purchaseOrderLineService
                .getBydemandId(this.demand.id)
                .subscribe((res: HttpResponse<IPurchaseOrderLine[]>) => (this.purchaseOrderLines = res.body));
        }
    }

    downloadFile(attachmentFile: IAttachmentFile) {
        this.demandService.downloadAttachmentFile(attachmentFile).subscribe((res: Blob) => {
            const element = document.createElement('a');
            element.href = URL.createObjectURL(res);
            element.download = attachmentFile.name;
            document.body.appendChild(element);
            element.click();
        });
    }

    previousState() {
        window.history.back();
    }

    private changeStatus(status: DemandStatus, comment: string) {
        this.demandService
            .changeStatus(this.demand.id, status, comment)
            .subscribe((res: HttpResponse<IDemand>) => this.initializeCurrentDemand(res.body));
    }

    approve(): void {
        this.changeStatus(DemandStatus.APPROVED, null);
    }

    sendToApproval(): void {
        this.changeStatus(DemandStatus.WAITING_FOR_APPROVAL, null);
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
