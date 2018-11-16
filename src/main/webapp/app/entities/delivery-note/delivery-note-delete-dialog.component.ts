import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeliveryNote } from 'app/shared/model/delivery-note.model';
import { DeliveryNoteService } from './delivery-note.service';

@Component({
    selector: 'jhi-delivery-note-delete-dialog',
    templateUrl: './delivery-note-delete-dialog.component.html'
})
export class DeliveryNoteDeleteDialogComponent {
    deliveryNote: IDeliveryNote;

    constructor(
        private deliveryNoteService: DeliveryNoteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.deliveryNoteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'deliveryNoteListModification',
                content: 'Deleted an deliveryNote'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-delivery-note-delete-popup',
    template: ''
})
export class DeliveryNoteDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deliveryNote }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DeliveryNoteDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.deliveryNote = deliveryNote;
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
