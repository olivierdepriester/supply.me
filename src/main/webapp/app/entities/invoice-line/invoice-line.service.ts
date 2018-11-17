import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoiceLine } from 'app/shared/model/invoice-line.model';

type EntityResponseType = HttpResponse<IInvoiceLine>;
type EntityArrayResponseType = HttpResponse<IInvoiceLine[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineService {
    public resourceUrl = SERVER_API_URL + 'api/invoice-lines';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/invoice-lines';

    constructor(private http: HttpClient) {}

    create(invoiceLine: IInvoiceLine): Observable<EntityResponseType> {
        return this.http.post<IInvoiceLine>(this.resourceUrl, invoiceLine, { observe: 'response' });
    }

    update(invoiceLine: IInvoiceLine): Observable<EntityResponseType> {
        return this.http.put<IInvoiceLine>(this.resourceUrl, invoiceLine, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInvoiceLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInvoiceLine[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInvoiceLine[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
