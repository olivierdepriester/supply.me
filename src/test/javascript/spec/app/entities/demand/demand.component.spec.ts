/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SupplyMeTestModule } from '../../../test.module';
import { DemandComponent } from 'app/entities/demand/demand.component';
import { DemandService } from 'app/entities/demand/demand.service';
import { Demand } from 'app/shared/model/demand.model';

describe('Component Tests', () => {
    describe('Demand Management Component', () => {
        let comp: DemandComponent;
        let fixture: ComponentFixture<DemandComponent>;
        let service: DemandService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DemandComponent],
                providers: []
            })
                .overrideTemplate(DemandComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DemandComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DemandService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Demand(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.demands[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
