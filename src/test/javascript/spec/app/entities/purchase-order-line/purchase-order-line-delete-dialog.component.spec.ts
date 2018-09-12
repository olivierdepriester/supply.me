/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { PurchaseOrderLineDeleteDialogComponent } from 'app/entities/purchase-order-line/purchase-order-line-delete-dialog.component';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line/purchase-order-line.service';

describe('Component Tests', () => {
    describe('PurchaseOrderLine Management Delete Component', () => {
        let comp: PurchaseOrderLineDeleteDialogComponent;
        let fixture: ComponentFixture<PurchaseOrderLineDeleteDialogComponent>;
        let service: PurchaseOrderLineService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [PurchaseOrderLineDeleteDialogComponent]
            })
                .overrideTemplate(PurchaseOrderLineDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseOrderLineDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrderLineService);
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
