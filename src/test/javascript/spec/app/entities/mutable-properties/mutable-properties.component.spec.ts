/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SupplyMeTestModule } from '../../../test.module';
import { MutablePropertiesComponent } from 'app/entities/mutable-properties/mutable-properties.component';
import { MutablePropertiesService } from 'app/entities/mutable-properties/mutable-properties.service';
import { MutableProperties } from 'app/shared/model/mutable-properties.model';

describe('Component Tests', () => {
    describe('MutableProperties Management Component', () => {
        let comp: MutablePropertiesComponent;
        let fixture: ComponentFixture<MutablePropertiesComponent>;
        let service: MutablePropertiesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MutablePropertiesComponent],
                providers: []
            })
                .overrideTemplate(MutablePropertiesComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MutablePropertiesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MutablePropertiesService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new MutableProperties(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.mutableProperties[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
