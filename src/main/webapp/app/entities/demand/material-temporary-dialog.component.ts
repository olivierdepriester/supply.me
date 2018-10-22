import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalRef, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MaterialService } from 'app/entities/material';
import { IDemand } from 'app/shared/model/demand.model';
import { IMaterial, Material } from 'app/shared/model/material.model';
import { JhiEventManager } from 'ng-jhipster';
import * as moment from 'moment';
import { IMaterialCategory } from 'app/shared/model/material-category.model';
import { MaterialCategoryService } from '../material-category';

@Component({
    selector: 'jhi-material-temporary-dialog',
    templateUrl: './material-temporary-dialog.component.html',
    styles: []
})
export class MaterialTemporaryDialogComponent implements OnInit {
    demand: IDemand;
    material: IMaterial = new Material();
    categories: IMaterialCategory[];

    constructor(
        private materialService: MaterialService,
        private materialCategoryService: MaterialCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.materialCategoryService.query().subscribe(res => (this.categories = res.body));
    }

    save() {
        this.material.partNumber = 'TMP0000000';
        this.material.temporary = true;
        this.material.creationDate = moment();
        this.materialService.create(this.material).subscribe(response => {
            this.eventManager.broadcast({
                name: 'temporaryMaterialCreated',
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
    selector: 'jhi-material-temporary-popup',
    template: ''
})
export class MaterialTemporaryPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ demand }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialTemporaryDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.demand = demand;
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
