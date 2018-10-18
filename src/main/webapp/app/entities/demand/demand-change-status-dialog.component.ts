import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDemand, DemandStatus } from 'app/shared/model/demand.model';
import { DemandService } from './demand.service';

@Component({
    selector: 'jhi-demand-change-status-dialog',
    templateUrl: './demand-change-status-dialog.component.html'
})
export class DemandChangeStatusDialogComponent {
    demand: IDemand;
    status: DemandStatus;
    comment: string;

    constructor(public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    saveComment() {
        this.eventManager.broadcast({
            name: 'demandComment',
            content: {
                demand: this.demand,
                status: this.status,
                comment: this.comment
            }
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-demand-change-status-popup',
    template: ''
})
export class DemandChangeStatusPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand, status }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DemandChangeStatusDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.demand = demand;
                this.ngbModalRef.componentInstance.status = status;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
