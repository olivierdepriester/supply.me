import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { IDemand } from 'app/shared/model/demand.model';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-detail',
    templateUrl: './demand-detail.component.html',
    styleUrls: ['./demand.scss']
})
export class DemandDetailComponent implements OnInit {
    demand: IDemand;
    files: IAttachmentFile[];
    editable: boolean;

    constructor(private activatedRoute: ActivatedRoute, private demandService: DemandService, private principal: Principal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand, attachments }) => {
            this.demand = demand;
            this.files = attachments;
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
