/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MaterialAvailabilityDetailComponent } from 'app/entities/material-availability/material-availability-detail.component';
import { MaterialAvailability } from 'app/shared/model/material-availability.model';

describe('Component Tests', () => {
    describe('MaterialAvailability Management Detail Component', () => {
        let comp: MaterialAvailabilityDetailComponent;
        let fixture: ComponentFixture<MaterialAvailabilityDetailComponent>;
        const route = ({ data: of({ materialAvailability: new MaterialAvailability(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MaterialAvailabilityDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MaterialAvailabilityDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MaterialAvailabilityDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.materialAvailability).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
