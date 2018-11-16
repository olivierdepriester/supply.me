import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDeliveryNote } from 'app/shared/model/delivery-note.model';

type EntityResponseType = HttpResponse<IDeliveryNote>;
type EntityArrayResponseType = HttpResponse<IDeliveryNote[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryNoteService {
    private resourceUrl = SERVER_API_URL + 'api/delivery-notes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/delivery-notes';

    constructor(private http: HttpClient) {}

    create(deliveryNote: IDeliveryNote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(deliveryNote);
        return this.http
            .post<IDeliveryNote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(deliveryNote: IDeliveryNote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(deliveryNote);
        return this.http
            .put<IDeliveryNote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDeliveryNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeliveryNote[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeliveryNote[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(deliveryNote: IDeliveryNote): IDeliveryNote {
        const copy: IDeliveryNote = Object.assign({}, deliveryNote, {
            deliveryDate:
                deliveryNote.deliveryDate != null && deliveryNote.deliveryDate.isValid() ? deliveryNote.deliveryDate.toJSON() : null,
            creationDate:
                deliveryNote.creationDate != null && deliveryNote.creationDate.isValid() ? deliveryNote.creationDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.deliveryDate = res.body.deliveryDate != null ? moment(res.body.deliveryDate) : null;
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((deliveryNote: IDeliveryNote) => {
            deliveryNote.deliveryDate = deliveryNote.deliveryDate != null ? moment(deliveryNote.deliveryDate) : null;
            deliveryNote.creationDate = deliveryNote.creationDate != null ? moment(deliveryNote.creationDate) : null;
        });
        return res;
    }
}
