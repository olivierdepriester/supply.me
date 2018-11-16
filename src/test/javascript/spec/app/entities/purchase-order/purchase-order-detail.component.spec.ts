/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { PurchaseOrderDetailComponent } from 'app/entities/purchase-order/purchase-order-detail.component';
import { PurchaseOrder } from 'app/shared/model/purchase-order.model';

describe('Component Tests', () => {
    describe('PurchaseOrder Management Detail Component', () => {
        let comp: PurchaseOrderDetailComponent;
        let fixture: ComponentFixture<PurchaseOrderDetailComponent>;
        const route = ({ data: of({ purchaseOrder: new PurchaseOrder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [PurchaseOrderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PurchaseOrderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseOrderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.purchaseOrder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
