/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialAvailabilityUpdateComponent } from 'app/entities/material-availability/material-availability-update.component';
import { MaterialAvailabilityService } from 'app/entities/material-availability/material-availability.service';
import { MaterialAvailability } from 'app/shared/model/material-availability.model';

describe('Component Tests', () => {
    describe('MaterialAvailability Management Update Component', () => {
        let comp: MaterialAvailabilityUpdateComponent;
        let fixture: ComponentFixture<MaterialAvailabilityUpdateComponent>;
        let service: MaterialAvailabilityService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialAvailabilityUpdateComponent]
            })
                .overrideTemplate(MaterialAvailabilityUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MaterialAvailabilityUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialAvailabilityService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new MaterialAvailability(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.materialAvailability = entity;
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
                    const entity = new MaterialAvailability();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.materialAvailability = entity;
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
