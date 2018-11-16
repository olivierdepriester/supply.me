import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialCategory } from 'app/shared/model/material-category.model';

type EntityResponseType = HttpResponse<IMaterialCategory>;
type EntityArrayResponseType = HttpResponse<IMaterialCategory[]>;

@Injectable({ providedIn: 'root' })
export class MaterialCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/material-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/material-categories';

    constructor(private http: HttpClient) {}

    create(materialCategory: IMaterialCategory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialCategory);
        return this.http
            .post<IMaterialCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(materialCategory: IMaterialCategory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialCategory);
        return this.http
            .put<IMaterialCategory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMaterialCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterialCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterialCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(materialCategory: IMaterialCategory): IMaterialCategory {
        const copy: IMaterialCategory = Object.assign({}, materialCategory, {
            creationDate:
                materialCategory.creationDate != null && materialCategory.creationDate.isValid()
                    ? materialCategory.creationDate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((materialCategory: IMaterialCategory) => {
            materialCategory.creationDate = materialCategory.creationDate != null ? moment(materialCategory.creationDate) : null;
        });
        return res;
    }
}
