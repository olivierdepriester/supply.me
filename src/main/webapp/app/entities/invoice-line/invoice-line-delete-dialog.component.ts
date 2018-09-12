import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInvoiceLine } from 'app/shared/model/invoice-line.model';
import { InvoiceLineService } from './invoice-line.service';

@Component({
    selector: 'jhi-invoice-line-delete-dialog',
    templateUrl: './invoice-line-delete-dialog.component.html'
})
export class InvoiceLineDeleteDialogComponent {
    invoiceLine: IInvoiceLine;

    constructor(
        private invoiceLineService: InvoiceLineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.invoiceLineService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'invoiceLineListModification',
                content: 'Deleted an invoiceLine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-invoice-line-delete-popup',
    template: ''
})
export class InvoiceLineDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ invoiceLine }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InvoiceLineDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.invoiceLine = invoiceLine;
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
