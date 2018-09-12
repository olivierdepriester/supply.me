import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDeliveryNoteLine } from 'app/shared/model/delivery-note-line.model';

type EntityResponseType = HttpResponse<IDeliveryNoteLine>;
type EntityArrayResponseType = HttpResponse<IDeliveryNoteLine[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryNoteLineService {
    private resourceUrl = SERVER_API_URL + 'api/delivery-note-lines';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/delivery-note-lines';

    constructor(private http: HttpClient) {}

    create(deliveryNoteLine: IDeliveryNoteLine): Observable<EntityResponseType> {
        return this.http.post<IDeliveryNoteLine>(this.resourceUrl, deliveryNoteLine, { observe: 'response' });
    }

    update(deliveryNoteLine: IDeliveryNoteLine): Observable<EntityResponseType> {
        return this.http.put<IDeliveryNoteLine>(this.resourceUrl, deliveryNoteLine, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDeliveryNoteLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDeliveryNoteLine[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDeliveryNoteLine[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
