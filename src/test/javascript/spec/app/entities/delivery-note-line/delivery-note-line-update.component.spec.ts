/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteLineUpdateComponent } from 'app/entities/delivery-note-line/delivery-note-line-update.component';
import { DeliveryNoteLineService } from 'app/entities/delivery-note-line/delivery-note-line.service';
import { DeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

describe('Component Tests', () => {
    describe('DeliveryNoteLine Management Update Component', () => {
        let comp: DeliveryNoteLineUpdateComponent;
        let fixture: ComponentFixture<DeliveryNoteLineUpdateComponent>;
        let service: DeliveryNoteLineService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteLineUpdateComponent]
            })
                .overrideTemplate(DeliveryNoteLineUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeliveryNoteLineUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeliveryNoteLineService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DeliveryNoteLine(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliveryNoteLine = entity;
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
                    const entity = new DeliveryNoteLine();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliveryNoteLine = entity;
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
