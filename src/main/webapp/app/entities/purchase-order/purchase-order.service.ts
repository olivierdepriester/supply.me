import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPurchaseOrder, PurchaseOrderStatus } from 'app/shared/model/purchase-order.model';
import { Demand } from 'app/shared/model/demand.model';

type EntityResponseType = HttpResponse<IPurchaseOrder>;
type EntityArrayResponseType = HttpResponse<IPurchaseOrder[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderService {
    private resourceUrl = SERVER_API_URL + 'api/purchase-orders';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-orders';

    constructor(private http: HttpClient) {}

    create(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
        purchaseOrder.creationDate = moment();
        purchaseOrder.status = PurchaseOrderStatus.NEW;
        purchaseOrder.code = '0000000000';
        const copy = this.convertDateFromClient(purchaseOrder);
        console.log(copy);
        return this.http
            .post<IPurchaseOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(purchaseOrder);
        return this.http
            .put<IPurchaseOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPurchaseOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPurchaseOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPurchaseOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(purchaseOrder: IPurchaseOrder): IPurchaseOrder {
        const copy: IPurchaseOrder = Object.assign({}, purchaseOrder, {
            expectedDate:
                purchaseOrder.expectedDate != null && purchaseOrder.expectedDate.isValid() ? purchaseOrder.expectedDate.toJSON() : null,
            creationDate:
                purchaseOrder.creationDate != null && purchaseOrder.creationDate.isValid() ? purchaseOrder.creationDate.toJSON() : null
        });
        // copy.purchaseOrderLines[0].purchaseOrder = null;
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expectedDate = res.body.expectedDate != null ? moment(res.body.expectedDate) : null;
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((purchaseOrder: IPurchaseOrder) => {
            purchaseOrder.expectedDate = purchaseOrder.expectedDate != null ? moment(purchaseOrder.expectedDate) : null;
            purchaseOrder.creationDate = purchaseOrder.creationDate != null ? moment(purchaseOrder.creationDate) : null;
        });
        return res;
    }
}