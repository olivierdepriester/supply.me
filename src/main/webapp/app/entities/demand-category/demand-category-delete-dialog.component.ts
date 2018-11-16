import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDemandCategory } from 'app/shared/model/demand-category.model';
import { DemandCategoryService } from './demand-category.service';

@Component({
    selector: 'jhi-demand-category-delete-dialog',
    templateUrl: './demand-category-delete-dialog.component.html'
})
export class DemandCategoryDeleteDialogComponent {
    demandCategory: IDemandCategory;

    constructor(
        private demandCategoryService: DemandCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.demandCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'demandCategoryListModification',
                content: 'Deleted an demandCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-demand-category-delete-popup',
    template: ''
})
export class DemandCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demandCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DemandCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.demandCategory = demandCategory;
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
