import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { DemandAuthorization, DemandStatus, IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
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
        private principal: Principal
    ) {}

    ngOnInit() {
        // Get the demand, its attachment files and its purchase orders
        this.activatedRoute.data.subscribe(({ demand, attachments, purchaseOrderLines }) => {
            this.initializeCurrentDemand(demand);
            this.files = attachments;
            this.purchaseOrderLines = purchaseOrderLines;
        });
        this.eventSubscriber = this.eventManager.subscribe('demandComment', response => {
            // Demand status change --> Reload the demand and its events
            this.changeStatus(response.content.status, response.content.comment);
        });
    }

    /**
     * Initialize the current demand model from a demand
     * @param demand Source demand.
     */
    private initializeCurrentDemand(demand: IDemand): void {
        this.demand = demand;
        this.principal.identity().then(account => {
            // Set the
            this.authorization = this.demandService.getAuthorizationForDemand(this.demand, account);
        });
    }

    /**
     * Download an attachment file
     * @param attachmentFile : file to be downloaded
     */
    downloadFile(attachmentFile: IAttachmentFile): void {
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

    /**
     * Change the status of the current demand.
     * @param status Status to set.
     * @param comment Comment to set with the status
     */
    private changeStatus(status: DemandStatus, comment: string) {
        this.demandService
            .changeStatus(this.demand.id, status, comment)
            .subscribe((res: HttpResponse<IDemand>) => this.initializeCurrentDemand(res.body));
    }

    /**
     * Approve the current demand
     */
    approve(): void {
        this.changeStatus(DemandStatus.APPROVED, null);
    }

    /**
     * Send the current demand for approval
     */
    sendToApproval(): void {
        this.changeStatus(DemandStatus.WAITING_FOR_APPROVAL, null);
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
