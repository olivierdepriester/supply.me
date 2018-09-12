/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { PurchaseOrderLineDetailComponent } from 'app/entities/purchase-order-line/purchase-order-line-detail.component';
import { PurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

describe('Component Tests', () => {
    describe('PurchaseOrderLine Management Detail Component', () => {
        let comp: PurchaseOrderLineDetailComponent;
        let fixture: ComponentFixture<PurchaseOrderLineDetailComponent>;
        const route = ({ data: of({ purchaseOrderLine: new PurchaseOrderLine(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [PurchaseOrderLineDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PurchaseOrderLineDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseOrderLineDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.purchaseOrderLine).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
