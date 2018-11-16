/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialCategoryDetailComponent } from 'app/entities/material-category/material-category-detail.component';
import { MaterialCategory } from 'app/shared/model/material-category.model';

describe('Component Tests', () => {
    describe('MaterialCategory Management Detail Component', () => {
        let comp: MaterialCategoryDetailComponent;
        let fixture: ComponentFixture<MaterialCategoryDetailComponent>;
        const route = ({ data: of({ materialCategory: new MaterialCategory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialCategoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MaterialCategoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MaterialCategoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.materialCategory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
