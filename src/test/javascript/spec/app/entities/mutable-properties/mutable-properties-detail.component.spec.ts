/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplyMeTestModule } from '../../../test.module';
import { MutablePropertiesDetailComponent } from 'app/entities/mutable-properties/mutable-properties-detail.component';
import { MutableProperties } from 'app/shared/model/mutable-properties.model';

describe('Component Tests', () => {
    describe('MutableProperties Management Detail Component', () => {
        let comp: MutablePropertiesDetailComponent;
        let fixture: ComponentFixture<MutablePropertiesDetailComponent>;
        const route = ({ data: of({ mutableProperties: new MutableProperties(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MutablePropertiesDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MutablePropertiesDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MutablePropertiesDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.mutableProperties).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
