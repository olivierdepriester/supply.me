/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DeliveryNoteService } from 'app/entities/delivery-note/delivery-note.service';
import { IDeliveryNote, DeliveryNote, DeliveryNoteStatus } from 'app/shared/model/delivery-note.model';

describe('Service Tests', () => {
    describe('DeliveryNote Service', () => {
        let injector: TestBed;
        let service: DeliveryNoteService;
        let httpMock: HttpTestingController;
        let elemDefault: IDeliveryNote;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(DeliveryNoteService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new DeliveryNote(0, 'AAAAAAA', currentDate, DeliveryNoteStatus.NEW, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        deliveryDate: currentDate.format(DATE_TIME_FORMAT),
                        creationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a DeliveryNote', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        deliveryDate: currentDate.format(DATE_TIME_FORMAT),
                        creationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        deliveryDate: currentDate,
                        creationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new DeliveryNote(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a DeliveryNote', async () => {
                const returnedFromService = Object.assign(
                    {
                        code: 'BBBBBB',
                        deliveryDate: currentDate.format(DATE_TIME_FORMAT),
                        status: 'BBBBBB',
                        creationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        deliveryDate: currentDate,
                        creationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of DeliveryNote', async () => {
                const returnedFromService = Object.assign(
                    {
                        code: 'BBBBBB',
                        deliveryDate: currentDate.format(DATE_TIME_FORMAT),
                        status: 'BBBBBB',
                        creationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        deliveryDate: currentDate,
                        creationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a DeliveryNote', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
