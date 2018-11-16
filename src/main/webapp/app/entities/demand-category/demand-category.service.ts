import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDemandCategory } from 'app/shared/model/demand-category.model';

type EntityResponseType = HttpResponse<IDemandCategory>;
type EntityArrayResponseType = HttpResponse<IDemandCategory[]>;

@Injectable({ providedIn: 'root' })
export class DemandCategoryService {
    public resourceUrl = SERVER_API_URL + 'api/demand-categories';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/demand-categories';

    constructor(private http: HttpClient) {}

    create(demandCategory: IDemandCategory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(demandCategory);
        return this.http
            .post<IDemandCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(demandCategory: IDemandCategory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(demandCategory);
        return this.http
            .put<IDemandCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDemandCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDemandCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDemandCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(demandCategory: IDemandCategory): IDemandCategory {
        const copy: IDemandCategory = Object.assign({}, demandCategory, {
            creationDate:
                demandCategory.creationDate != null && demandCategory.creationDate.isValid() ? demandCategory.creationDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((demandCategory: IDemandCategory) => {
                demandCategory.creationDate = demandCategory.creationDate != null ? moment(demandCategory.creationDate) : null;
            });
        }
        return res;
    }
}
