/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialDeleteDialogComponent } from 'app/entities/material/material-delete-dialog.component';
import { MaterialService } from 'app/entities/material/material.service';

describe('Component Tests', () => {
    describe('Material Management Delete Component', () => {
        let comp: MaterialDeleteDialogComponent;
        let fixture: ComponentFixture<MaterialDeleteDialogComponent>;
        let service: MaterialService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialDeleteDialogComponent]
            })
                .overrideTemplate(MaterialDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MaterialDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialService);
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
