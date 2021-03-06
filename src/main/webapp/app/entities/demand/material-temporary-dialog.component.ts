import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MaterialService } from 'app/entities/material';
import { IMaterial } from 'app/shared/model/material.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-material-temporary-dialog',
    templateUrl: './material-temporary-dialog.component.html',
    styles: []
})
export class MaterialTemporaryDialogComponent implements OnInit {
    material: IMaterial;

    constructor(private materialService: MaterialService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    ngOnInit() {}

    save() {
        this.material.temporary = true;
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
        this.activatedRoute.data.subscribe(({ material }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialTemporaryDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.material = material;
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
