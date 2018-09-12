/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SupplyMeTestModule } from '../../../test.module';
import { DemandDeleteDialogComponent } from 'app/entities/demand/demand-delete-dialog.component';
import { DemandService } from 'app/entities/demand/demand.service';

describe('Component Tests', () => {
    describe('Demand Management Delete Component', () => {
        let comp: DemandDeleteDialogComponent;
        let fixture: ComponentFixture<DemandDeleteDialogComponent>;
        let service: DemandService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SupplyMeTestModule],
                declarations: [DemandDeleteDialogComponent]
            })
                .overrideTemplate(DemandDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DemandDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DemandService);
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
