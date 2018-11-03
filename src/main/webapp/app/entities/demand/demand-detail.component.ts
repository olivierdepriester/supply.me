import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { IDemand } from 'app/shared/model/demand.model';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { DemandService } from './demand.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html'
})
export class DemandDetailComponent implements OnInit {
    demand: IDemand;
    files: IAttachmentFile[];
    purchaseOrderLines: IPurchaseOrderLine[];
    editable: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private purchaseOrderLineService: PurchaseOrderLineService,
        private demandService: DemandService,
        private principal: Principal,
        private sanitizer: DomSanitizer
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand, attachments }) => {
            this.demand = demand;
            this.files = attachments;
            this.purchaseOrderLineService.getBydemandId(this.demand.id).subscribe((res: HttpResponse<IPurchaseOrderLine[]>) => {
                this.purchaseOrderLines = res.body;
            });
            this.principal.identity().then(account => {
                this.editable = this.demandService.isEditAllowed(this.demand, account);
            });
        });
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
}
