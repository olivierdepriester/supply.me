/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DemandCategoryUpdateComponent } from 'app/entities/demand-category/demand-category-update.component';
import { DemandCategoryService } from 'app/entities/demand-category/demand-category.service';
import { DemandCategory } from 'app/shared/model/demand-category.model';

describe('Component Tests', () => {
    describe('DemandCategory Management Update Component', () => {
        let comp: DemandCategoryUpdateComponent;
        let fixture: ComponentFixture<DemandCategoryUpdateComponent>;
        let service: DemandCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DemandCategoryUpdateComponent]
            })
                .overrideTemplate(DemandCategoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DemandCategoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DemandCategoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DemandCategory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.demandCategory = entity;
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
                    const entity = new DemandCategory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.demandCategory = entity;
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
