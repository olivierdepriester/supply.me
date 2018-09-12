/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { InvoiceLineDeleteDialogComponent } from 'app/entities/invoice-line/invoice-line-delete-dialog.component';
import { InvoiceLineService } from 'app/entities/invoice-line/invoice-line.service';

describe('Component Tests', () => {
    describe('InvoiceLine Management Delete Component', () => {
        let comp: InvoiceLineDeleteDialogComponent;
        let fixture: ComponentFixture<InvoiceLineDeleteDialogComponent>;
        let service: InvoiceLineService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [InvoiceLineDeleteDialogComponent]
            })
                .overrideTemplate(InvoiceLineDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InvoiceLineDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvoiceLineService);
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
