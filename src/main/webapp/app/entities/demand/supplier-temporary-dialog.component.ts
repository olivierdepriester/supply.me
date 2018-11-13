import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SupplierService } from 'app/entities/supplier';
import { ISupplier } from 'app/shared/model/supplier.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-supplier-temporary-dialog',
    templateUrl: './supplier-temporary-dialog.component.html',
    styles: []
})
export class SupplierTemporaryDialogComponent implements OnInit {
    supplier: ISupplier;

    constructor(private supplierService: SupplierService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    ngOnInit() {}

    save() {
        this.supplier.temporary = true;
        this.supplier.referenceNumber = 'UNDEFINED';
        this.supplierService.create(this.supplier).subscribe(response => {
            this.eventManager.broadcast({
                name: 'temporarySupplierCreated',
                content: response.body
            });
            this.activeModal.dismiss(true);
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}
@Component({
    selector: 'jhi-supplier-temporary-popup',
    template: ''
})
export class SupplierTemporaryPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ supplier }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SupplierTemporaryDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.supplier = supplier;
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
