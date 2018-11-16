import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterial } from 'app/shared/model/material.model';

type EntityResponseType = HttpResponse<IMaterial>;
type EntityArrayResponseType = HttpResponse<IMaterial[]>;

@Injectable({ providedIn: 'root' })
export class MaterialService {
    public resourceUrl = SERVER_API_URL + 'api/materials';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/materials';

    constructor(private http: HttpClient) {}

    create(material: IMaterial): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(material);
        return this.http
            .post<IMaterial>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(material: IMaterial): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(material);
        return this.http
            .put<IMaterial>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterial[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterial[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(material: IMaterial): IMaterial {
        const copy: IMaterial = Object.assign({}, material, {
            creationDate: material.creationDate != null && material.creationDate.isValid() ? material.creationDate.toJSON() : null
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
            res.body.forEach((material: IMaterial) => {
                material.creationDate = material.creationDate != null ? moment(material.creationDate) : null;
            });
        }
        return res;
    }
}
