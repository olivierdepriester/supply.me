/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteDeleteDialogComponent } from 'app/entities/delivery-note/delivery-note-delete-dialog.component';
import { DeliveryNoteService } from 'app/entities/delivery-note/delivery-note.service';

describe('Component Tests', () => {
    describe('DeliveryNote Management Delete Component', () => {
        let comp: DeliveryNoteDeleteDialogComponent;
        let fixture: ComponentFixture<DeliveryNoteDeleteDialogComponent>;
        let service: DeliveryNoteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteDeleteDialogComponent]
            })
                .overrideTemplate(DeliveryNoteDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeliveryNoteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeliveryNoteService);
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
