/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DeliveryNoteUpdateComponent } from 'app/entities/delivery-note/delivery-note-update.component';
import { DeliveryNoteService } from 'app/entities/delivery-note/delivery-note.service';
import { DeliveryNote } from 'app/shared/model/delivery-note.model';

describe('Component Tests', () => {
    describe('DeliveryNote Management Update Component', () => {
        let comp: DeliveryNoteUpdateComponent;
        let fixture: ComponentFixture<DeliveryNoteUpdateComponent>;
        let service: DeliveryNoteService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DeliveryNoteUpdateComponent]
            })
                .overrideTemplate(DeliveryNoteUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeliveryNoteUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeliveryNoteService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DeliveryNote(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliveryNote = entity;
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
                    const entity = new DeliveryNote();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliveryNote = entity;
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
