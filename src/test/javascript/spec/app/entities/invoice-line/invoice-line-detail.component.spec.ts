/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { InvoiceLineDetailComponent } from 'app/entities/invoice-line/invoice-line-detail.component';
import { InvoiceLine } from 'app/shared/model/invoice-line.model';

describe('Component Tests', () => {
    describe('InvoiceLine Management Detail Component', () => {
        let comp: InvoiceLineDetailComponent;
        let fixture: ComponentFixture<InvoiceLineDetailComponent>;
        const route = ({ data: of({ invoiceLine: new InvoiceLine(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [InvoiceLineDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InvoiceLineDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InvoiceLineDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.invoiceLine).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
