/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialAvailabilityDeleteDialogComponent } from 'app/entities/material-availability/material-availability-delete-dialog.component';
import { MaterialAvailabilityService } from 'app/entities/material-availability/material-availability.service';

describe('Component Tests', () => {
    describe('MaterialAvailability Management Delete Component', () => {
        let comp: MaterialAvailabilityDeleteDialogComponent;
        let fixture: ComponentFixture<MaterialAvailabilityDeleteDialogComponent>;
        let service: MaterialAvailabilityService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialAvailabilityDeleteDialogComponent]
            })
                .overrideTemplate(MaterialAvailabilityDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MaterialAvailabilityDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialAvailabilityService);
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
