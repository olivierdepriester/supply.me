/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteDetailComponent } from 'app/entities/delivery-note/delivery-note-detail.component';
import { DeliveryNote } from 'app/shared/model/delivery-note.model';

describe('Component Tests', () => {
    describe('DeliveryNote Management Detail Component', () => {
        let comp: DeliveryNoteDetailComponent;
        let fixture: ComponentFixture<DeliveryNoteDetailComponent>;
        const route = ({ data: of({ deliveryNote: new DeliveryNote(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DeliveryNoteDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeliveryNoteDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.deliveryNote).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
