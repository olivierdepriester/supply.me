import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDemand } from 'app/shared/model/demand.model';

type EntityResponseType = HttpResponse<IDemand>;
type EntityArrayResponseType = HttpResponse<IDemand[]>;

@Injectable({ providedIn: 'root' })
export class DemandService {
    private resourceUrl = SERVER_API_URL + 'api/demands';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/demands';

    constructor(private http: HttpClient) {}

    create(demand: IDemand): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(demand);
        return this.http
            .post<IDemand>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(demand: IDemand): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(demand);
        return this.http
            .put<IDemand>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDemand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDemand[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDemand[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(demand: IDemand): IDemand {
        const copy: IDemand = Object.assign({}, demand, {
            expectedDate: demand.expectedDate != null && demand.expectedDate.isValid() ? demand.expectedDate.toJSON() : null,
            creationDate: demand.creationDate != null && demand.creationDate.isValid() ? demand.creationDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.expectedDate = res.body.expectedDate != null ? moment(res.body.expectedDate) : null;
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((demand: IDemand) => {
            demand.expectedDate = demand.expectedDate != null ? moment(demand.expectedDate) : null;
            demand.creationDate = demand.creationDate != null ? moment(demand.creationDate) : null;
        });
        return res;
    }
}
