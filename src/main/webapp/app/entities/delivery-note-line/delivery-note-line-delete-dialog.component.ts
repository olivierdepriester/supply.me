import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';
import { DeliveryNoteLineService } from './delivery-note-line.service';

@Component({
    selector: 'jhi-delivery-note-line-delete-dialog',
    templateUrl: './delivery-note-line-delete-dialog.component.html'
})
export class DeliveryNoteLineDeleteDialogComponent {
    deliveryNoteLine: IDeliveryNoteLine;

    constructor(
        private deliveryNoteLineService: DeliveryNoteLineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.deliveryNoteLineService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'deliveryNoteLineListModification',
                content: 'Deleted an deliveryNoteLine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-delivery-note-line-delete-popup',
    template: ''
})
export class DeliveryNoteLineDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deliveryNoteLine }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DeliveryNoteLineDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.deliveryNoteLine = deliveryNoteLine;
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
