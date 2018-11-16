import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPurchaseOrderLine } from 'app/shared/model/purchase-order-line.model';

type EntityResponseType = HttpResponse<IPurchaseOrderLine>;
type EntityArrayResponseType = HttpResponse<IPurchaseOrderLine[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderLineService {
    public resourceUrl = SERVER_API_URL + 'api/purchase-order-lines';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-order-lines';

    constructor(private http: HttpClient) {}

    create(purchaseOrderLine: IPurchaseOrderLine): Observable<EntityResponseType> {
        return this.http.post<IPurchaseOrderLine>(this.resourceUrl, purchaseOrderLine, { observe: 'response' });
    }

    update(purchaseOrderLine: IPurchaseOrderLine): Observable<EntityResponseType> {
        return this.http.put<IPurchaseOrderLine>(this.resourceUrl, purchaseOrderLine, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPurchaseOrderLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchaseOrderLine[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchaseOrderLine[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
