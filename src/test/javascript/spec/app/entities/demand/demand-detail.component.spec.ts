/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { DemandDetailComponent } from 'app/entities/demand/demand-detail.component';
import { Demand } from 'app/shared/model/demand.model';

describe('Component Tests', () => {
    describe('Demand Management Detail Component', () => {
        let comp: DemandDetailComponent;
        let fixture: ComponentFixture<DemandDetailComponent>;
        const route = ({ data: of({ demand: new Demand(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DemandDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DemandDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DemandDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.demand).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
