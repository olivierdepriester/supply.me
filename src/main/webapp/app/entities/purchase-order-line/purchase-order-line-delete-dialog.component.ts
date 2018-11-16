import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';
import { PurchaseOrderLineService } from './purchase-order-line.service';

@Component({
    selector: 'jhi-purchase-order-line-delete-dialog',
    templateUrl: './purchase-order-line-delete-dialog.component.html'
})
export class PurchaseOrderLineDeleteDialogComponent {
    purchaseOrderLine: IPurchaseOrderLine;

    constructor(
        private purchaseOrderLineService: PurchaseOrderLineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseOrderLineService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseOrderLineListModification',
                content: 'Deleted an purchaseOrderLine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-order-line-delete-popup',
    template: ''
})
export class PurchaseOrderLineDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseOrderLine }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PurchaseOrderLineDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.purchaseOrderLine = purchaseOrderLine;
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
