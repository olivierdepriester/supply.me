/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialCategoryUpdateComponent } from 'app/entities/material-category/material-category-update.component';
import { MaterialCategoryService } from 'app/entities/material-category/material-category.service';
import { MaterialCategory } from 'app/shared/model/material-category.model';

describe('Component Tests', () => {
    describe('MaterialCategory Management Update Component', () => {
        let comp: MaterialCategoryUpdateComponent;
        let fixture: ComponentFixture<MaterialCategoryUpdateComponent>;
        let service: MaterialCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialCategoryUpdateComponent]
            })
                .overrideTemplate(MaterialCategoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MaterialCategoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialCategoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new MaterialCategory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.materialCategory = entity;
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
                    const entity = new MaterialCategory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.materialCategory = entity;
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
