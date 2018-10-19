import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialCategory } from 'app/shared/model/material-category.model';
import { MaterialCategoryService } from './material-category.service';

@Component({
    selector: 'jhi-material-category-delete-dialog',
    templateUrl: './material-category-delete-dialog.component.html'
})
export class MaterialCategoryDeleteDialogComponent {
    materialCategory: IMaterialCategory;

    constructor(
        private materialCategoryService: MaterialCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.materialCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialCategoryListModification',
                content: 'Deleted an materialCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-material-category-delete-popup',
    template: ''
})
export class MaterialCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialCategory = materialCategory;
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
