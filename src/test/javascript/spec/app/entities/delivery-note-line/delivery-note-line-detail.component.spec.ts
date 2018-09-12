/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteLineDetailComponent } from 'app/entities/delivery-note-line/delivery-note-line-detail.component';
import { DeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

describe('Component Tests', () => {
    describe('DeliveryNoteLine Management Detail Component', () => {
        let comp: DeliveryNoteLineDetailComponent;
        let fixture: ComponentFixture<DeliveryNoteLineDetailComponent>;
        const route = ({ data: of({ deliveryNoteLine: new DeliveryNoteLine(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteLineDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DeliveryNoteLineDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeliveryNoteLineDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.deliveryNoteLine).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
