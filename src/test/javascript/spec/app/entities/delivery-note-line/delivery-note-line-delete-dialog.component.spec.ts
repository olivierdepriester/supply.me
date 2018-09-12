/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteLineDeleteDialogComponent } from 'app/entities/delivery-note-line/delivery-note-line-delete-dialog.component';
import { DeliveryNoteLineService } from 'app/entities/delivery-note-line/delivery-note-line.service';

describe('Component Tests', () => {
    describe('DeliveryNoteLine Management Delete Component', () => {
        let comp: DeliveryNoteLineDeleteDialogComponent;
        let fixture: ComponentFixture<DeliveryNoteLineDeleteDialogComponent>;
        let service: DeliveryNoteLineService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteLineDeleteDialogComponent]
            })
                .overrideTemplate(DeliveryNoteLineDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeliveryNoteLineDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeliveryNoteLineService);
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
