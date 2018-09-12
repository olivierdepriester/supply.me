/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MutablePropertiesUpdateComponent } from 'app/entities/mutable-properties/mutable-properties-update.component';
import { MutablePropertiesService } from 'app/entities/mutable-properties/mutable-properties.service';
import { MutableProperties } from 'app/shared/model/mutable-properties.model';

describe('Component Tests', () => {
    describe('MutableProperties Management Update Component', () => {
        let comp: MutablePropertiesUpdateComponent;
        let fixture: ComponentFixture<MutablePropertiesUpdateComponent>;
        let service: MutablePropertiesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MutablePropertiesUpdateComponent]
            })
                .overrideTemplate(MutablePropertiesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MutablePropertiesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MutablePropertiesService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new MutableProperties(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mutableProperties = entity;
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
                    const entity = new MutableProperties();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mutableProperties = entity;
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
