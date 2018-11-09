import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Principal } from 'app/core';
import { IAttachmentFile } from 'app/shared/model/attachment-file.model';
import { IDemand } from 'app/shared/model/demand.model';
import { IMaterial } from 'app/shared/model/material.model';
import * as moment from 'moment';
import { JhiEventManager } from 'ng-jhipster';
import { Observable, Subscription } from 'rxjs';
import { DemandService } from './demand.service';
import { FileUpload } from 'primeng/primeng';
import { IDepartment } from 'app/shared/model/department.model';

@Component({
    selector: 'jhi-demand-update',
    templateUrl: './demand-update.component.html'
})
export class DemandUpdateComponent implements OnInit, OnDestroy {
    private _demand: IDemand;
    isSaving: boolean;
    expectedDate: Date;
    vat: number;
    @ViewChild('editForm') editForm: HTMLFormElement;
    @ViewChild('fileUpload') fileUpload: FileUpload;
    eventSubscriber: Subscription;
    locale: string;
    files: IAttachmentFile[];

    constructor(
        private demandService: DemandService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ demand, attachments }) => {
            this.demand = demand;
            this.files = attachments;
            if (demand.id != null) {
                this.principal.identity().then(account => {
                    if (!this.demandService.isEditAllowed(this.demand, account)) {
                        this.router.navigate(['accessdenied']);
                    }
                    this.locale = 'en';
                });
            } else {
                this.demand.urgent = false;
                this.vat = 16;
            }
        });
        // Callback on new material creation
        this.eventSubscriber = this.eventManager.subscribe('temporaryMaterialCreated', response => {
            this.demand.material = response.content;
            this.demand.estimatedPrice = this.demand.material.estimatedPrice;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    onMaterialChange(event: IMaterial) {
        if (event) {
            this.demand.estimatedPrice = event.estimatedPrice;
        }
    }

    onDepartmentChange(event: IDepartment) {
        if (event && event.defaultProject && this.demand.project == null) {
            this.demand.project = event.defaultProject;
        }
    }

    previousState() {
        window.history.back();
    }

    /**
     * Upload the selected files on the server and get their identifier
     *
     * @param {*} event Files to load
     * @memberof DemandUpdateComponent
     */
    uploadFiles(event: any) {
        this.isSaving = true;
        const formData = new FormData();
        for (const file of event.files) {
            formData.append('files', file);
        }
        this.demandService.uploadDraftFiles(formData).subscribe(
            (response: HttpResponse<IAttachmentFile[]>) => {
                response.body.forEach(af => this.files.push(af));
                this.fileUpload.clear();
                this.isSaving = false;
            },
            (response: HttpErrorResponse) => {
                this.onSaveError();
            }
        );
    }

    save() {
        this.isSaving = true;
        this.demand.expectedDate = moment(this.expectedDate);
        this.demand.vat = this.vat / 100;
        if (this.demand.id !== undefined) {
            this.subscribeToSaveResponse(this.demandService.update(this.demand));
        } else {
            this.subscribeToSaveResponse(this.demandService.create(this.demand));
        }
    }

    removeFile(attachment: IAttachmentFile): void {
        if (attachment.temporaryToken) {
            // Remove a newly created file --> Remove the draft already uploaded on the server
            this.demandService
                .removeDraftFile(attachment.temporaryToken)
                .subscribe((response: HttpResponse<any>) => this.files.splice(this.files.indexOf(attachment), 1));
        } else {
            // Remove the file from the list. The whole list will be saved after the demand has.
            this.files.splice(this.files.indexOf(attachment), 1);
        }
    }

    downloadFile(attachment: IAttachmentFile) {
        this.demandService.downloadAttachmentFile(attachment).subscribe((res: Blob) => {
            const element = document.createElement('a');
            element.href = URL.createObjectURL(res);
            element.download = attachment.name;
            document.body.appendChild(element);
            element.click();
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDemand>>) {
        result.subscribe(
            (res: HttpResponse<IDemand>) => {
                this.demandService.saveAttachedFiles(res.body, this.files).subscribe(
                    (filesResponse: HttpResponse<IAttachmentFile[]>) => {
                        this.files = filesResponse.body;
                        this.onSaveSuccess();
                    },
                    (filesResponse: HttpErrorResponse) => this.onSaveError()
                );
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get demand() {
        return this._demand;
    }

    set demand(demand: IDemand) {
        this._demand = demand;
        if (demand.expectedDate != null) {
            this.expectedDate = demand.expectedDate.toDate();
        } else {
            this.expectedDate = null;
        }
        if (demand.vat != null) {
            this.vat = demand.vat * 100;
        } else {
            this.vat = null;
        }
    }
}
