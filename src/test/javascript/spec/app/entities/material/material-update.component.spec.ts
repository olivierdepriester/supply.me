/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialUpdateComponent } from 'app/entities/material/material-update.component';
import { MaterialService } from 'app/entities/material/material.service';
import { Material } from 'app/shared/model/material.class.model';

describe('Component Tests', () => {
    describe('Material Management Update Component', () => {
        let comp: MaterialUpdateComponent;
        let fixture: ComponentFixture<MaterialUpdateComponent>;
        let service: MaterialService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialUpdateComponent]
            })
                .overrideTemplate(MaterialUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MaterialUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Material(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.material = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Material();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.material = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
