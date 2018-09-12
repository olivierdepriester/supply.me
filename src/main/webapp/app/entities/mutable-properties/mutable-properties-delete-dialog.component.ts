import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMutableProperties } from 'app/shared/model/mutable-properties.model';
import { MutablePropertiesService } from './mutable-properties.service';

@Component({
    selector: 'jhi-mutable-properties-delete-dialog',
    templateUrl: './mutable-properties-delete-dialog.component.html'
})
export class MutablePropertiesDeleteDialogComponent {
    mutableProperties: IMutableProperties;

    constructor(
        private mutablePropertiesService: MutablePropertiesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mutablePropertiesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mutablePropertiesListModification',
                content: 'Deleted an mutableProperties'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mutable-properties-delete-popup',
    template: ''
})
export class MutablePropertiesDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mutableProperties }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MutablePropertiesDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mutableProperties = mutableProperties;
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
