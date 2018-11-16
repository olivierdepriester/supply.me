import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoice } from 'app/shared/model/invoice.model';

type EntityResponseType = HttpResponse<IInvoice>;
type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
    private resourceUrl = SERVER_API_URL + 'api/invoices';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/invoices';

    constructor(private http: HttpClient) {}

    create(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .put<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvoice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(invoice: IInvoice): IInvoice {
        const copy: IInvoice = Object.assign({}, invoice, {
            sendingDate: invoice.sendingDate != null && invoice.sendingDate.isValid() ? invoice.sendingDate.toJSON() : null,
            dueDate: invoice.dueDate != null && invoice.dueDate.isValid() ? invoice.dueDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.sendingDate = res.body.sendingDate != null ? moment(res.body.sendingDate) : null;
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((invoice: IInvoice) => {
            invoice.sendingDate = invoice.sendingDate != null ? moment(invoice.sendingDate) : null;
            invoice.dueDate = invoice.dueDate != null ? moment(invoice.dueDate) : null;
        });
        return res;
    }
}
