/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { PurchaseOrderLineUpdateComponent } from 'app/entities/purchase-order-line/purchase-order-line-update.component';
import { PurchaseOrderLineService } from 'app/entities/purchase-order-line/purchase-order-line.service';
import { PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

describe('Component Tests', () => {
    describe('PurchaseOrderLine Management Update Component', () => {
        let comp: PurchaseOrderLineUpdateComponent;
        let fixture: ComponentFixture<PurchaseOrderLineUpdateComponent>;
        let service: PurchaseOrderLineService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [PurchaseOrderLineUpdateComponent]
            })
                .overrideTemplate(PurchaseOrderLineUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchaseOrderLineUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrderLineService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PurchaseOrderLine(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchaseOrderLine = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PurchaseOrderLine();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchaseOrderLine = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
