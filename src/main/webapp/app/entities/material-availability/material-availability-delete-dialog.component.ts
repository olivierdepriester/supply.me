import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialAvailability } from 'app/shared/model/material-availability.model';
import { MaterialAvailabilityService } from './material-availability.service';

@Component({
    selector: 'jhi-material-availability-delete-dialog',
    templateUrl: './material-availability-delete-dialog.component.html'
})
export class MaterialAvailabilityDeleteDialogComponent {
    materialAvailability: IMaterialAvailability;

    constructor(
        private materialAvailabilityService: MaterialAvailabilityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.materialAvailabilityService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialAvailabilityListModification',
                content: 'Deleted an materialAvailability'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-material-availability-delete-popup',
    template: ''
})
export class MaterialAvailabilityDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialAvailability }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialAvailabilityDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialAvailability = materialAvailability;
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
