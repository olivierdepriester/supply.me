/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { PurchaseOrderDeleteDialogComponent } from 'app/entities/purchase-order/purchase-order-delete-dialog.component';
import { PurchaseOrderService } from 'app/entities/purchase-order/purchase-order.service';

describe('Component Tests', () => {
    describe('PurchaseOrder Management Delete Component', () => {
        let comp: PurchaseOrderDeleteDialogComponent;
        let fixture: ComponentFixture<PurchaseOrderDeleteDialogComponent>;
        let service: PurchaseOrderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [PurchaseOrderDeleteDialogComponent]
            })
                .overrideTemplate(PurchaseOrderDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseOrderDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrderService);
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
