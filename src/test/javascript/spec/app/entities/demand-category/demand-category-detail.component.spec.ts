/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DemandCategoryDetailComponent } from 'app/entities/demand-category/demand-category-detail.component';
import { DemandCategory } from 'app/shared/model/demand-category.model';

describe('Component Tests', () => {
    describe('DemandCategory Management Detail Component', () => {
        let comp: DemandCategoryDetailComponent;
        let fixture: ComponentFixture<DemandCategoryDetailComponent>;
        const route = ({ data: of({ demandCategory: new DemandCategory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DemandCategoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DemandCategoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DemandCategoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.demandCategory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
