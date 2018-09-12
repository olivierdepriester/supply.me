/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { MutablePropertiesDeleteDialogComponent } from 'app/entities/mutable-properties/mutable-properties-delete-dialog.component';
import { MutablePropertiesService } from 'app/entities/mutable-properties/mutable-properties.service';

describe('Component Tests', () => {
    describe('MutableProperties Management Delete Component', () => {
        let comp: MutablePropertiesDeleteDialogComponent;
        let fixture: ComponentFixture<MutablePropertiesDeleteDialogComponent>;
        let service: MutablePropertiesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [MutablePropertiesDeleteDialogComponent]
            })
                .overrideTemplate(MutablePropertiesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MutablePropertiesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MutablePropertiesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
