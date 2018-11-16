/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialCategoryDeleteDialogComponent } from 'app/entities/material-category/material-category-delete-dialog.component';
import { MaterialCategoryService } from 'app/entities/material-category/material-category.service';

describe('Component Tests', () => {
    describe('MaterialCategory Management Delete Component', () => {
        let comp: MaterialCategoryDeleteDialogComponent;
        let fixture: ComponentFixture<MaterialCategoryDeleteDialogComponent>;
        let service: MaterialCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialCategoryDeleteDialogComponent]
            })
                .overrideTemplate(MaterialCategoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MaterialCategoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialCategoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
